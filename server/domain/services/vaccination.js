/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('vaccination');
const moment = require('moment');

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
        query.push({ "createDate": { '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() } });
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({ "deviceType": requestBody.deviceType });
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({ "unitCode": requestBody.unitCode });
        }

        let result =  await Domain.models.vaccination.aggregate([{"$match": {"$and": query}},
            {"$group":{_id: '$code',  num_coustomer: { $sum: 1 }}},
        ]);

        logger.debug(`result: ${result}`);
        return { rs: result, total: result.length }
    },


};