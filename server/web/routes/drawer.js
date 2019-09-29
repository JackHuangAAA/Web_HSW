/**
 * Created by Administrator on 2019/9/27 0027.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('drawer');

/**
 * @api {POST} /drawer/saveDrawer  增加抽屉信息
 * @apiGroup drawer
 * @apiVersion 1.0.0
 * @apiDescription 增加抽屉信息
 * @apiParam {JSON}  Object  drawer model
 * @apiSuccess {JSON}  Object  drawer model
 */
router.post('/saveDrawer',
    Libs.router( async (ctx, next) => {
        return await Domain.services.drawer.saveDrawer(ctx.request.body);
    })
);

module.exports = router;