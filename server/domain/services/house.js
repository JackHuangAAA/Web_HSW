/**
 * Created by Administrator on 2019/9/27 0027.
 */

const moment = require('moment');
const logger = Libs.logger.getLogger('house');

module.exports = {

    /**
     * 查询房源,分页
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryHouseByPaging: async function(requestBody){
        logger.debug(`queryHouseByPaging param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.plot)) {
            query.push({"plot": new RegExp(requestBody.plot)});
        }
        if (!_.isEmpty(requestBody.plate)) {
            query.push({"plate": new RegExp(requestBody.plate)});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": new RegExp(requestBody.type)});
        }
        if (!_.isEmpty(requestBody.trade)) {
            query.push({"trade": new RegExp(requestBody.trade)});
        }
        if (!_.isEmpty(requestBody.decoration)) {
            query.push({"decoration": new RegExp(requestBody.decoration)});
        }
        if (!_.isEmpty(requestBody.area_min) && !_.isEmpty(requestBody.area_max) ) {
            query.push({"area": { '$gte': requestBody.area_min, '$lte': requestBody.area_max }});
        }
        if (!_.isEmpty(requestBody.unit_min) && !_.isEmpty(requestBody.unit_max) ) {
            query.push({"unit": { '$gte': requestBody.unit_min, '$lte': requestBody.unit_max }});
        }
        if (!_.isEmpty(requestBody.total_min) && !_.isEmpty(requestBody.total_max) ) {
            query.push({"total": { '$gte': requestBody.total_min, '$lte': requestBody.total_max }});
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.house.paginate(query, {
            sort: {"createDate": -1},
            page: requestBody.page||1,
            limit: parseInt(requestBody.size)||10
        });
        return await {rs: result.docs, total: result.total};
    },
    /**
     * 查询房源
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryHouse: async function(requestBody){
        logger.debug(`queryHouse param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.plot)) {
            query.push({"plot": new RegExp(requestBody.plot)});
        }
        if (!_.isEmpty(requestBody.plate)) {
            query.push({"plate": new RegExp(requestBody.plate)});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": new RegExp(requestBody.type)});
        }
        if (!_.isEmpty(requestBody.trade)) {
            query.push({"trade": new RegExp(requestBody.trade)});
        }
        if (!_.isEmpty(requestBody.decoration)) {
            query.push({"decoration": new RegExp(requestBody.decoration)});
        }
        if (!_.isEmpty(requestBody.area_min) && !_.isEmpty(requestBody.area_max) ) {
            query.push({"area": { '$gte': requestBody.area_min, '$lte': requestBody.area_max }});
        }
        if (!_.isEmpty(requestBody.unit_min) && !_.isEmpty(requestBody.unit_max) ) {
            query.push({"unit": { '$gte': requestBody.unit_min, '$lte': requestBody.unit_max }});
        }
        if (!_.isEmpty(requestBody.total_min) && !_.isEmpty(requestBody.total_max) ) {
            query.push({"total": { '$gte': requestBody.total_min, '$lte': requestBody.total_max }});
        }

        query = query.length > 0 ? { "$and": query } : {};
        return await Domain.models.house.find(query).sort({"createDate": -1});
    },

    /**
     * 保存房源
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveHouse: async function(requestBody){
        logger.debug(`saveHouse param: ${JSON.stringify(requestBody)}`);
        return Domain.models.house.create(requestBody);
    },

    /**
     * 删除房源
     * @param requestBody
     * @returns {Promise.<*>}
     */
    deleteHouse: async function (requestBody) {
        logger.debug(`deleteHouse param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.house.remove({_id:requestBody.id});
    },

    /**
     * 修改房源
     * @param requestBody
     * @returns {Promise.<Query>}
     */
    modifyHouse: async function(requestBody){
        logger.debug("modifyHouse:" + JSON.stringify(requestBody));
        return await Domain.models.house.updateOne({'_id': requestBody.id},
            {
                $set: {
                    ...requestBody
                }
            });
    },
};