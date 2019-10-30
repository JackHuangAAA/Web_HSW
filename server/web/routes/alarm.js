/**
 * Created by Administrator on 2019/10/09 0041.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('alarm');

/**
 * @api {POST} /alarm/saveAlarm  保存报警信息
 * @apiGroup alarm
 * @apiVersion 1.0.0
 * @apiDescription 保存报警信息
 * @apiParam {JSON}  Object  alarm model
 * @apiSuccess {JSON}  Object  alarm model 数组
 */
router.post('/saveAlarm',
    Libs.router( async (ctx, next) => {
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
 * @api {GET} /alarm/queryAlarmByCondition  查询当天报警次数及报警信息
 * @apiGroup Alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询当天报警次数及报警信息
 * @apiParam {String} [device] 设备id
 * @apiParam {Number} [type] 报警类型(1:温度异常;2:库存不足,不传查询全部)
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiParam {Date} [ifToday] 是否当日 当日：today；非当日：不使用改参数
 * @apiSuccess {Array}  rs  当天报警次数信息数组
 * @apiSuccess {Number}  total 当天报警次数
 */
router.get('/queryAlarmByCondition',
    Libs.router(async (ctx, next) => {
        return await Domain.services.alarm.queryAlarmByCondition(ctx.request.query);
    })
);
module.exports = router;