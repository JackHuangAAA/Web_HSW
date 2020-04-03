/**
 * Created by Administrator on 2019/9/27 0027.
 */

const moment = require('moment');
const logger = Libs.logger.getLogger('houseprice');

module.exports = {

    /**
     * 查询小区价格
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryPlotPriceByPaging: async function(requestBody){
        logger.debug(`queryPlotPriceByPaging param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.userId)) {
            query.push({"user": requestBody.userId});
        }
        if (!_.isEmpty(requestBody.plotId)) {
            query.push({"plot": requestBody.plotId});
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.plotprice.paginate(query, {
            sort: {"createDate": -1},
            page: requestBody.page||1,
            limit: parseInt(requestBody.size)||10
        });
        return await {rs: result.docs, total: result.total};
    },
    /**
     * 查询小区价格
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryPlotPrice: async function(requestBody){
        logger.debug(`queryPlotPrice param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.userId)) {
            query.push({"user": new RegExp(requestBody.userId)});
        }
        if (!_.isEmpty(requestBody.plotId)) {
            query.push({"plot": new RegExp(requestBody.plotId)});
        }

        query = query.length > 0 ? { "$and": query } : {};
        return await Domain.models.plotprice.find(query).sort({"createDate": -1});
    },

    /**
     * 保存小区价格
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    savePlotPrice: async function(requestBody){
        logger.debug(`savePlotPrice param: ${JSON.stringify(requestBody)}`);
        return Domain.models.plotprice.create(requestBody);
    },

    /**
     * 删除小区价格
     * @param requestBody
     * @returns {Promise.<*>}
     */
    deletePlotPrice: async function (requestBody) {
        logger.debug(`deletePlotPrice param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.plotprice.remove({_id:requestBody.id});
    },

    /**
     * 修改小区价格
     * @param requestBody
     * @returns {Promise.<Query>}
     */
    modifyPlotPrice: async function(requestBody){
        logger.debug("modifyPlotPrice:" + JSON.stringify(requestBody));
        return await Domain.models.plotprice.updateOne({'_id': requestBody.id},
            {
                $set: {
                    ...requestBody
                }
            });
    },
};