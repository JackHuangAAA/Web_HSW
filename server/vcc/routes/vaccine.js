'use strict';
const router = require('koa-router')();
const logger = Libs.logger.getLogger('vaccine');
const mongoose = require('mongoose');


/**
 * @api {GET} /vaccine/queryVaccine  查询疫苗种类 出库完成
 * @apiGroup Vaccine
 * @apiVersion 1.0.0
 * @apiDescription 查询疫苗种类
 * @apiParam {String} device 设备id
 * @apiSuccess {Array}  rs  查询疫苗种类数组
 * @apiSuccess {Number}  total 查询疫苗种类总数
 */
router.get('/queryVaccine',
    Libs.router(async (ctx, next) => {
        return await Domain.services.vaccine.queryVaccine(ctx.request.query);
    })
);

/**
 * @api {GET} /vaccine/queryVaccineLowerThreshold  查询疫苗剩余数量小于报警阈值的疫苗信息
 * @apiGroup Vaccine
 * @apiVersion 1.0.0
 * @apiDescription 查询疫苗剩余数量小于报警阈值的疫苗信息
 * @apiParam {String} device 设备id
 * @apiSuccess {Array}  rs  疫苗剩余数量小于报警阈值的疫苗信息数组
 * @apiSuccess {Number}  total 查询疫苗剩余数量小于报警阈值的疫苗信息总数
 */
router.get('/queryVaccineLowerThreshold',
    Libs.router(async (ctx, next) => {
        return await Domain.services.vaccine.queryVaccineLowerThreshold(ctx.request.query);
    })
);

/**
 * @api {GET} /vaccine/queryVaccineStorageNum  按设备id查询，疫苗code分组合计疫苗数量
 * @apiGroup Vaccine
 * @apiVersion 1.0.0
 * @apiDescription 按设备id查询，疫苗code分组合计疫苗数量
 * @apiParam {String} device 设备id
 * @apiParam {String} deviceType 设备类型
 * @apiParam {String} unitName 所属单位
 * @apiParam {String} unitCode 所属单位编号
 * @apiParam {Number} type 类型(1:入库;2:出库)
 * @apiSuccess {Array}  rs  查询疫苗种类数组
 * @apiSuccess {Number}  total 查询疫苗种类总数
 */
router.get('/queryVaccineStorageNum',
    Libs.router(async (ctx, next) => {
        let result = await Domain.services.vaccine.queryVaccineStorageNum(ctx.request.query);
        /*let _result = result.rs.map(item => {
            delete item._id;
            item.device = mongoose.Types.ObjectId(result.device.device)
            item.deviceType = result.device.deviceType
            item.unitCode = result.device.unitCode
            item.unitName = result.device.unitName
            return item;
        })
      // 查询某设备下所有抽屉信息
        let drawerData = await Domain.services.drawer.queryDrawerByVaccineArr({ device: result.device.device });
        let _drawerData = drawerData.rs.map(ele => {
             delete ele._id;
            ele.type = ctx.request.query.type
            ele.user =  ctx.currentUser._id
            // ele.user = "5d89b7af3b11e1a1733ef870"
            return ele;
        })
        // 出入库记录
        await Domain.services.inout.insertManyInout(_drawerData);
        // 汇总出库
        if (ctx.request.query.type == 2) {
            await Domain.services.summary.saveSummary(_result);
            // 清零疫苗数量
        }*/
        return result;
    })
);


/**
 * @api {POST} /vaccine/clearVacineTotal  出库时清零疫苗数量
 * @apiGroup Vaccine
 * @apiVersion 1.0.0
 * @apiDescription 出库时清零疫苗数量
 * @apiParam {String} device 设备id
 * @apiSuccess {OBject}  json  操作返回数据
 */
router.post('/clearVaccineTotal',
    Libs.router(async (ctx, next) => {
        return await Domain.services.vaccine.clearVaccineTotal(ctx.request.body);
    })
);

/**
 * @api {POST} /vaccine/modifyVaccine  更新抽屉内疫苗信息
 * @apiGroup Vaccine
 * @apiVersion 1.0.0
 * @apiDescription 更新抽屉内疫苗信息
 * @apiParam {Array} ids 疫苗ids
 * @apiParam {Array} totals 入库后抽屉内疫苗数组
 * @apiSuccess {OBject}  json  操作返回数据
 */
router.post('/modifyVaccine',
    Libs.router(async (ctx, next) => {
        return await Domain.services.vaccine.modifyVaccine(ctx.request.body);
    })
);

/**
 * @api {POST} /vaccine/modifyVaccineNum  更新抽屉内疫苗数量信息
 * @apiGroup Vaccine
 * @apiVersion 1.0.0
 * @apiDescription 更新抽屉内疫苗数量信息
 * @apiParam {Array} ids 疫苗ids
 * @apiParam {Array} totals 入库后抽屉内疫苗数量数组
 * @apiSuccess {OBject}  json  操作返回数据
 */
router.post('/modifyVaccineNum',
    Libs.router(async (ctx, next) => {
        return await Domain.services.vaccine.modifyVaccineNum(ctx.request.body);
    })
);

module.exports = router;
