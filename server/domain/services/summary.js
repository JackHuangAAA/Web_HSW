/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('alarm');
const moment = require('moment');

module.exports = {
    /**
     * 保存汇总信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveSummary: async function(requestBody){
        logger.debug(`saveSummary param: ${JSON.stringify(requestBody)}`);
        return Domain.models.summary.insertMany(requestBody);
    },
    /**
     * 查询汇总信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    querySummary: async function(requestBody){
        logger.debug(`querySummary param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.device)) {
            query.push({"device": requestBody.device});
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({"deviceType": requestBody.deviceType});
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({"unitCode": requestBody.unitCode});
        }
        if (!_.isEmpty(requestBody.unitName)) {
            query.push({"unitName":  new RegExp(requestBody.unitName)});
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": requestBody.code});
        }
        if (!_.isEmpty(requestBody.begin)) {
            let begin = moment(requestBody.begin);
            begin = begin.startOf('day').toDate();
            query.push({"createDate": {"$gte": begin}});
        }
        if (!_.isEmpty(requestBody.end)) {
            let end = moment(requestBody.end);
            end = end.endOf('day').toDate();
            query.push({"createDate": {"$lte": end}});
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.summary.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page ||1,
            limit: parseInt(requestBody.size)||10,
            lean:true
        });
        return {rs: result.docs, total: result.total};
    },

};