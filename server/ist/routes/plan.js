'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('plan');

/**
 * @api {GET} /plan/queryPlan  查询接种计划
 * @apiGroup Plan
 * @apiVersion 1.0.0
 * @apiDescription 查询接种计划
 * @apiParam {Number} [sort] 序号
 * @apiParam {String} [vaccine] 疫苗名称
 * @apiSuccess {Array}  rs  接种计划数组
 */
router.get('/queryPlan',
    Libs.router(async (ctx, next) => {
        return await Domain.services.plan.queryPlan(ctx.request.query);
    })
);

/**
 * @api {POST} /plan/savePlan  保存接种计划
 * @apiGroup Plan
 * @apiVersion 1.0.0
 * @apiDescription 保存接种计划
 * @apiParam {Object} json  接种计划
 * @apiSuccess {Object}  json  保存成功的接种计划
 */
router.post('/savePlan',
    Libs.router(async (ctx, next) => {
        return await Domain.services.plan.savePlan(ctx.request.body);
    })
);

/**
 * @api {POST} /plan/removePlan 删除接种计划
 * @apiGroup Plan
 * @apiVersion 1.0.0
 * @apiDescription 删除接种计划
 * @param requestBody
 * @returns {Object}  json  删除成功的接种计划
 */
router.post('/removePlan', Libs.router(async (ctx, next) => {
        return await Domain.services.plan.removePlan(ctx.request.body);
    })
);

/**
 * @api {POST} /plan/modifyPlan  修改接种计划
 * @apiGroup Plan
 * @apiVersion 1.0.0
 * @apiDescription 修改接种计划
 * @apiParam {Object} json  接种计划
 * @apiSuccess {Object}  json  保存成功的接种计划
 */
router.post('/modifyPlan',
    Libs.router(async (ctx, next) => {
        return await Domain.services.plan.modifyPlan(ctx.request.body);
    })
);

/**
 * @api {GET} /plan/queryPlanByCondition  根据条件查询接种计划
 * @apiGroup Plan
 * @apiVersion 1.0.0
 * @apiDescription 根据条件查询接种计划
 * @apiParam {Object} json
 * @apiSuccess [array]    接种计划
 */
router.get('/queryPlanByCondition',
    Libs.router(async (ctx, next) => {
        return await Domain.services.plan.queryPlanByCondition(ctx.request.query);
    })
);

module.exports = router;
