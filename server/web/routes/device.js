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
 * @apiParam {Number} page 第几页
 * @apiParam {Number} size 每页显示数目
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
 * @api {GET} /device/queryDeviceByAggregate  聚合查询，各单位各设备类型不同状态的设备数量统计
 * @apiGroup device
 * @apiVersion 1.0.0
 * @apiDescription 聚合查询，各单位各设备类型不同状态的设备数量统计
 * @apiParam flag 0查询总体设备信息，1查询各单位设备信息
 * @apiSuccess {JSON}  Object  version model数组
 */
router.get('/queryDeviceByAggregate',
    Libs.router(async (ctx, next) => {
        return await Domain.services.device.queryDeviceByAggregate(ctx.currentUser,ctx.request.query);
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
 * @apiParam {Number} [status] 状态：0离线/1在线/2故障
 * @apiParam {Number} [type] 设备类型：1接种柜/2冷藏柜
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiSuccess {JSON}  Object  version model数组
 */
router.get('/queryDeviceByCondition',
    Libs.router(async (ctx, next) => {
        return await Domain.services.device.queryDeviceByCondition(ctx.request.query);
    })
);

/**
 * @api {GET} /device/queryDeviceByCondition  疫苗柜库存查询
 * @apiGroup device
 * @apiVersion 1.0.0
 * @apiDescription 疫苗柜库存查询
 * @apiParam {Number} [type] 设备类型：1接种柜/2冷藏柜
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiSuccess {JSON}  Object  version model数组
 */
router.get('/queryDeviceStock',
    Libs.router(async (ctx, next) => {
        return await Domain.services.device.queryDeviceStock(ctx.request.query);
    })
);

module.exports = router;