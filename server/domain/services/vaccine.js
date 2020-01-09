/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('vaccine');
var mongoose = require('mongoose');


module.exports = {

    /**
     * 查询设备疫苗类型信息
     * @param {any} requestBody
     * @returns
     */
    queryVaccineNum: async function (requestBody) {
        logger.debug(`queryVaccineNum param: ${JSON.stringify(requestBody)}`);
        let deviceId = mongoose.Types.ObjectId(requestBody.device);
        let result = await Domain.models.vaccine.aggregate([
            { $match: { device: deviceId } },
            { $group: { _id: '$code', num_movie: { $sum: 1 }, name: { $first: "$name" } } },
            { $project: { _id: 1, name: 1, num_movie: 1 } }

        ]);
        logger.debug(`result: ${result}`);
        return { rs: result, total: result.length }
    },

    /**
     *
     * @param {any} requestBody
     * @returns
     */
    queryVaccineLowerThreshold: async function (requestBody) {
        logger.debug(`queryVaccineLowerThreshold param: ${JSON.stringify(requestBody)}`);
        let query = [{ surplus: { $lt: 10 } }];
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.vaccine.find(query)
        return { rs: result, total: result.length }
    },

    /**
     *  按设备id查询，疫苗code分组合计疫苗数量
     * @param {any} requestBody
     * @returns
     */
    queryVaccineStorageNum: async function (requestBody) {
        logger.debug(`queryVaccineStorageNum param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.device)) {
            let deviceId = mongoose.Types.ObjectId(requestBody.device);
            query.push({device: deviceId});
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({ "code": requestBody.code });
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({ "name": requestBody.name });
        }
        if (!_.isEmpty(requestBody.batchNo)) {
            query.push({ "batchNo": requestBody.batchNo });
        }
        if (!_.isEmpty(requestBody.product)) {
            query.push({ "product": new RegExp(requestBody.product)});
        }
        if (!_.isEmpty(requestBody['ids']) || !_.isEmpty(requestBody['ids[]'])) {
            let ids = requestBody.ids.split(',');
            let temp = _.map(ids, item => {
                return mongoose.Types.ObjectId(item);
            });
            query.push({ "_id": { $in: temp } });
        }
        if (requestBody.surplusltTen) {
            query.push({ "surplus": {'$lte':10}});
        }
        let page = requestBody.page?requestBody.page:1;
        let size = requestBody.size?requestBody.size:10;


        let result = await Domain.models.vaccine.aggregate([
            { $match: { "$and": query } },
            { $group: { _id: '$code', num_movie: { $sum: 1 }, name: { "$first": "$name" }, code: { "$first": "$code" }, total: { "$sum": "$total" }, surplus: { "$sum": "$surplus" }, product: { "$first": "$product" },updateDate: { "$first": "$updateDate" } } },
            { $project: { _id: 1, num_movie: 1, name: 1, code: 1, total: 1, surplus: 1, product:1, updateDate: 1, use: { "$subtract": ["$total", "$surplus"] } } },
            {$skip:(page-1)*size},
            {$limit:parseInt(size)}
        ]);
        if (requestBody.queryNum) {
            return { total: result.length }
        }else {
            return { rs: result, total: result.length, device: requestBody }
        }

    },
    /**
     *  出库后 数量、 剩余数量清零
     * @param {any} requestBody
     * @returns
     */
    clearVaccineTotal: async function (requestBody) {
        logger.debug(`clearVaccineTotal param: ${JSON.stringify(requestBody)}`);
        await Domain.models.vaccine.update(
            { _id: { $in: requestBody['ids']}},
            {
                $set: { total: 0,  surplus: 0 }
            },
            {multi:true}
        );
    },

    /**
     *  更新抽屉内疫苗信息
     * @param {any} requestBody
     * @returns
     */
    modifyVaccine: async function (requestBody) {
        logger.debug(`modifyVaccine param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.vaccine.update(
            { _id: requestBody.id },
            {
                $set: { ...requestBody }
            }
        );
    },

    /**
     *  更新抽屉内疫苗数量信息
     * @param {any} requestBody
     * @returns
     */
    modifyVaccineNum: async function (requestBody) {
        logger.debug(`modifyVaccineNum param: ${JSON.stringify(requestBody)}`);
        let result = await Domain.models.vaccine.findOne({ _id: requestBody.id },null,{lean:true});
        requestBody.total = parseInt(result.total) + parseInt(requestBody.total);
        requestBody.surplus = parseInt(result.surplus) + parseInt(requestBody.surplus);
        return await Domain.models.vaccine.update(
            { _id: requestBody.id },
            {
                $set: { ...requestBody }
            }
        );
    },

    /**
     * 按指定条件查询疫苗信息
     * @param requestBody
     * @returns {Promise.<*>}
     */
    queryVaccineByCondition: async function(requestBody){
        logger.debug(`queryVaccineByCondition param: ${JSON.stringify(requestBody)}`);
        let query = [], sort=null;
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({ "code": requestBody.code });
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({ "name": requestBody.name });
        }
        if (!_.isEmpty(requestBody.batchNo)) {
            query.push({ "batchNo": requestBody.batchNo });
        }
        if (requestBody.surplusIsNotZero) {
            query.push({ "surplus": {'$gt':0}});
        }
        if (requestBody.surplusltTen) {
            query.push({ "surplus": {'$lte':10}});
        }
        if (requestBody.sortSurplus) {
            sort = {sort:{surplus: 1}};
        }
        query = query.length > 0 ? { "$and": query } : {};
        return await Domain.models.vaccine.find(query,null, sort);
    },

    /**
     * 查询疫苗信息
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryVaccine: async function(requestBody){
        logger.debug(`queryVaccine param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.deviceId)) {
            query.push({"device": requestBody.deviceId});
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": requestBody.name});
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.vaccine.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page||1,
            limit: parseInt(requestBody.size)||10
        });

        return {rs: result.docs, total: result.total};
    },

    /**
     * 删除疫苗信息
     * @param requestBody
     * @returns {Promise.<void>}
     */
    removeVaccineById: async function(requestBody) {
        logger.debug(`removeVaccineById param: ${JSON.stringify(requestBody)}`);
        return Domain.models.vaccine.remove({_id: requestBody.id});
    },

    /**
     * 批量入库疫苗信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveManyVaccine: async function(requestBody){
        logger.debug(`saveManyVaccine param: ${JSON.stringify(requestBody)}`);
        return Domain.models.vaccine.insertMany(requestBody);
    },


};
