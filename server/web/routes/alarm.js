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
 * @api {GET} /alarm/queryAlarmDailyInfo  查询今日报警信息
 * @apiGroup alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询报警信息
 * @apiParam {String} [ifToday] 是否查询今日信息，若无则查询所有时间
 * @apiParam {String} [device] 设备
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 ** @apiParam {String} [unitName] 所属单位名称
 * @apiParam {Number} [type] 报警类型(1:温度异常;2:库存不足)
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.get('/queryAlarmDailyInfo',
    Libs.router( async (ctx, next) => {
        return await Domain.services.alarm.queryAlarmDailyInfo(ctx.request.query);
    })
);
module.exports = router;