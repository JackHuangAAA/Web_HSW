/**
 * Created by Administrator on 2019/9/27 0027.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('temperature');

/**
 * @api {GET} /device/queryDevices  查询设备信息
 * @apiGroup device
 * @apiVersion 1.0.0
 * @apiDescription 查询设备信息
 * @apiParam {Number} [alias] 自定义编号
 * @apiParam {String} [code] 设备序列号
 * @apiParam {Number} [type] 类型
 * @apiParam {String} [unitCode] 所属单位
 * @apiParam {String} [cabinetNo] 接种台编号
 * @apiSuccess {JSON}  Object  device model 数组
 */
router.get('/queryDevices',
    Libs.router( async (ctx, next) => {
        return await Domain.services.device.queryDevices(ctx.request.query);
    })
);

/**
 * @api {POST} /device/saveDevice  增加设备信息
 * @apiGroup device
 * @apiVersion 1.0.0
 * @apiDescription 增加设备信息
 * @apiParam {JSON}  Object  device model
 * @apiSuccess {JSON}  Object  device model
 */
router.post('/saveDevice',
    Libs.router( async (ctx, next) => {
        return await Domain.services.device.saveDevice(ctx.request.body);
    })
);

/**
 * @api {GET} /device/queryDeviceByCondition  按条件查询设备信息
 * @apiGroup device
 * @apiVersion 1.0.0
 * @apiDescription 按条件查询设备信息
 * @apiParam {Number} [alias] 自定义编号
 * @apiParam {String} [code] 设备序列号
 * @apiParam {String} [cabinetNo] 接种台编号
 * @apiSuccess {JSON}  Object  version model数组
 */
router.get('/queryDeviceByCondition',
    Libs.router(async (ctx, next) => {
        return await Domain.services.device.queryDeviceByCondition(ctx.request.query);
    })
);

module.exports = router;