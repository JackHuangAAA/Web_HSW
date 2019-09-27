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
 * @api {GET} /queryAlarmByTemp  查询温度报警次数
 * @apiGroup Alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询温度报警次数
 * @apiSuccess {Array}  rs  当天温度报警次数信息数组
 * @apiSuccess {Number}  total 当天温度报警次数
 */
router.get('/queryAlarmByTemp',
    Libs.router(async (ctx, next) => {
       return await Domain.services.alarm.queryAlarmByTemp(ctx.request.query);
    })
);

module.exports = router;
