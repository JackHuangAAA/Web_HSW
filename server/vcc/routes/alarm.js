'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('alarm');

/**
 * @api {GET} /alarm/queryAlarmByCondition  查询当天报警次数及报警信息
 * @apiGroup Alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询当天报警次数及报警信息
 * @apiParam {String} [device] 设备id
 * @apiParam {Number} [type] 报警类型(1:温度异常;2:库存不足,不传查询全部)
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiSuccess {Array}  rs  当天报警次数信息数组
 * @apiSuccess {Number}  total 当天报警次数
 */
router.get('/queryAlarmByCondition',
    Libs.router(async (ctx, next) => {
       return await Domain.services.alarm.queryAlarmByCondition(ctx.request.query);
    })
);

/**
 * @api {POST} /alarm/saveAlarm  保存报警信息
 * @apiGroup Alarm
 * @apiVersion 1.0.0
 * @apiDescription 保存报警信息
 * @apiParam {Object} json  报警信息
 * @apiSuccess {Object}  json  保存成功的报警信息
 */
router.post('/saveAlarm',
    Libs.router(async (ctx, next) => {
       return await Domain.services.alarm.saveAlarm(ctx.request.body);
    })
);

/**
 * @api {GET} /alarm/queryAlarm  查询报警信息
 * @apiGroup alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询报警信息
 * @apiParam {String} [device] 设备
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiParam {Number} [type] 报警类型(1:温度异常;2:库存不足)
 * @apiParam {String} [device] 设备
 * @apiParam {String} [device] 设备
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.get('/queryAlarm',
    Libs.router( async (ctx, next) => {
        return await Domain.services.alarm.queryAlarm(ctx.request.query);
    })
);

/**
 * @api {GET} /alarm/queryAlarmNum  查询报警信息
 * @apiGroup alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询报警信息
 * @apiParam {String} [device] 设备
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiParam {Number} [type] 报警类型(1:温度异常;2:库存不足)
 * @apiParam {String} [device] 设备
 * @apiParam {String} [device] 设备
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.get('/queryAlarmNum',
    Libs.router( async (ctx, next) => {
        return await Domain.services.alarm.queryAlarmNum(ctx.request.query);
    })
);
module.exports = router;
