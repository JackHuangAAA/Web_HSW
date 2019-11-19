'use strict';
const router = require('koa-router')();

/**
 * @api {POST} /parameter/saveParameter  增加参数信息
 * @apiGroup parameter
 * @apiVersion 1.0.0
 * @apiDescription 增加参数信息
 * @apiParam {JSON}  Object  parameter model
 * @apiSuccess {JSON}  Object  parameter model
 */
router.post('/saveParameter',
	Libs.router(async (ctx,next) => {
		return await Domain.services.parameter.saveParameter(ctx.request.body);
	})
);

/**
 * @api {GET} /parameter/queryParameter  查询参数信息
 * @apiGroup parameter
 * @apiVersion 1.0.0
 * @apiDescription 查询参数信息
 * @apiParam {String} [name] 名称(中文)
 * @apiParam {String} [key] 名称(英文大写)
 * @apiSuccess {JSON}  Object  parameter model数组、数量
 */
router.get('/queryParameter',
	Libs.router(async (ctx,next) => {
		return await Domain.services.parameter.queryParameter(ctx.request.query);
	})
);

/**
 * @api {POST} /parameter/modifyParameter  修改参数信息
 * @apiGroup parameter
 * @apiVersion 1.0.0
 * @apiDescription 修改参数信息
 * @apiParam {JSON}  Object  parameter model
 * @apiSuccess {JSON}  Object  更新状态信息
 */
router.post('/modifyParameter',
    Libs.router(async (ctx,next) => {
        return await Domain.services.parameter.modifyParameter(ctx.request.body);
    })
);

/**
 * @api {POST} /parameter/removeParameterById  根据id删除参数信息
 * @apiGroup parameter
 * @apiVersion 1.0.0
 * @apiDescription 根据id删除参数信息
 * @apiParam {JSON}  Object  parameter model
 * @apiSuccess {JSON}  Object  删除状态信息
 */
router.post('/removeParameterById',
    Libs.router(async (ctx,next) => {
        return await Domain.services.parameter.removeParameterById(ctx.request.body);
    })
);
/**
 * @api {POST} /parameter/ableOrDisableParameter  根据id停用启用参数信息
 * @apiGroup parameter
 * @apiVersion 1.0.0
 * @apiDescription 根据id停用启用参数信息
 * @apiParam {JSON} String  _id 
 * @apiSuccess {JSON}  Object 停用启用信息
 */
router.post('/displayParameter',
    Libs.router(async (ctx,next) => {
        return await Domain.services.parameter.displayParameter(ctx.request.body);
    })
);

/**
 * @api {GET} /parameter/queryParameterByCondition  按指定条件查询参数信息
 * @apiGroup parameter
 * @apiVersion 1.0.0
 * @apiDescription 按指定条件查询参数信息
 * @apiParam {String} [name] 名称(中文)
 * @apiParam {String} [key] 名称(英文大写)
 * @apiParam {String} [value] 参数值
 * @apiSuccess {JSON}  Object  parameter model数组
 */
router.get('/queryParameterByCondition',
    Libs.router(async (ctx,next) => {
        return await Domain.services.parameter.queryParameterByCondition(ctx.request.query);
    })
);

module.exports = router;
