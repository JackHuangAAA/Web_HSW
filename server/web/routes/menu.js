'use strict';
const router=require('koa-router')();

/**
 * 查询用户权限分配菜单
 */
router.get('/queryMenus',
    Libs.router( async (ctx, next) => {
        return await Domain.services.menu.queryMenus();
    })
);

module.exports = router;