/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('alarm');
const moment = require('moment');

module.exports = {

    /**
     *
     *
     * @param {any} requestBody
     * @returns
     */
    queryAlarmByByCondition: async function (requestBody) {
        logger.debug(`queryAlarmByByCondition param: ${JSON.stringify(requestBody)}`);
        let today = moment();
        let query = [];
        query.push({ "createDate": { '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() } });
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        query = query.length >1 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.alarm.find(query);

        logger.debug(`result: ${result}`);
        return { rs: result, total: result.length }
    },

    /**
     * 查询报警信息
     * @param {any} requestBody
     * @returns
     */
    queryAlarm: async function (requestBody) {
        logger.debug(`queryAlarm param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({ "deviceType": requestBody.deviceType });
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({ "unitCode": requestBody.unitCode });
        }
        query = query.length >1 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.alarm.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size)
        });
        return {rs: result.docs, total: result.total};
    },

    /**
     * 保存报警信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveAlarm: async function(requestBody){
        logger.debug(`saveAlarm param: ${JSON.stringify(requestBody)}`);
        return Domain.models.alarm.create(requestBody);
    }

};