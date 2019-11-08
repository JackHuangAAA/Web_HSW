'use strict';
const router=require('koa-router')();

/**
 * @api {GET} /menu/queryMenus  查询用户权限分配菜单
 * @apiGroup menu
 * @apiVersion 1.0.0
 * @apiDescription 查询用户权限分配菜单
 * @apiSuccess {JSON}  Object 
 */
router.get('/queryMenus',
    Libs.router( async (ctx, next) => {
        return await Domain.services.menu.queryMenus();
    })
);

module.exports = router;