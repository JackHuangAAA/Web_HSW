/**
 * Created by Administrator on 2019/9/27 0027.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('device');

router.get('/', Libs.router(async (ctx, next) => {
    return ctx.currentDevice;
}));

module.exports = router;