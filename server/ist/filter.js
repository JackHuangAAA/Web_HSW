/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';

const logger = Libs.logger.getLogger('filter');
let moment = require('moment');
const excluded = [
    '/device/queryDeviceByCondition',
    '/customer/queryCustomerByCondition'
];

const bindUserFilter = async (ctx,next) => {
    try {
        //检查设备是否已经接入平台
        let result = await deviceFilter(ctx);
        if(result){
            await next();

        } else {
            ctx.body = Libs.response.error(Libs.error('9999', '使用设备尚未接入平台'));
        }
    } catch (err) {
        ctx.body = Libs.response.error(err);
    }
}

/**
 * 检查设备是否存在
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
                type: 3 //设备类型(1:接种柜;2:冷藏柜;3:综合服务终端)
            });
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
