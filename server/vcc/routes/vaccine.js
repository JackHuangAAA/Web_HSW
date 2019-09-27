'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('vaccine');


/**
 * @api {GET} /queryVaccine  查询疫苗种类
 * @apiGroup Vaccine
 * @apiVersion 1.0.0
 * @apiDescription 查询疫苗种类
 * @apiParam {String} device 设备id
 * @apiSuccess {Array}  rs  查询疫苗种类数组
 * @apiSuccess {Number}  total 查询疫苗种类总数
 */
router.get('/queryVaccine',
    Libs.router(async (ctx, next) => {
       return await Domain.services.vaccine.queryVaccine(ctx.request.query);
    })
);

/**
 * @api {GET} /queryVaccineLowerThreshold  查询疫苗剩余数量小于报警阈值的疫苗信息
 * @apiGroup Vaccine
 * @apiVersion 1.0.0
 * @apiDescription 查询疫苗剩余数量小于报警阈值的疫苗信息
 * @apiParam {String} device 设备id
 * @apiSuccess {Array}  rs  疫苗剩余数量小于报警阈值的疫苗信息数组
 * @apiSuccess {Number}  total 查询疫苗剩余数量小于报警阈值的疫苗信息总数
 */
router.get('/queryVaccineLowerThreshold',
    Libs.router(async (ctx, next) => {
       return await Domain.services.vaccine.queryVaccineLowerThreshold(ctx.request.query);
    })
);

module.exports = router;
