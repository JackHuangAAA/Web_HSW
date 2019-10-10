/**
 * Created by Administrator on 2019/9/24.
 */
const logger = Libs.logger.getLogger('alarm');
const moment = require('moment');

module.exports = {
     /**
     * 保存汇总信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveSummary: async function(requestBody){
        logger.debug(`saveSummary param: ${JSON.stringify(requestBody)}`);
        return Domain.models.summary.insertMany(requestBody);
    },

};