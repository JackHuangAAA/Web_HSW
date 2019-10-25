'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('zcy');


/**
 * @api {GET} /checkUser  用户登陆校验
 * @apiGroup zcy
 * @apiVersion 1.0.0
 * @apiDescription 调用政采云用户信息接口，校验用户登录信息
 * @apiParam {String} code  登陆账号
 * @apiSuccess {String}  password  登陆密码
 * @apiSuccess {Json}  Object  返回:{code:'xxx',name:'xxx',check:true|false}
 */
router.get('/checkUser',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.zcy.checkUser(ctx.request.query);
    })
);

/**
 * @api {GET} /queryDistrict  查询行政区域信息
 * @apiGroup zcy
 * @apiVersion 1.0.0
 * @apiDescription 调用政采云行政区域信息接口，查询行政区域
 * @apiSuccess Json  Object 行政区域数组
 */
router.get('/queryDistrict',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.zcy.queryDistrict(ctx.request.query);
    })
);

/**
 * @api {GET} /queryUnit  查询单位信息
 * @apiGroup zcy
 * @apiVersion 1.0.0
 * @apiDescription 调用政采云行单位信息接口，查询单位信息
 * @apiSuccess Json  Object 单位信息数组
 */
router.get('/queryUnit',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.zcy.queryUnit(ctx.request.query);
    })
);

/**
 * @api {GET} /queryCabinetNo  查询柜台编号信息
 * @apiGroup zcy
 * @apiVersion 1.0.0
 * @apiDescription 调用政采云柜台编号信息接口，查询柜台编号信息
 * @apiSuccess Json  Object 柜台编号信息数组
 */
router.get('/queryCabinetNo',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.zcy.queryCabinetNo(ctx.request.query);
    })
);

/**
 * @api {GET} /queryVaccine  查询疫苗信息
 * @apiGroup zcy
 * @apiVersion 1.0.0
 * @apiDescription 调用政采云柜台编号信息接口，查询疫苗信息
 * @apiSuccess Json  Object 疫苗信息
 */
router.get('/queryVaccine',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.zcy.queryVaccine(ctx.request.query);
    })
);

/**
 * @api {GET} /queryVaccineKinds  查询疫苗总种类信息
 * @apiGroup zcy
 * @apiVersion 1.0.0
 * @apiDescription 调用政采云柜台编号信息接口，查询疫苗信息
 * @apiSuccess Json  Object 疫苗信息
 */
router.get('/queryVaccineKinds',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.zcy.queryVaccineKinds(ctx.request.query);
    })
);

/**
 * @api {POST} /receiveVaccination  接收接种信息
 * @apiGroup zcy
 * @apiVersion 1.0.0
 * @apiDescription 接收政采云推送的接种信息
 * @apiSuccess Json  Object 响应信息
 */
router.post('/receiveVaccination',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.zcy.receiveVaccination(ctx.request.body);
    })
);

/**
 * @api {POST} /receiveVaccinationStatus  接收任务完成状态
 * @apiGroup zcy
 * @apiVersion 1.0.0
 * @apiDescription 接收政采云推送的接种信息
 * @apiSuccess Json  Object 响应信息
 */
router.post('/receiveVaccinationStatus',
    Libs.router(async (ctx, next) => {
        return await Domain.thirds.zcy.receiveVaccinationStatus(ctx.request.body);
    })
);
module.exports = router;