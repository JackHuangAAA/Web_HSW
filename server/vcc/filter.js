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

                    //保存操作流水
                    await operationlogSave(ctx);
                    await next();
                } else {
                    ctx.body = Libs.response.error(Libs.error('0001', '未登录'));
                }
            } else {
                //保存操作流水
                //await operationlogSave(ctx);
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

    let userCode = ctx.currentUser.code;
    let userName = ctx.currentUser.name;

    let deviceCode = ctx.header['deviceid'];
    let device = await Domain.services.device.queryDeviceByCondition({
        code: deviceCode
    });
    let unitCode=device[0].unitCode;
    let unitName=device[0].unitName;
    let deviceId=device[0]._id;
    //6接种信息
    if (ctx.url.includes(logMap.get("key_vaccinate"))) {
        await Domain.services.log.saveLog({
            userCode:userCode,
            userName: userName,
            device: ctx.request.body.device,
            deviceType: 1,
            unitCode: unitCode,
            unitName: unitName,
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
            userCode: userCode,
            userName: userName,
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
            userCode:userCode,
            userName: userName,
            device: ctx.request.body[0].device,
            deviceType: 1,
            unitCode: unitCode,
            unitName: unitName,
            action: action,
            content:JSON.stringify(ctx.request.body),
            operatorDte: new Date()
        };

        await Domain.services.log.saveLog(query);
    }


    //1登录 保存方法写在vcc-routes-user
    /* if (ctx.url.includes(logMap.get("key_checkIn"))) {
        await Domain.services.log.saveLog({
            userCode: userCode,
            userName: userName,
            device: deviceId,
            deviceType: 1,
            unitCode: unitCode,
            unitName: unitName,
            action: "1",
            content:JSON.stringify(ctx.request.body),
            operatorDte: new Date()
        })
    }*/
    //退出
    if (ctx.url.includes(logMap.get("key_checkOut"))) {
        await Domain.services.log.saveLog({
            userCode: userCode,
            userName: userName,
            device: deviceId,
            deviceType: 1,
            unitCode: unitCode,
            unitName: unitName,
            action: "2",
            content:JSON.stringify(ctx.request.body),
            operatorDte: new Date()
        })
    }
}
