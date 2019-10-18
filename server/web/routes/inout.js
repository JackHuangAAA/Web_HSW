/**
 * Created by Administrator on 2019/9/27 0027.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('inout');

/**
 * @api {POST} /inout/saveInouts  保存出入库信息
 * @apiGroup inout
 * @apiVersion 1.0.0
 * @apiDescription 保存出入库信息
 * @apiParam {JSON}  Object  inout model
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.post('/saveInouts',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.saveInouts(ctx.request.body);
    })
);

/**
 * @api {GET} /inout/queryInouts  查询出入库信息
 * @apiGroup inout
 * @apiVersion 1.0.0
 * @apiDescription 查询出入库信息
 * @apiParam {String} [deviceId] 设备id
 * @apiParam {String} [code] 疫苗编号
 * @apiParam {String} [name] 疫苗名称
 * @apiParam {String} [unitName] 单位名称
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} begin 开始时间
 * @apiParam {String} end 结束时间
 * @apiParam {String} size 每页显示数目
 * @apiParam {String} page 第几页
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.get('/queryInouts',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.queryInouts(ctx.request.query);
    })
);

/**
 * @api {GET} /inout/queryInoutsByUnitCode  查询各单位冷藏柜、接种柜的历史出入库信息
 * @apiGroup inout
 * @apiVersion 1.0.0
 * @apiDescription 查询各单位冷藏柜、接种柜的历史出入库信息
 * @apiParam {String} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiParam {String} begin 开始时间
 * @apiParam {String} end 结束时间
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.get('/queryInoutsByUnitCode',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.queryInoutsByUnitCode(ctx.request.query);
    })
);

module.exports = router;