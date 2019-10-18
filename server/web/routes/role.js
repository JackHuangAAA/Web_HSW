'use strict';
const router = require('koa-router')();

/**
 * @api {GET} /role/queryRole  查询角色信息
 * @apiGroup role
 * @apiVersion 1.0.0
 * @apiDescription 查询角色信息
 * @apiParam {String} [name] 名称
 * @apiParam {Number} [type] 类型
 * @apiSuccess {JSON}  Object  role model数组、数量
 */
router.get('/queryRole',
    Libs.router(async (ctx, next) => {
        return await Domain.services.role.queryRole(ctx.request.query);
    })
);

/**
 * @api {POST} /role/saveRole  增加角色信息
 * @apiGroup role
 * @apiVersion 1.0.0
 * @apiDescription 增加角色信息
 * @apiParam {JSON}  Object  role model
 * @apiSuccess {JSON}  Object  role model
 */
router.post('/saveRole',
    Libs.router(async (ctx, next) => {
        return await Domain.services.role.saveRole(ctx.request.body);
    })
);

/**
 * @api {POST} /role/modifyRole  修改角色信息
 * @apiGroup role
 * @apiVersion 1.0.0
 * @apiDescription 修改角色信息
 * @apiParam {JSON}  Object  role model
 * @apiSuccess {JSON}  Object  更新状态信息
 */
router.post('/modifyRole',
    Libs.router(async (ctx, next) => {
        return await Domain.services.role.modifyRole(ctx.request.body);
    })
);

/**
 * @api {POST} /role/removeRoleById  根据id删除角色信息
 * @apiGroup role
 * @apiVersion 1.0.0
 * @apiDescription 根据id删除角色信息,同时删除权限permission数据
 * @apiParam {String}  id  主键 _id
 * @apiSuccess {JSON}  Object  删除状态信息
 */
router.post('/removeRoleById',
    Libs.router(async (ctx, next) => {
        return await Domain.services.role.removeRoleById(ctx.request.body);
    })
);

/**
 * @api {GET} /role/queryRoleByType  根据类型查询角色信息
 * @apiGroup role
 * @apiVersion 1.0.0
 * @apiDescription 根据类型查询角色信息
 * @apiParam {Number}  type  类型
 * @apiParam {String}  name  角色名称
 * @apiSuccess {JSON}  Object  role model数组
 */
router.get('/queryRoleByType',
    Libs.router(async (ctx, next) => {
        return await Domain.services.role.queryRoleByType(ctx.currentUser, ctx.request.query);
    })
);

/**
 * @api {GET} /role/queryRoleByType  删除角色的permission字段
 * @apiGroup role
 * @apiVersion 1.0.0
 * @apiDescription 删除角色的permission字段
 * @apiParam {String}  id  角色id
 * @apiSuccess {JSON}  Object  role model数组
 */
router.post('/removePermissionByRoleId',
    Libs.router(async (ctx, next) => {
        return await Domain.services.role.removePermissionByRoleId(ctx.request.body);
    })
);

/**
 * @api {GET} /role/queryRoleByCondition  根据指定条件查询角色
 * @apiGroup role
 * @apiVersion 1.0.0
 * @apiDescription 根据指定条件查询角色
 * @apiParam {String} [name] 角色名称
 * @apiParam {String} [type] 角色类型
 * @apiParam {String} [permission] 权限
 * @apiSuccess {JSON}  Object  role model数组
 */
router.get('/queryRoleByCondition',
    Libs.router(async(ctx, next) => {
        return await Domain.services.role.queryRoleByCondition(ctx.request.query);
    })
);


module.exports = router;