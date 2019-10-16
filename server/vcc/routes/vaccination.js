/**
 * Created by Huangshaowei on 2019/10/07 0041.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('vaccination');

/**
 * @api {GET} /vaccination/queryVaccination  查询接种记录
 * @apiGroup vaccination
 * @apiVersion 1.0.0
 * @apiDescription 查询接种记录
 *  @apiParam {Number} [size] 每次显示条数
 * @apiParam {String} [user] 操作医生
 * @apiParam {Number} [sort] 排队序号
 * @apiSuccess {JSON}  Object  vaccination model 数组
 */
router.get('/queryVaccination',
    Libs.router( async (ctx, next) => {
        return await Domain.services.vaccination.queryVaccination(ctx.request.query);
    })
);

/**
 * @api {POST} /vaccination/saveVaccination  增加接种记录
 * @apiGroup vaccination
 * @apiVersion 1.0.0
 * @apiDescription 增加接种记录
 * @apiParam {JSON}  Object  vaccination model
 * @apiSuccess {JSON}  Object  vaccination model
 */
router.post('/saveVaccination',
    Libs.router( async (ctx, next) => {
        return await Domain.services.vaccination.saveVaccination(ctx.request.body);
    })
);

/**
 * @api {GET} /vaccination/queryVaccinationByCondition  按条件查询接种
 * @apiGroup vaccination
 * @apiVersion 1.0.0
 * @apiDescription 按条件查询接种记录
 * @apiParam {Number} [deviceid] 设备ID
 * @apiParam {String} [code] 接种序号
 * @apiSuccess {JSON}  Object  version model数组
 */
router.get('/queryVaccinationByCondition',
    Libs.router(async (ctx, next) => {
        return await Domain.services.vaccination.queryVaccinationByCondition(ctx.request.query);
    })
);

/**
 * @api {GET} /vaccination/queryVaccinationDailyInfo  查询当日该设备接种人
 * @apiGroup vaccination
 * @apiVersion 1.0.0
 * @apiDescription 查询当日该设备接种人
 * @apiParam {Number} device   设备ID   ！！！vaccination_models未找到该字段
 * @apiSuccess {JSON}  Object  version model数组
 */
router.get('/queryVaccinationDailyInfo',
    Libs.router(async (ctx, next) => {
        return await Domain.services.vaccination.queryVaccinationDailyInfo(ctx.request.query);
    })
);

module.exports = router;