/**
 * Created by Administrator on 2019/11/12 0027.
 */
const moment = require('moment');
const logger = Libs.logger.getLogger('queue');
const mongoose = require('mongoose');
module.exports = {

    /**
     * 查询排队信息
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryQueue: async function(requestBody){
        logger.debug(`queryQueue param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": requestBody.code});
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name":  new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.sex)) {
            query.push({"sex": requestBody.sex});
        }
        if (!_.isEmpty(requestBody.age)) {
            query.push({"age": requestBody.age});
        }
        if (!_.isEmpty(requestBody.vaccineName)) {
            query.push({"vaccine.name":  new RegExp(requestBody.vaccineName)});
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.queue.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page || 1,
            limit: parseInt(requestBody.size) || 10
        });

        return {rs: result.docs};
    },

    /**
     * 保存排队信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveQueue: async function(requestBody){
        logger.debug(`saveQueue param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.queue.create(requestBody);
    },

    /**
     * 删除排队信息
     * @param requestBody
     * @returns {Promise.<*>}
     */
    removeQueue: async function (requestBody) {
        logger.debug('removeQueue：' + JSON.stringify(requestBody));
        return await Domain.models.queue.findOneAndRemove({ _id: requestBody.id });
    },

    /**
     * 修改排队信息
     * @param requestBody
     * @returns {Promise.<Query>}
     */
    modifyQueue: async function(requestBody){
        logger.debug("modifyQueue:" + JSON.stringify(requestBody));
        return await Domain.models.queue.updateOne({'_id': requestBody.id},
            {
                $set: {
                    ...requestBody,
                    createDate: new Date()
                }
            });
    },

    /**
     * 按条件查询排队信息
     * @param requestBody
     * @returns {Promise.<T|Query|*>}
     */
    queryQueueByCondition: async function(requestBody){
        logger.debug(`queryQueueByCondition param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": requestBody.code});
        }
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name":  new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.sex)) {
            query.push({"sex": requestBody.sex});
        }
        if (!_.isEmpty(requestBody.age)) {
            query.push({"age": requestBody.age});
        }
        if (!_.isEmpty(requestBody.status)) {
            query.push({"status": requestBody.status});
        }
        if (!_.isEmpty(requestBody.vaccineName)) {
            query.push({"vaccine.name":  new RegExp(requestBody.vaccineName)});
        }
        query = query.length > 0 ? { "$and": query } : {};
        return await Domain.models.queue.find(query).sort({'createDate':-1});
    }
};