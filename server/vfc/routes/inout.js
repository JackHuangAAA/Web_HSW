/**
 * Created by Administrator on 2019/9/27 0027.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('inout');

/**
 * @api {POST} /inout/saveInout  增加出入库信息
 * @apiGroup inout
 * @apiVersion 1.0.0
 * @apiDescription 增加出入库信息
 * @apiParam {JSON}  Object  inout model
 * @apiSuccess {JSON}  Object  inout model
 */
router.post('/saveInout',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.saveInout(ctx.request.body);
    })
);

/**
 * 批量增加出入库
 * @param requestBody
 * @returns {Promise.<requestBody>}
 */
router.post('/saveManyInout',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.insertManyInout(ctx.request.body);
    })
);

/**
 * 查询出入库记录
 * @param requestBody
 * @returns {Promise.<requestBody>}
 */
router.get('/queryInoutByCondition',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.queryInoutByCondition(ctx.request.query);
    })
);
/**
 * @api {GET} /inout/queryInouts  查询出入库信息
 * @apiGroup inout
 * @apiVersion 1.0.0
 * @apiDescription 查询出入库信息
 * @apiParam {String} [deviceId] 设备id
 *  @apiParam {Number} [type] 出入库类别2出库，1入库
 * @apiParam {String} [code] 疫苗编号
 * @apiParam {String} [batchId] 操作批次号
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

/**
 * @api {GET} /inout/queryInouts  查询出入库信息
 * @apiGroup inout
 * @apiVersion 1.0.0
 * @apiDescription 查询出入库信息
 * @apiParam {String} [deviceId] 设备id
 *  @apiParam {Number} [type] 出入库类别2出库，1入库
 * @apiParam {String} [batchId] 操作批次号
 * @apiParam {String} [name] 疫苗名称
 * @apiParam {String} [unitName] 单位名称
 * @apiParam {String} begin 开始时间
 * @apiParam {String} end 结束时间
 * @apiParam {String} size 每页显示数目
 * @apiParam {String} page 第几页
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.get('/queryInoutsBybatchId',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.queryInoutsBybatchId(ctx.request.query);
    })
);


module.exports = router;