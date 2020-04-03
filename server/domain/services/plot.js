/**
 * Created by Administrator on 2019/9/27 0027.
 */

const moment = require('moment');
const logger = Libs.logger.getLogger('plot');

module.exports = {

    /**
     * 查询小区分页
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryPlotByPaging: async function(requestBody){
        logger.debug(`queryPlotByPaging param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.plate)) {
            query.push({"plate": new RegExp(requestBody.plate)});
        }
        if (!_.isEmpty(requestBody.years)) {
            query.push({"years": new RegExp(requestBody.years)});
        }
        if (!_.isEmpty(requestBody.unit_min) && !_.isEmpty(requestBody.unit_max) ) {
            query.push({"unit": { '$gte': requestBody.unit_min, '$lte': requestBody.unit_max }});
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.plot.paginate(query, {
            sort: {"createDate": -1},
            page: requestBody.page||1,
            limit: parseInt(requestBody.size)||10
        });
        return await {rs: result.docs, total: result.total};
    },
    /**
     * 查询小区
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryPlot: async function(requestBody){
        logger.debug(`queryPlot param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.name)) {
            query.push({"name": new RegExp(requestBody.name)});
        }
        if (!_.isEmpty(requestBody.plate)) {
            query.push({"plate": new RegExp(requestBody.plate)});
        }
        if (!_.isEmpty(requestBody.years)) {
            query.push({"years": new RegExp(requestBody.years)});
        }
        if (!_.isEmpty(requestBody.unit_min) && !_.isEmpty(requestBody.unit_max) ) {
            query.push({"unit": { '$gte': requestBody.unit_min, '$lte': requestBody.unit_max }});
        }

        query = query.length > 0 ? { "$and": query } : {};
        return await Domain.models.house.find(query).sort({"createDate": -1});
    },

    /**
     * 保存小区
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    savePlot: async function(requestBody){
        logger.debug(`savePlot param: ${JSON.stringify(requestBody)}`);
        return Domain.models.plot.create(requestBody);
    },

    /**
     * 删除房源
     * @param requestBody
     * @returns {Promise.<*>}
     */
    deletePlot: async function (requestBody) {
        logger.debug(`deletePlot param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.plot.remove({_id:requestBody.id});
    },

    /**
     * 修改房源
     * @param requestBody
     * @returns {Promise.<Query>}
     */
    modifyPlot: async function(requestBody){
        logger.debug("modifyPlot:" + JSON.stringify(requestBody));
        return await Domain.models.plot.updateOne({'_id': requestBody.id},
            {
                $set: {
                    ...requestBody
                }
            });
    },
};