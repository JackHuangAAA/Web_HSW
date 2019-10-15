'use strict';
const router = require('koa-router')();

/**
 * @api {GET} /permission/loadTree  查询权限信息树
 * @apiGroup permission
 * @apiVersion 1.0.0
 * @apiDescription 查询权限信息树
 * @apiSuccess {JSON}  Object  permission model数组、数量
 */
router.get('/loadTree',
    Libs.router( async (ctx, next) => {
        return await Domain.services.permission.loadTree(ctx.request.query);
    })
);

/**
 * @api {GET} /permission/queryPermission  查询权限信息
 * @apiGroup permission
 * @apiVersion 1.0.0
 * @apiDescription 查询权限信息
 * @apiParam {String} [title] 名称
 * @apiParam {Number} [type] 类型
 * @apiSuccess {JSON}  Object  permission model数组、数量
 */
router.get('/queryPermission',
    Libs.router( async (ctx, next) => {
        let result  = await Domain.services.permission.queryPermission(ctx.request.query);
        return result;
    })
);

/**
 * @api {GET} /permission/queryPermission  根据权限id查询权限信息
 * @apiGroup permission
 * @apiVersion 1.0.0
 * @apiDescription 根据权限id查询权限信息
 * @apiParam {String} [id] 权限id
 * @apiSuccess {JSON}  Object  permission model数组、数量
 */
router.get('/queryPermissionById',
    Libs.router( async (ctx, next) => {
        let result  = await Domain.services.permission.queryPermissionById(ctx.request.query);
        return result;
    })
);

/**
 * @api {POST} /permission/savePermission  增加权限信息
 * @apiGroup permission
 * @apiVersion 1.0.0
 * @apiDescription 增加权限信息
 * @apiParam {Array}  menuData 菜单数组
 * @apiParam {String}  id 角色id
 * @apiSuccess {JSON}  Object  permission model
 */
router.post('/savePermission',
    Libs.router( async (ctx, next) => {
        return await Domain.services.permission.savePermission(ctx, ctx.request.body);
    })
);

/**
 * @api {POST} /permission/modifyPermission  修改权限信息
 * @apiGroup permission
 * @apiVersion 1.0.0
 * @apiDescription 修改权限信息
 * @apiParam {Array}  menuData 菜单数组
 * @apiParam {String}  id 角色id
 * @apiSuccess {JSON}  Object  更新状态信息
 */
router.post('/modifyPermission',
    Libs.router( async (ctx, next) => {
        return await Domain.services.permission.modifyPermission(ctx.request.body);
    })
);

/**
 * @api {POST} /permission/removePermissionById  根据id删除权限信息
 * @apiGroup permission
 * @apiVersion 1.0.0
 * @apiDescription 根据id删除权限信息
 * @apiParam {String}  id  主键 _id
 * @apiParam {String}  parent  父节点 _id,当parent!=0时，删除子节点，否则删除节点
 * @apiSuccess {JSON}  Object  删除状态信息
 */
router.post('/removePermissionById',
    Libs.router( async (ctx, next) => {
        return await Domain.services.permission.removePermissionById(ctx.request.body);
    })
);


module.exports = router;