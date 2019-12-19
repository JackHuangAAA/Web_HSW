/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('alarm');
const moment = require('moment');
const mongoose = require('mongoose');

module.exports = {

    /**
     *
     *
     * @param {any} requestBody
     * @returns
     */
    queryAlarmByCondition: async function (requestBody) {
        logger.debug(`queryAlarmByCondition param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.ifToday)) {
           let today = moment();
           query.push({ "createDate": { '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() } });
        }
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({ "deviceType": requestBody.deviceType });
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({ "unitCode": requestBody.unitCode });
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.alarm.find(query);
        return {rs: result, total: result.length};
    },

    /**
     * 查询报警信息
     * @param {any} requestBody
     * @returns
     */
    queryAlarm: async function (requestBody) {
        logger.debug(`queryAlarm param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.ifToday)) {
            let today = moment();
            query.push({ "createDate": { '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() } });
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({ "deviceType": requestBody.deviceType });
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({ "unitCode": requestBody.unitCode });
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.alarm.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page || 1,
            limit: parseInt(requestBody.size)||10
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
    },

    /**
     * 查询报警信息数量
     * @param {any} requestBody
     * @returns
     */
    queryAlarmNum: async function (requestBody) {
        logger.debug(`queryAlarm param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.ifToday)) {
            let today = moment();
            query.push({ "createDate": { '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() } });
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({ "deviceType": requestBody.deviceType });
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({ "unitCode": requestBody.unitCode });
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.alarm.find(query);
        return {total: result.length};
    },
};