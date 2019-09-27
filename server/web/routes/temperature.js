/**
 * Created by Administrator on 2019/9/27 0027.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('temperature');

/**
 * @api {GET} /temperature/queryTemperatures  查询温度信息
 * @apiGroup temperature
 * @apiVersion 1.0.0
 * @apiDescription 查询温度信息
 * @apiParam {String} [deviceId] 设备id
 * @apiParam {String} begin 开始时间
 * @apiParam {String} end 结束时间
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.get('/queryTemperatures',
    Libs.router( async (ctx, next) => {
        return await Domain.services.temperature.queryTemperatures(ctx.request.query);
    })
);

/**
 * @api {POST} /temperature/saveTemperature  增加温度信息
 * @apiGroup temperature
 * @apiVersion 1.0.0
 * @apiDescription 增加温度信息
 * @apiParam {JSON}  Object  temperature model
 * @apiSuccess {JSON}  Object  temperature model
 */
router.post('/saveTemperature',
    Libs.router( async (ctx, next) => {
        return await Domain.services.temperature.saveTemperature(ctx.request.body);
    })
);

module.exports = router;