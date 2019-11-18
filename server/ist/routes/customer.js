'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('customer');


/**
 * @api {POST} /customer/saveCustomer  增加客户信息
 * @apiGroup Customer
 * @apiVersion 1.0.0
 * @apiDescription 增加客户信息
 * @apiParam {Object} json  客户信息
 * @apiParam {Number} status [0:接种完成，1：挂号完成，2：缴费完成，3：登记完成]
 * @apiSuccess {Object}  json  客户信息
 */
router.post('/saveCustomer',
    Libs.router(async (ctx, next) => {
        return await Domain.services.customer.saveCustomer(ctx.request.body);
    })
);

/**
 * @api {POST} /customer/removeCustomerById  删除客户信息
 * @apiGroup Customer
 * @apiVersion 1.0.0
 * @apiDescription 删除客户信息
 * @apiParam {String} id 根据id删除客户信息
 * @apiSuccess {Object}  json  删除信息状态
 */
router.post('/removeCustomerById',
    Libs.router(async (ctx, next) => {
        return await Domain.services.customer.removeCustomerById(ctx.request.body);
    })
);

/**
 * @api {POST} /customer/modifyCustomer  更新客户信息
 * @apiGroup Customer
 * @apiVersion 1.0.0
 * @apiDescription 更新客户信息
 * @apiParam {Object} json 更新客户信息
 * @apiSuccess {Object}  json  更新信息状态
 */
router.post('/modifyCustomer',
    Libs.router(async (ctx, next) => {
        return await Domain.services.customer.modifyCustomer(ctx.request.body);
    })
);

/**
 * @api {GET} /customer/queryCustomer  查询客户信息
 * @apiGroup Customer
 * @apiVersion 1.0.0
 * @apiDescription 查询客户信息
 * @apiParam {String} id(可选) 查询客户信息
 * @apiSuccess {Object}  json  客户信息
 */
router.get('/queryCustomer',
    Libs.router(async (ctx, next) => {
        return await Domain.services.customer.queryCustomer(ctx.request.query);
    })
);

/**
 * @api {GET} /customer/queryCustomerByCondition  根据条件查询客户信息
 * @apiGroup Customer
 * @apiVersion 1.0.0
 * @apiDescription 根据条件查询客户信息
 * @apiParam {Object} json 根据条件查询客户信息
 * @apiSuccess {Object}  json  客户信息
 */
router.get('/queryCustomerByCondition',
    Libs.router(async (ctx, next) => {
        return await Domain.services.customer.queryCustomerByCondition(ctx.request.query);
    })
);

module.exports=router;