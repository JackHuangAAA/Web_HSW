'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('drawer');

/**
 * @api {GET} /queryDrawerEmpty  查询抽屉里疫苗为空的数据
 * @apiGroup  Drawer
 * @apiVersion 1.0.0
 * @apiDescription 查询抽屉里疫苗为空的数据
 * @apiSuccess {Array}  rs  疫苗为空的数据数组
 * @apiSuccess {Number}  total 疫苗为空的数据总数
 */
router.get('/queryDrawerEmpty',
    Libs.router(async (ctx, next) => {
       return await Domain.services.drawer.queryDrawerEmpty(ctx.request.query);
    })
);

module.exports = router;
