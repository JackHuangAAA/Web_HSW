/**
 * Created by Administrator on 2019/10/07 0041.
 */
const moment = require('moment');
const mongoose = require('mongoose');
const logger = Libs.logger.getLogger('vaccination');

module.exports = {

    /**
     *
     *
     * @param {any} requestBody
     * @returns
     */
    queryVaccinationByCustomerCode: async function (requestBody) {
        logger.debug(`queryVaccinationByCustomerCode param: ${JSON.stringify(requestBody)}`);
        let query = [];
        let today = moment();
        query.push({"createDate": {'$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate()}});
        if (!_.isEmpty(requestBody.device)) {
            query.push({"device": requestBody.device});
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({"deviceType": requestBody.deviceType});
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({"unitCode": requestBody.unitCode});
        }

        let result = await Domain.models.vaccination.aggregate([{"$match": {"$and": query}},
            {"$group": {_id: '$code', num_coustomer: {$sum: 1}}},
        ]);

        logger.debug(`result: ${result}`);
        return {rs: result, total: result.length};
    },

    /**
     * 查询接种记录
     * @param requestBody
     * @returns
     */
    queryVaccination: async function(requestBody){
        logger.debug(`queryVaccination param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.sort)) {
            query.push({"sort": requestBody.sort});
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": requestBody.code});
        }
        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.vaccination.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size)
        });
        return await {rs: result.docs, total: result.total};
    },

    /**
     * 保存接种记录
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveVaccination: async function(requestBody){
        logger.debug(`saveVaccination param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.vaccination.create(requestBody);
    },

	 /**
     * 按条件查询接种记录
     * 按接种序号分组统计客户数量aggregate
     * @param requestBody
     * @returns
     */
    queryVaccinationByCondition: async function(requestBody){
        logger.debug(`queryVaccinationByCondition param: ${JSON.stringify(requestBody)}`);
         let query = [];
         if (!_.isEmpty(requestBody.user)) {
             query.push({ "user": requestBody.user });
         }
         if (!_.isEmpty(requestBody.device)) {
             query.push({ "device": requestBody.device });
         }
         if (!_.isEmpty(requestBody.deviceType)) {
             query.push({ "deviceType": requestBody.deviceType });
         }
         if (!_.isEmpty(requestBody.unitCode)) {
             query.push({ "unitCode": requestBody.unitCode });
         }
         if (!_.isEmpty(requestBody.unitName)) {
             query.push({ "unitName": requestBody.unitName });
         }
         query = query.length >1 ? { "$and": query } : query.length == 1 ? query[0] : {};
         return await Domain.models.vaccination.find(query);
    },

    /**
     * 查询当日该设备接种人
     * @param requestBody
     * @returns
     */
    queryVaccinationDailyInfo: async function(requestBody){
        logger.debug(`queryVaccinationByCondition param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.deviceid)) {
            query.push({ "device": mongoose.Types.ObjectId(requestBody.deviceid) });
        }
        let today = moment();
        let dailyInfo={ '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() };
        if (!_.isEmpty(requestBody.today)) {
            query.push({ "createDate": dailyInfo });
        }
        query = query.length >1 ? { "$and": query } : query.length == 1 ? query[0] : {};
        return await Domain.models.vaccination.aggregate([
                {
                    $match:query
                },
                {
                    $group:{
                        _id:"$customer.code",
                        "customer":{"$first":"$customer"}
                    }
                }
            ]);
    }
};