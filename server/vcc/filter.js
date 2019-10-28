/**
 * Created by Administrator on 2019/9/24.
 */

'use strict';

const logger = Libs.logger.getLogger('filter');
let moment = require('moment');
const excluded = [
  '/zcy/checkUser',
  '/user/modifyUserByCode',
  '/device/queryDeviceByCondition',
  '/device',
  '/user/queryUserByCondition'
];

const bindUserFilter = async (ctx,next) => {
    try {
        //绕过用户判断进行操作流水保存，正式测试时将该函数放在下方//保存操作记录！！！
        await operationlogSave(ctx);
        //检查设备是否已经接入平台
        let result = await deviceFilter(ctx);
        if(result){
            //检查是否已经登录(//!_.endsWith(ctx.url,'test=0' 测试使用)
            if (
                _.find(excluded, v => {
                    return _.startsWith(ctx.url, v);
                }) == undefined &&
                !_.endsWith(ctx.url, 'test=0')
            ) {
                let token = ctx.cookies.get('token');
                if (!_.isEmpty(token)) {
                    let result = await Domain.services.user.checkToken(token);
                    ctx.currentUser = result;
                    await next();
                } else {
                    ctx.body = Libs.response.error(Libs.error('0001', '未登录'));
                }
            } else {
                //保存操作流水
                await next();
            }
        } else {
            ctx.body = Libs.response.error(Libs.error('9999', '使用设备尚未接入平台'));
        }
    } catch (err) {
        ctx.body = Libs.response.error(err);
    }
}

/**
 * 检查设备是否存在
 * 不存在时，初始化设备基本信息（设备信息增加、设备抽屉生成）
 * @param header
 * @returns {Promise.<boolean>}
 */
const deviceFilter = async ctx => {
    logger.info(`device header info:${JSON.stringify(ctx.header)}`);
    let deviceCode = ctx.header['deviceid'];
    if (deviceCode != null) {
        let device = await Domain.services.device.queryDeviceByCondition({
            code: deviceCode
        });
        if (_.isEmpty(device)) {
            //初始化设备
            logger.info(`add device,deviceCode:${deviceCode}`);
            device = await Domain.services.device.saveDevice({
                code: deviceCode, //设备序列号
                type: 1 //设备类型(1:接种柜;2:冷藏柜)
            });
            //初始添加抽屉
            let darwers = []
            for (let y = 1; y < 6; y++) {
                for (let x = 1; x < 3; x++) {
                    let temp = {
                        device: device._id,
                        x: x, //列
                        y: y, //行
                        vaccine: []
                    };
                    darwers.push(temp);
                }
            }
            await Domain.services.drawer.saveDrawer(darwers);
        }
        //保存心跳时间
        let now = new moment();
        await Domain.services.cache.setCacheToString(
            deviceCode + '_heartbeat',
            _.toString(now)
        );
        ctx.currentDevice = device;
        return true;
    }
    return false;
}

module.exports = {
    register: async app => {
        app.use(bindUserFilter);
        return app;
    }
};
//接种柜动作（1、签到；2、签退;3、入库（包含批量）;4、出库（包含批量）;5、接种出库;6、接种信息）
const operationlogSave = async (ctx) => {
    let logMap = new Map([
        ["key_checkIn","/user/modifyUserByCode"],
        ["key_checkOut","/user/logout"],
        ["key_saveInout","/inout/saveInout"],
        ["key_saveManyInout","/inout/saveManyInout"],
        ["key_vaccinate","/vaccination/saveVaccination"]
    ]);
    //保存操作记录，正式测试将代码块移至 //保存操作记录！！！处
    //测试定义登录账户信息//直接获取当前登录账号信息//直接获取当前登录账号信息
    let result = new Object();
    result.userCode = "1234567";
    result.userName = "李医生";
    //6接种信息
    if (ctx.url.includes(logMap.get("key_vaccinate"))) {
        await Domain.services.log.saveLog({
            userCode:result.userCode,
            userName: result.userName,
            device: ctx.request.body.device,
            deviceType: 1,
            unitCode: ctx.request.body.unitCode,
            unitName: ctx.request.body.unitName,
            action: "6",
            content:JSON.stringify(ctx.request.body),
            operatorDte: new Date()
        })
    }
    //3,4,5出入库记录
    if (ctx.url.includes(logMap.get("key_saveInout"))) {
        //type=0接种，type=1入库，type=2出库
        let action;
        if (ctx.request.body.type == 1) {
            action ="3";
        } else if(ctx.request.body.type == 2){
            action ="4";
        } else {
            action ="5";
        }

        let query ={
            userCode:result.userCode,
            userName: result.userName,
            device: ctx.request.body.device,
            deviceType: 1,
            unitCode: unitCode,
            unitName: unitName,
            action: action,
            content:JSON.stringify(ctx.request.body),
            operatorDte: new Date()
        };

        await Domain.services.log.saveLog(query);
    }
    //3,4,5出入库记录
    if (ctx.url.includes(logMap.get("key_saveManyInout"))) {
        //type=0接种，type=1入库，type=2出库
        let action;
        if (ctx.request.body[0].type == 1) {
            action ="3";
        } else if(ctx.request.body[0].type == 2){
            action ="4";
        } else {
            action ="5";
        }
        let query ={
            userCode:result.userCode,
            userName: result.userName,
            device: ctx.request.body[0].device,
            deviceType: 1,
            unitCode: ctx.request.body[0].unitCode,
            unitName: ctx.request.body[0].unitName,
            action: action,
            content:JSON.stringify(ctx.request.body),
            operatorDte: new Date()
        };

        await Domain.services.log.saveLog(query);
    }

    //1登录
    if (ctx.url.includes(logMap.get("key_checkIn"))) {
        await Domain.services.log.saveLog({
            userCode: result.userCode,
            userName: result.userName,
            device: ctx.request.body.device,
            deviceType: 1,
            unitCode: unitCode,
            unitName: unitName,
            action: "1",
            content:JSON.stringify(ctx.request.body),
            operatorDte: new Date()
        })
    }
    //退出
    if (ctx.url.includes(logMap.get("key_checkOut"))) {
        await Domain.services.log.saveLog({
            userCode: result.userCode,
            userName: result.userName,
            device: ctx.request.body.device,
            deviceType: 1,
            unitCode: unitCode,
            unitName: unitName,
            action: "2",
            content:JSON.stringify(ctx.request.body),
            operatorDte: new Date()
        })
    }
}
