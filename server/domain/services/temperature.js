/**
 * Created by Administrator on 2019/9/27 0027.
 */
const moment = require('moment');
const logger = Libs.logger.getLogger('temperature');

module.exports = {

    /**
     * 查询温度信息
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryTemperatures: async function(requestBody){
        logger.debug(`queryTemperatures param: ${JSON.stringify(requestBody)}`);
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
        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.temperature.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size),
            lean:true
        });
        return {rs: result.docs, total: result.total};
    },

    /**
     * 保存温度信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveTemperatures: async function(requestBody){
        logger.debug(`saveTemperatures param: ${JSON.stringify(requestBody)}`);
        return Domain.models.temperature.create(requestBody);
    }

};