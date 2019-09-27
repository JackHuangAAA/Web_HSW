'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('alarm');

/**
 * @api {GET} /queryAlarmDailyInfo  查询当天报警信息
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
 * @api {GET} /queryAlarmByByCondition  查询报警次数
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

module.exports = router;
