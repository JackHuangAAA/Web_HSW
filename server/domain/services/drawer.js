/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('drawer');

module.exports = {

    /**
     * 
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
            return item.vaccine.length > 0
        })
        return { rs: result, total: result.length }
    },


};