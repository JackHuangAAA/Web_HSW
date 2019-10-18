/**
 * Created by Administrator on 2019/10/12 0041.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('alarm');

/**
 * @api {POST} /summary/saveSummary  保存汇总信息
 * @apiGroup summary
 * @apiVersion 1.0.0
 * @apiDescription 保存报警信息
 * @apiParam {JSON}  Object  summary model
 * @apiSuccess {JSON}  Object  summary model 数组
 */
router.post('/saveSummary',
    Libs.router( async (ctx, next) => {
        return await Domain.services.summary.saveSummary(ctx.request.body);
    })
);

/**
 * @api {GET} /summary/querySummary  查询汇总信息
 * @apiGroup summary
 * @apiVersion 1.0.0
 * @apiDescription 查询汇总信息
 * @apiParam {String} [device] 设备ID
 * @apiParam {Number} [deviceType] 设备类型
 * @apiParam {String} [unitCode] 所属单位编号
 * @apiParam {String} [unitName] 所属单位名称
 * @apiParam {String} [code] 疫苗编号
 * @apiParam {String} begin 开始时间
 * @apiParam {String} end 结束时间
 * @apiParam {Number} page 当前页码
 * @apiParam {Number} size 每页数量
 * @apiSuccess {JSON}  Object  summary model 数组
 */
router.get('/querySummary',
    Libs.router( async (ctx, next) => {
        return await Domain.services.summary.querySummary(ctx.request.query);
    })
);

module.exports = router;