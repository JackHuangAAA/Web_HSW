/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('log');
const moment = require('moment');

module.exports = {

    /**
     * 按条件查询操作日志
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryLogByCondition: async function (requestBody) {
        logger.debug(`queryLogByCondition param: ${JSON.stringify(requestBody)}`);
        let today = moment();
        let query = [];
        query.push({ "createDate": { '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() } });
        if (!_.isEmpty(requestBody.deviceCode)) {
            query.push({ "deviceCode": new requestBody.deviceCode });
        }
        if (!_.isEmpty(requestBody.content)) {
            query.push({ "content": new RegExp(requestBody.content) });
        }
        query = query.length >1 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.alarm.find(query);
        logger.debug(`result: ${result}`);
        return { rs: result, total: result.length }
    },

    /**
     * 保存日志信息
     * @param requestBody
     * @returns
     */
    saveLog: async function(requestBody){
        logger.debug(`saveLog param: ${JSON.stringify(requestBody)}`);
        return Domain.models.log.create(requestBody);
    },



};