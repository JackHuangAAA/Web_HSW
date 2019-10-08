'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('vaccination');

/**
 * @api {GET} /vaccination/queryVaccinationByCustomerCode  根据顾客code分组合计当日接种记录
 * @apiGroup Vaccination
 * @apiVersion 1.0.0
 * @apiDescription 根据顾客code分组合计当日接种记录
 * @apiParam {String} device 设备id
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiSuccess {Array}  rs  当天顾客接种信息
 */
router.get('/queryVaccinationByCustomerCode',
    Libs.router(async (ctx, next) => {
       return await Domain.services.vaccination.queryVaccinationByCustomerCode();
    })
);


module.exports = router;
