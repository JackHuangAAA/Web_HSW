/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('vaccine');
var mongoose = require('mongoose');


module.exports = {

    /**
     * 
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryVaccine: async function (requestBody) {
        logger.debug(`queryVaccine param: ${JSON.stringify(requestBody)}`);
        let deviceId = mongoose.Types.ObjectId(requestBody.device);
        let result = await Domain.models.vaccine.aggregate([
            { $match: { device: deviceId } },
            { $group: { _id: '$code', num_movie: { $sum: 1 }, name: { $first: "$name" } } },
            { $project: { _id: 1, name: 1, num_movie: 1 } }

        ])
        logger.debug(`result: ${result}`);
        return { rs: result, total: result.length }
    },

    /**
     * 
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryVaccineLowerThreshold: async function (requestBody) {
        logger.debug(`queryVaccineLowerThreshold param: ${JSON.stringify(requestBody)}`);
        let query = [{ surplus: { $lt: 10 } }]
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        query = query.length == 2 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.vaccine.find(query)
        return { rs: result, total: result.length }
    },

    /**
     *  按设备id查询，疫苗code分组合计疫苗数量
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryVaccineStorageNum: async function (requestBody) {
        logger.debug(`queryVaccineStorageNum param: ${JSON.stringify(requestBody)}`);
        let deviceId = mongoose.Types.ObjectId(requestBody.device);
        let result = await Domain.models.vaccine.aggregate([
            { $match: { device: deviceId } },
            { $group: { _id: '$code', num_movie: { $sum: 1 }, name: { "$first": "$name" }, code: { "$first": "$code" }, total: { "$sum": "$total" }, surplus: { "$sum": "$surplus" }, updateDate: { "$first": "$updateDate" } } },

            { $project: { _id: 1, num_movie: 1, name: 1, code: 1, total: 1, surplus: 1, updateDate: 1, use: { "$subtract": ["$total", "$surplus"] } } }

        ])
        logger.debug(`result: ${result}`);
        return { rs: result, total: result.length, device: requestBody }
    },
    /**
     *  出库后 数量、 剩余数量清零
     * 
     * @param {any} requestBody 
     * @returns 
     */
    clearVaacineTotal: async function (requestBody) {
        logger.debug(`clearVaacineTotal param: ${JSON.stringify(requestBody)}`);
        await Domain.models.vaccine.update(
            { device:requestBody.device },
            {
                $set: { total: 0,  surplus: 0 }
            },
            {multi:true}
        );
    },

    /**
     *  更新抽屉内疫苗数量信息
     * 
     * @param {any} requestBody 
     * @returns 
     */
    modifyVaccine: async function (requestBody) {
        logger.debug(`modifyVaccine param: ${JSON.stringify(requestBody)}`);
        await Domain.models.vaccine.update(
            { _id: requestBody.ids[0] },
            {
                $set: { total: requestBody.totals[0] }
            }
        );
        await Domain.models.vaccine.update(
            { _id: requestBody.ids[1] },
            {
                $set: { total: requestBody.totals[1] }
            }
        );
    },



};
