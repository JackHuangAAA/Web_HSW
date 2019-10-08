/**
 * Created by Administrator on 2019/9/27 0027.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('inout');

/**
 * @api {GET} /inout/queryInouts  查询出入库信息
 * @apiGroup inout
 * @apiVersion 1.0.0
 * @apiDescription 查询出入库信息
 * @apiParam {String} [deviceId] 设备id
 * @apiParam {String} [code] 疫苗编号
 * @apiParam {String} [name] 疫苗名称
 * @apiParam {String} begin 开始时间
 * @apiParam {String} end 结束时间
 * @apiSuccess {JSON}  Object  inout model 数组
 */
router.get('/queryInouts',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.queryInouts(ctx.request.query);
    })
);

module.exports = router;