/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('house');

/**
 * 查询房源分页
 */
router.get('/queryHouseByPaging',
    Libs.router( async (ctx, next) => {
        return await Domain.services.house.queryHouseByPaging(
            ctx.request.query
        );
    })
);

/**
 * 查询房源
 */
router.get('/queryHouse',
    Libs.router( async (ctx, next) => {
        return await Domain.services.house.queryHouse(
            ctx.request.query
        );
    })
);

/**
 * 保存房源
 */
router.post('/saveHouse',
    Libs.router( async (ctx, next) => {
        return await Domain.services.house.saveHouse(ctx.request.body);
    })
);

/**
 * 删除房源
 */
router.post('/deleteHouse',
    Libs.router( async (ctx, next) => {
        return await Domain.services.house.deleteHouse(ctx.request.body);
    })
);

/**
 * 修改房源
 */
router.post('/modifyHouse',
    Libs.router( async (ctx, next) => {
        return await Domain.services.house.modifyHouse(ctx.request.body);
    })
);

module.exports = router;