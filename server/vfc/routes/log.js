'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('log');

/**
 * @api {GET} /log/queryLogByCondition  按条件查询操作日志
 * @apiGroup Log
 * @apiVersion 1.0.0
 * @apiDescription 按条件查询操作日志
 * @apiSuccess {Array}  rs  操作日志数组
 */
router.get('/queryLogByCondition',
    Libs.router(async (ctx, next) => {
       return await Domain.services.alarm.queryLogByCondition();
    })
);

module.exports = router;
