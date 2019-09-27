/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('drawer');

module.exports = {

    /**
     *  查询抽屉里疫苗为空的数据
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryDrawerEmpty: async function (requestBody) {
        let query = [];
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        query = query.length == 2 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.drawer.find(query).populate("vaccine");
        console.log(result, 'result')
        result = result.filter(item => {
            return item.vaccine == null
        })
        return { rs: result, total: result.length }
    },
    /**
     * 
     * 根据抽屉id更新抽屉信息
     * @param {any} requestBody 
     * @returns 
     */
    modifyDrawerById: async function (requestBody) {
        logger.debug(`modifyDrawerById param: ${JSON.stringify(requestBody)}`);
        return Domain.models.drawer.update(
            { _id: requestBody.id },
            {
                $set: { vaccine: requestBody.vaccine }
            }
        );
    },

    /**
     *   根据条件查询抽屉信息，并按坐标排序
     * 
     * @param {any} requestBody 
     * @returns 
     */
    queryDrawerByCondition: async function (requestBody) {
        let query = [];
        if (!_.isEmpty(requestBody.device)) {
            query.push({ "device": requestBody.device });
        }
        if (!_.isEmpty(requestBody.id)) {
            query.push({ "device": requestBody.id });
        }
        query = query.length == 2 ? { "$and": query } : query.length == 1 ? query[0] : {};
        let result = await Domain.models.drawer.find(query).sort({"x":1, "y": 1}).populate("vaccine");

        return { rs: result, total: result.length }
    },



};