/**
 * Created by Administrator on 2019/9/27 0027.
 */

const moment = require('moment');
const logger = Libs.logger.getLogger('inout');

module.exports = {

    /**
     * 查询出入库信息
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryInouts: async function(requestBody){
        logger.debug(`queryInouts param: ${json.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.deviceId)) {
            query.push({"device": requestBody.deviceId});
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({"vaccineCode": requestBody.code});
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({"vaccineName": new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.begin)) {
            let begin = moment(requestBody.begin);
            begin = begin.startOf('day').toDate();
            query.push({createDate: {"$gte": begin}});
        }
        if (!_.isEmpty(requestBody.end)) {
            let end = moment(requestBody.end);
            end = end.endOf('day').toDate();
            query.push({createDate: {"$lte": end}});
        }
        query = query.length==2?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.inout.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size),
            lean:true
        });
        return {rs: result.docs, total: result.total};
    },

    /**
     * 保存出入库信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveInout: async function(requestBody){
        logger.debug(`saveInout param: ${json.stringify(requestBody)}`);
        return Domain.models.inout.create(requestBody);
    }

};