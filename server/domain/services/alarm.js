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
        query = query.length >1 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.alarm.find(query);
        return result;
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
    },

    /**
     * 查询今日报警信息
     * @param requestBody
     * @returns
     */
    queryAlarmDailyInfo: async function(requestBody){
        logger.debug(`queryAlarmDailyInfo param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": mongoose.Types.ObjectId(requestBody.device) });
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({ "deviceType": requestBody.deviceType });
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({ "unitCode": requestBody.unitCode });
        }
        if (!_.isEmpty(requestBody.unitName)) {
            query.push({"unitName": {"$regex" : requestBody.unitName, "$options" : "$i"}});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        if (!_.isEmpty(requestBody.ifToday)) {
            let today = moment();
            let dailyInfo={ '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() };
            query.push({ "createDate": dailyInfo });
        }
        query = query.length >1 ? { "$and": query } : query.length == 1 ? query[0] : {};

        return await Domain.models.alarm.find(query);
    }


};