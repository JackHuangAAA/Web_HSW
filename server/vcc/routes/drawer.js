'use strict'
const router = require('koa-router')()
const logger = Libs.logger.getLogger('drawer')

/**
 * @api {GET} /drawer/queryDrawerEmpty  查询抽屉里疫苗为空的数据
 * @apiGroup  Drawer
 * @apiVersion 1.0.0
 * @apiDescription 查询抽屉里疫苗为空的数据
 * @apiParam {String} device 设备id
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiSuccess {Array}  rs  疫苗为空的数据数组
 * @apiSuccess {Number}  total 疫苗为空的数据总数
 */
router.get(
  '/queryDrawerEmpty',
  Libs.router(async (ctx, next) => {
    return await Domain.services.drawer.queryDrawerEmpty(ctx.request.query)
  })
);

/**
 * @api {GET} /drawer/queryDrawerByCondition 根据条件查询抽屉信息，并按坐标排序
 * @apiGroup Drawer
 * @apiVersion 1.0.0
 * @apiDescription 根据条件查询抽屉信息，并按坐标排序
 * @apiParam {String} id 抽屉id
 * @apiParam {String} device 设备id
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiSuccess {Array}  data 操作返回数据数组
 */
router.get(
  '/queryDrawerByCondition',
  Libs.router(async (ctx, next) => {
    return await Domain.services.drawer.queryDrawerByCondition(
      ctx.request.query
    )
  })
);

/**
 * @api {POST} /drawer/modifyDrawerById  根据抽屉id更新抽屉信息 （区域划分 增加）
 * @apiGroup  Drawer
 * @apiVersion 1.0.0
 * @apiDescription 根据抽屉id更新抽屉信息 （区域划分）
 * @apiParam {String} id 抽屉id
 * @apiParam {Object} vaccine 疫苗json数据
 * @apiSuccess {Object} data 操作返回数据 新增疫苗数据
 */
router.post(
  '/modifyDrawerById',
  Libs.router(async (ctx, next) => {
    return await Domain.services.drawer.modifyDrawerById(ctx.request.body)
  })
);

/**
 * @api {POST} /drawer/modifyDrawerByIdDec  根据抽屉id更新抽屉信息 （区域划分 减少）
 * @apiGroup  Drawer
 * @apiVersion 1.0.0
 * @apiDescription 根据抽屉id更新抽屉信息 （区域划分）
 * @apiParam {String} id 抽屉id
 * @apiParam {Object} vaccineId 疫苗id
 * @apiSuccess {Object} data 操作返回数据的
 */
router.post(
  '/modifyDrawerByIdDec',
  Libs.router(async (ctx, next) => {
    return await Domain.services.drawer.modifyDrawerByIdDec(ctx.request.body)
  })
);

/**
 * @api {GET} /drawer/queryDrawerByVaccineArr  将每条疫苗抽屉信息平铺
 * @apiGroup  Drawer
 * @apiVersion 1.0.0
 * @apiDescription 根据抽屉id更新抽屉信息 （区域划分）
 * @apiParam {String} id 抽屉id
 * @apiParam {Object} vaccineId 疫苗id
 * @apiSuccess {Object} data 操作返回数据的
 */
router.post('/modifyDrawerByIdDec',
    Libs.router(async (ctx, next) => {
        return await Domain.services.drawer.modifyDrawerByIdDec(ctx.request.body);
    })
);

module.exports = router;