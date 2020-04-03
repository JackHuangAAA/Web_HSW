/**
 * Created by Administrator on 2019/9/27 0027.
 */

const moment = require('moment');
const logger = Libs.logger.getLogger('houseprice');

module.exports = {

    /**
     * 查询房源价格
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryHousePriceByPaging: async function(requestBody){
        logger.debug(`queryHousePriceByPaging param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.userId)) {
            query.push({"user": requestBody.userId});
        }
        if (!_.isEmpty(requestBody.houseId)) {
            query.push({"house": requestBody.houseId});
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.houseprice.paginate(query, {
            sort: {"createDate": -1},
            page: requestBody.page||1,
            limit: parseInt(requestBody.size)||10
        });
        return await {rs: result.docs, total: result.total};
    },
    /**
     * 查询房源价格
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryHousePrice: async function(requestBody){
        logger.debug(`queryHousePrice param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.userId)) {
            query.push({"user": new RegExp(requestBody.userId)});
        }
        if (!_.isEmpty(requestBody.houseId)) {
            query.push({"house": new RegExp(requestBody.houseId)});
        }

        query = query.length > 0 ? { "$and": query } : {};
        return await Domain.models.houseprice.find(query).sort({"createDate": -1});
    },

    /**
     * 保存房源价格
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveHousePrice: async function(requestBody){
        logger.debug(`saveHousePrice param: ${JSON.stringify(requestBody)}`);
        return Domain.models.houseprice.create(requestBody);
    },

    /**
     * 删除房源价格
     * @param requestBody
     * @returns {Promise.<*>}
     */
    deleteHousePrice: async function (requestBody) {
        logger.debug(`deleteHousePrice param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.houseprice.remove({_id:requestBody.id});
    },

    /**
     * 修改房源价格
     * @param requestBody
     * @returns {Promise.<Query>}
     */
    modifyHousePrice: async function(requestBody){
        logger.debug("modifyHousePrice:" + JSON.stringify(requestBody));
        return await Domain.models.house.updateOne({'_id': requestBody.id},
            {
                $set: {
                    ...requestBody
                }
            });
    },
};