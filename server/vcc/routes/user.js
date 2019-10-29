/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('user');

/**
 * @api {POST} /user/modifyUserByCode  按用户code更新用户信息，不存在时插入，存在时修改用户名
 * @apiGroup User
 * @apiVersion 1.0.0
 * @apiDescription 按用户code更新用户信息，不存在时插入，存在时修改用户名
 * @apiParam {String} code 用户code
 * @apiParam {String} name 用户名称
 * @apiSuccess {Object} data 操作返回数据
 */
router.post(
  '/modifyUserByCode',
  Libs.router(async (ctx, next) => {
    let user = await Domain.services.user.modifyUserByCode(ctx.request.body);
    ctx.cookies.set('token', user.token);
    //登录操作保存至流水日志
    let device = await Domain.models.device.find({code:ctx.header.deviceid});
    let deviceId = device[0]._id;
    let unitCode = device[0].unitCode;
    let unitName = device[0].unitName;
    let type = device[0].type;
    await Domain.services.log.saveLog({
        userCode: user.code,
        userName: user.name,
        device: deviceId,
        deviceType: type,
        unitCode: unitCode,
        unitName: unitName,
        action: "1",
        content:JSON.stringify(ctx.request.body),
        operatorDte: new Date()
      })
    return user;
  })
);

/**
 * @api {GET} /user/queryUserByCondition  按指定条件查询用户信息
 * @apiGroup User
 * @apiVersion 1.0.0
 * @apiDescription 按指定条件查询用户信息
 * @apiParam {String} [code] 用户code
 * @apiParam {String} [name]
 * @apiParam {String} [_id]
 * @apiSuccess {Array} data 操作返回数据数组
 */
router.get(
  '/queryUserByCondition',
  Libs.router(async (ctx, next) => {
    return await Domain.services.user.queryUserByCondition(ctx.request.query);
  })
);

/**
 * @api {POST} /user/queryUserByCondition  按用户id更新指纹信息
 * @apiGroup User
 * @apiVersion 1.0.0
 * @apiDescription 按用户id更新指纹信息
 * @apiParam {String} id 用户id
 * @apiParam {Array} finger 用户指纹数组
 * @apiSuccess {Object}  data 操作返回数据
 */
router.post(
  '/modifyUserById',
  Libs.router(async (ctx, next) => {
    return await Domain.services.user.modifyUserById(ctx.request.body);
  })
);

/**
 * 当前用户
 */
router.get(
  '/current',
  Libs.router(async (ctx, next) => {
    return ctx.currentUser;
  })
);

/**
 * 登陆
 */
/*router.post('/login',
    Libs.router( async (ctx, next) => {
        let token = await Domain.services.user.login(ctx.request.body.code,ctx.request.body.password);
        ctx.cookies.set('token', token);
    })
);*/

/**
 * 登出
 */
router.get(
  '/logout',
  Libs.router(async (ctx, next) => {
    ctx.cookies.set('token', null);
    ctx.currentUser = null;
  })
);

/**
 * 查询用户信息
 */
router.get(
  '/queryUsers',
  Libs.router(async (ctx, next) => {
    return await Domain.services.user.queryUsers(ctx.request.query);
  })
);

/**
 * 增加用户信息
 */
router.post(
  '/saveUser',
  Libs.router(async (ctx, next) => {
    return await Domain.services.user.saveUser(ctx.request.body);
  })
);

/**
 * 修改用户信息
 */
router.post(
  '/modifyUser',
  Libs.router(async (ctx, next) => {
    return await Domain.services.user.modifyUser(ctx.request.body);
  })
);

/**
 * 删除用户信息
 */
router.post(
  '/removeUserById',
  Libs.router(async (ctx, next) => {
    return await Domain.services.user.removeUserById(ctx.request.body);
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

