/**
 * Created by Huangshaowei on 2019/10/18 0041.
 */
'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('vaccination');

/**
 * @api {GET} /vaccination/queryVaccine查询疫苗信息
 * @apiGroup vaccine
 * @apiVersion 1.0.0
 * @apiDescription 查询疫苗信息
 *  @apiParam {Number} size 每次显示条数
 *  @apiParam {Number} page 显示第几页
 * @apiParam {String} [deviceId] 设备ID
 * @apiParam {String} [name] 疫苗名称
 * @apiSuccess {JSON}  Object  vaccine model 数组
 */
router.get('/queryVaccine',
    Libs.router( async (ctx, next) => {
        return await Domain.services.vaccine.queryVaccine(ctx.request.query);
    })
);

module.exports = router;