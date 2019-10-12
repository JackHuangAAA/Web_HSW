'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('alarm');

/**
 * @api {GET} /alarm/queryAlarmByByCondition  查询当天报警次数及报警信息
 * @apiGroup Alarm
 * @apiVersion 1.0.0
 * @apiDescription 查询当天报警次数及报警信息
 * @apiParam {String} [device] 设备id
 * @apiParam {Number} [type] 报警类型(1:温度异常;2:库存不足,不传查询全部)
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiParam {Date} [date] 当日时间
 * @apiSuccess {Array}  rs  当天报警次数信息数组
 * @apiSuccess {Number}  total 当天报警次数
 */
router.get('/queryAlarmByByCondition',
    Libs.router(async (ctx, next) => {
       return await Domain.services.alarm.queryAlarmByByCondition(ctx.request.query);
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

module.exports = router;
