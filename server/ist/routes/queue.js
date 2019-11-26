'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('queue');


/**
 * @api {GET} /queue/queryQueue  查询排队信息
 * @apiGroup Queue
 * @apiVersion 1.0.0
 * @apiDescription 查询排队信息
 * @apiParam {String} [code] 接种编号
 * @apiParam {String} [name] 姓名
 * @apiParam {String} [sex] 性别
 * @apiParam {Number} [age] 年龄
 * @apiParam {String} [vaccineName] 接种疫苗名称
 * @apiParam {Number} [next] 是否为下一位操作的查询，1是，不是为空
 * @apiSuccess {JSON}  Object  数组
 */
router.get('/queryQueue',
    Libs.router( async (ctx, next) => {
        return await Domain.services.queue.queryQueue(ctx.request.query);
    })
);

/**
 * @api {POST} /queue/saveQueue  保存排队信息
 * @apiGroup Queue
 * @apiVersion 1.0.0
 * @apiDescription 保存排队信息
 * @apiParam {Object} json  排队信息
 * @apiSuccess {Object}  json  保存成功的排队信息
 */
router.post('/saveQueue',
    Libs.router(async (ctx, next) => {
        return await Domain.services.queue.saveQueue(ctx.request.body);
    })
);

/**
 * @api {POST} /queue/removeQueue 删除排队信息
 * @apiGroup Queue
 * @apiVersion 1.0.0
 * @apiDescription 删除排队信息
 * @param requestBody
 * @returns {Object}  json  删除成功的排队信息
 */
router.post('/removeQueue', Libs.router(async (ctx, next) => {
        return await Domain.services.queue.removeQueue(ctx.request.body);
    })
);

/**
 * @api {POST} /queue/modifyQueue  修改排队信息
 * @apiGroup Queue
 * @apiVersion 1.0.0
 * @apiDescription 修改接种计划
 * @apiParam {Object} json  接种计划
 * @apiSuccess {Object}  json  保存成功的接种计划
 */
router.post('/modifyQueue',
    Libs.router(async (ctx, next) => {
        return await Domain.services.queue.modifyQueue(ctx.request.body);
    })
);

/**
 * @api {GET} /queue/queryQueueByCondition  按条件查询排队信息
 * @apiGroup Queue
 * @apiVersion 1.0.0
 * @apiDescription 按条件查询排队信息
 * @apiParam {String} [code] 接种编号
 * @apiParam {String} [name] 姓名
 * @apiParam {String} [sex] 性别
 * @apiParam {Number} [age] 年龄
 * @apiParam {String} [vaccineName] 接种疫苗名称
 * @apiSuccess {Array}  rs  排队信息数组
 */
router.get('/queryQueueByCondition',
    Libs.router(async (ctx, next) => {
        return await Domain.services.queue.queryQueueByCondition(ctx.request.query);
    })
);
module.exports = router;
