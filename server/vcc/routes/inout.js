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
router.post('/saveInoutMany',
    Libs.router( async (ctx, next) => {
        return await Domain.services.inout.saveInoutMany(ctx.request.body);
    })
);

module.exports = router;