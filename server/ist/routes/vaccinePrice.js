'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('vaccinePrice');




/**
 * @api {GET} /queryVaccinePrice  查询疫苗价格信息
 * @apiGroup vaccinePrice
 * @apiVersion 1.0.0
 * @apiDescription 调用疫苗价格信息接口，查询疫苗价格
 * @apiSuccess Json  Object 疫苗价格信息
 */
router.get('/queryVaccinePrice',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.vaccinePrice.queryVaccinePrice(ctx.request.query);
    })
);

module.exports = router;