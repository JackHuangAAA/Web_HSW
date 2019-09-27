'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('alarm');

/**
 * @api {GET} /alarm/queryAlarmDailyInfo  查询当天报警信息
 * @apiGroup Alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询当天报警信息
 * @apiSuccess {Array}  rs  当天报警信息数组
 * @apiSuccess {Number}  total 当天报警次数
 */
router.get('/queryAlarmDailyInfo',
    Libs.router(async (ctx, next) => {
       return await Domain.services.alarm.queryAlarmDailyInfo();
    })
);

/**
 * @api {GET} /alarm/queryAlarmByByCondition  查询报警次数
 * @apiGroup Alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询报警次数
 * @apiParam {Number} type 报警类型(1:温度异常;2:库存不足,不传查询全部)
 * @apiSuccess {Array}  rs  当天报警次数信息数组
 * @apiSuccess {Number}  total 当天报警次数
 */
router.get('/queryAlarmByByCondition',
    Libs.router(async (ctx, next) => {
       return await Domain.services.alarm.queryAlarmByByCondition(ctx.request.query);
    })
);

/**
 * @api {GET} /alarm/saveAlarm  保存报警信息
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

module.exports = router;
