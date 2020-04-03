/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('user');

/**
 * 查询我的收藏,分页
 */
router.get('/queryCelectionByPaging',
    Libs.router( async (ctx, next) => {
        return await Domain.services.celection.queryCelectionByPaging(
            ctx.request.query
        );
    })
);

/**
 * 查询我的收藏
 */
router.get('/queryCelection',
    Libs.router( async (ctx, next) => {
        return await Domain.services.celection.queryCelection(
            ctx.request.query
        );
    })
);

/**
 * 保存我的收藏
 */
router.post('/saveCelection',
    Libs.router( async (ctx, next) => {
        return await Domain.services.celection.saveCelection(ctx.request.body);
    })
);

/**
 * 删除我的收藏
 */
router.post('/deleteCelection',
    Libs.router( async (ctx, next) => {
        return await Domain.services.celection.deleteCelection(ctx.request.body);
    })
);

module.exports = router;