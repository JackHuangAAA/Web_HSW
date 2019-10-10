/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';

const logger = Libs.logger.getLogger('filter');
let moment = require('moment');
const excluded = ['/user/login','/device'];

let logMap = new Map();
let key_checkIn="",key_checkOut="",key_getVaccines="",key_inVcc="",key_vaccinate="";
logMap.set(key_checkIn,"/inout/saveInouts?test=0"); //签到
logMap.set(key_checkOut,"/inout/saveInouts?test=0"); //签退
logMap.set(key_getVaccines,"/inout/saveInouts?test=0"); //取疫苗
logMap.set(key_inoutVcc,"/inout/saveInouts");//出入库
logMap.set(key_vaccinate,"/vaccination/saveVaccination");//接种
const bindUserFilter = async (ctx,next) => {
    try {
        //检查设备是否已经接入平台
        let result = await deviceFilter(ctx);
        if(result){

            //保存操作记录，正式测试将代码块移至 //保存操作记录！！！处
            //测试定义登录账户信息//直接获取当前登录账号信息//直接获取当前登录账号信息
            let result = new Object();
            result.userCode= "1234567";
            result.userName="李医生";
            //接种记录
            if(ctx.url.includes(logMap.get(key_vaccinate))){
                await Domain.services.log.saveLog ({
                    userCode: result.userCode,
                    userName:result.userName,
                    device: ctx.request.body.device,
                    deviceType:"1",
                    unitCode:ctx.request.body.unitCode,
                    unitName:ctx.request.body.unitName,
                    action:"6",
                    content:result.userName+"在编号为"+ctx.request.body.device+"的接种柜"+"为"+ctx.request.body.customer.name+"接种了"+ctx.request.body.customer.vaccineName,
                    operatorDte:ctx.request.body.createDate
                });
            };
            //出入库记录
            if(ctx.url.includes(logMap.get(key_inoutVcc))){
                //type=1入库，type=2出库
                if(ctx.request.body.type==1){
                    let action=4,action_name="入库";
                }else {
                    let action=5,action_name="出库";
                };
                await Domain.services.log.saveLog ({
                    userCode: result.userCode,
                    userName:result.userName,
                    device: ctx.request.body.device,
                    deviceType:"1",
                    unitCode:ctx.request.body.unitCode,
                    unitName:ctx.request.body.unitName,
                    action:action,
                    content:result.userName+"在编号为"+ctx.request.body.device+"的接种柜"+action_name+ctx.request.body.use+"支"+ctx.request.body.vaccineName,
                    operatorDte:ctx.request.body.createDate
                });
            };




            //检查是否已经登录
            if (_.find(excluded, v => {
                    return _.startsWith(ctx.url, v);
                }) == undefined && !_.endsWith(ctx.url,'test=0')) {//!_.endsWith(ctx.url,'test=0' 测试使用
                let token = ctx.cookies.get('token');
                if (!_.isEmpty(token)) {
                    let result = await Domain.services.user.checkToken(token);
                    ctx.currentUser = result;
                   //保存操作记录！！！

                    ctx.url.includes()
                    await next();
                } else {
                    ctx.body = Libs.response.error(Libs.error('0001', '未登录'));
                }
            } else {
                await next();
            }
        }else{
            ctx.body = Libs.response.error(Libs.error('9999', '使用设备尚未接入平台'));
        }
    }catch(err){
        ctx.body = Libs.response.error(err);
    }
};

/**
 * 检查设备是否存在
 * 不存在时，初始化设备基本信息（设备信息增加、设备抽屉生成）
 * @param header
 * @returns {Promise.<boolean>}
 */
const deviceFilter = async (ctx) => {
        logger.info(`device header info:${JSON.stringify(ctx.header)}`);
        let deviceCode = ctx.header['deviceid'];
        if(deviceCode != null){
            let device = await Domain.services.device.queryDeviceByCondition ({code: deviceCode});
            if(_.isEmpty(device)){
                //初始化设备
                logger.info(`add device,deviceCode:${deviceCode}`);
                device = await Domain.services.device.saveDevice(
                    {
                        code: deviceCode,//设备序列号
                        type: 1  //设备类型(1:接种柜;2:冷藏柜)
                    }
                );
                //初始添加抽屉
                let darwers = [];
                for(let y=1;y<6;y++){
                    for(let x=1;x<3;x++){
                        let temp = {
                            'device': device._id,
                            'x':x, //列
                            'y':y, //行
                            'vaccine': null
                        };
                        darwers.push(temp);
                    }
                }
                await Domain.services.drawer.saveDrawer(darwers);
            }
            //保存心跳时间
            let now = new moment();
            await Domain.services.cache.setCacheToString(device.code+"_heartbeat", _.toString(now));
            ctx.currentDevice = device;
            return true;
        }
        return false;
};

module.exports = {
    register: async (app) => {
        app.use(bindUserFilter);
        return app;
    }
};
