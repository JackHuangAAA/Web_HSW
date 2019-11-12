/**
 * Created by Administrator on 2019/11/12.
 */
const logger = Libs.logger.getLogger('plan');
const moment = require('moment');
const mongoose = require('mongoose');

module.exports = {



    /**
     * 查询接种计划
     * @param {any} requestBody
     * @returns
     */
    queryPlan: async function (requestBody) {
        logger.debug(`queryPlan param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.sort)) {
            query.push({ "sort": requestBody.sort });
        }
        if (!_.isEmpty(requestBody.vaccine)) {
            query.push({"vaccine":  new RegExp(requestBody.vaccine)});
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.plan.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page || 1,
            limit: parseInt(requestBody.size)||10
        });
        return {rs: result.docs};
    },

    /**
     * 保存接种计划
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    savePlan: async function(requestBody){
        logger.debug(`savePlan param: ${JSON.stringify(requestBody)}`);
        return Domain.models.plan.create(requestBody);
    },

    /**
     * 删除接种计划
     * @param requestBody
     * @returns {Promise.<*>}
     */
    removePlan: async function (requestBody) {
        logger.debug('removePlan：' + JSON.stringify(requestBody));
        return await Domain.models.plan.findOneAndRemove({ _id: requestBody.id });
    },

    /**
     * 修改接种计划
     * @param requestBody
     * @returns {Promise.<Query>}
     */
    modifyPlan: async function (requestBody) {
        logger.debug('modifyPlan：' + JSON.stringify(requestBody));
        return await Domain.models.plan.updateOne({ '_id': requestBody.id },
            {
                $set: requestBody

            });
    },

};