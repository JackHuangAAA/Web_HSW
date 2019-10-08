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
    queryAlarmDailyInfo: async function (requestBody) {
        let today = moment();
        let query = [];
        query.push({ "createDate": { '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() } });
        let result = await Domain.models.alarm.find({ "$and": query });
        

        logger.debug(`result: ${result}`);
        return { rs: result, total: result.length }
    },

    /**
     * 
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryAlarmByByCondition: async function (requestBody) {
        logger.debug(`queryAlarmByTemp param: ${JSON.stringify(requestBody)}`);
        let today = moment();
        let query = [];
        query.push({ "createDate": { '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() } });
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        query = query.length == 2 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.alarm.find(query);
        logger.debug(`result: ${result}`);
        return { rs: result, total: result.length }
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

};