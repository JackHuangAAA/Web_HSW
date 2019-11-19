const logger = Libs.logger.getLogger('customer');
const moment = require('moment');

module.exports={
    /**
     * 增加客户信息
     * @param requestBody
     * @returns {requestBody}
     */
    saveCustomer: async function (requestBody) {
        logger.debug('saveCustomer :' + JSON.stringify(requestBody));
        return await Domain.models.customer.create(requestBody);
    },

    /**
     * 删除客户信息
     * @param requestBody
     * @returns {requestBody}
     */
    removeCustomerById: async function (requestBody) {
        logger.debug('removeCustomerById :' + JSON.stringify(requestBody));
        return await Domain.models.customer.findOneAndRemove({_id:requestBody.id});
    },

    /**
     * 更新客户信息
     * @param requestBody
     * @returns {requestBody}
     */
    modifyCustomer: async function (requestBody) {
        logger.debug('modifyCustomer :' + JSON.stringify(requestBody));
        return await Domain.models.customer.updateOne({_id:requestBody.id},
            {
                $set:{
                    ...requestBody
                }
            },{new:true}
        );
    },

    /**
     * 查询客户信息
     * @param requestBody
     * @returns {requestBody}
     */
    queryCustomer: async function (requestBody) {
        logger.debug('queryCustomer :' + JSON.stringify(requestBody));
        let query=[];
        if(!_.isEmpty(requestBody.id)){
            query.push({_id:requestBody.id});
        }
        query=query.length>0?{$and:query}:{};
        let result= await Domain.models.customer.paginate(query,
            {
                sort:{"_id":-1},
                page:requestBody.page||1,
                limit:parseInt(requestBody.size)||10
            }
        );
        return {rs:result.docs,total:result.total};
    },

    /**
     * 根据条件查询客户信息
     * @param requestBody
     * @returns {requestBody}
     */
    queryCustomerByCondition: async function (requestBody) {
        logger.debug('queryCustomerByCondition :' + JSON.stringify(requestBody));
        let query=[];
        if(!_.isEmpty(requestBody.id)){
            query.push({_id:requestBody.id});
        }
        query=query.length>0?{$and:query}:{};
        return await Domain.models.customer.find(query);
        
    },
}