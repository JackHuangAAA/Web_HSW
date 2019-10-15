/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('user');

/**
 * 当前用户
 */
router.get('/current',
    Libs.router( async (ctx, next) => {
        return ctx.currentUser;
    })
);

/**
 * 登陆
 */
router.post('/login',
    Libs.router( async (ctx, next) => {
        let token = await Domain.services.user.login(ctx.request.body.code,ctx.request.body.password);
        ctx.cookies.set('token', token);
    })
);

/**
 * 登出
 */
router.get('/logout',
    Libs.router( async (ctx, next) => {
        ctx.cookies.set('token', null);
        ctx.currentUser = null;
    })
);

/**
 * 查询用户信息
 */
router.get('/queryUsers',
    Libs.router( async (ctx, next) => {
        return await Domain.services.user.queryUsers(ctx.request.query);
    })
);

/**
 * 增加用户信息
 */
router.post('/saveUser',
    Libs.router( async (ctx, next) => {
        return await Domain.services.user.saveUser(ctx.request.body);
    })
);

/**
 * 修改用户信息
 */
router.post('/modifyUser',
    Libs.router( async (ctx, next) => {
        return await Domain.services.user.modifyUser(ctx.request.body);
    })
);

/**
 * 删除用户信息
 */
router.post('/removeUserById',
    Libs.router( async (ctx, next) => {
        return await Domain.services.user.removeUserById(ctx.request.body);
    })
);

/**
 * 按指定条件查询用户
 */
router.get('/queryUserByCondition',
    Libs.router( async (ctx, next) => {
        return await Domain.services.user.queryUserByCondition(ctx.request.query);
    })
);

/**
 * 删除指纹信息
 * @apiParam {String} id 用户id
 * @apiParam {String} deviceid 设备ID
 * @apiParam {String} code_delete 要删除的指纹代码
 */
router.post(
    '/deleteFinger',
    Libs.router(async (ctx, next) => {
        return await Domain.services.user.deleteFinger(ctx.request.body);
    })
);

/**
 * 新增指纹信息
 * @apiParam {String} id 用户id
 *  * @apiParam {String} deviceid 设备ID
 * @apiParam {String} code_new 新指纹代码
 */
router.post(
    '/saveFinger',
    Libs.router(async (ctx, next) => {
        return await Domain.services.user.saveFinger(ctx.request.body);
    })
);

module.exports = router;