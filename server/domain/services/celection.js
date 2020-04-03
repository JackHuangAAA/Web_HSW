/**
 * Created by Administrator on 2019/9/27 0027.
 */

const moment = require('moment');
const logger = Libs.logger.getLogger('celection');

module.exports = {

    /**
     * 查询我的收藏,分页
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryCelectionByPaging: async function(requestBody){
        logger.debug(`queryCelectionByPaging param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.user)) {
            query.push({"user": requestBody.userId});
        }

        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.celection.paginate(query, {
            sort: {"createDate": -1},
            page: requestBody.page||1,
            limit: parseInt(requestBody.size)||10
        });
        return await {rs: result.docs, total: result.total};
    },
    /**
     * 查询我的收藏
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryCelection: async function(requestBody){
        logger.debug(`queryCelection param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.user)) {
            query.push({"user": requestBody.userId});
        }

        query = query.length > 0 ? { "$and": query } : {};
        return await Domain.models.celection.find(query).sort({"createDate": -1});
    },

    /**
     * 保存我的收藏
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveCelection: async function(requestBody){
        logger.debug(`saveCelection param: ${JSON.stringify(requestBody)}`);
        let celection = await Domain.models.celection.find({"user":requestBody.userId});
        if(celection.length>0){
            return await Domain.models.celection.updateOne(
                {"user":requestBody.userId},
                    {
                        $push:{
                            "house":requestBody.house
                        }
                    });
        }else{
            let house = requestBody.house;
            requestBody.house = new Array();
            requestBody.house.push(house);
            return await Domain.models.celection.create(requestBody);
        }
    },

    /**
     * 删除我的收藏
     * @param requestBody
     * @returns {Promise.<*>}
     */
    deleteCelection: async function (requestBody) {
        logger.debug(`deleteCelection param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.celection.updateOne(
            {"user":requestBody.userId},
            {
                $pull:{
                    "house":requestBody.house
                }
            });
    },



};