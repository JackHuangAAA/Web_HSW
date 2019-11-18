const logger = Libs.logger.getLogger('customer');
const moment = require('moment');

module.exports={
    /**
     * 增加用户信息
     * @param requestBody
     * @returns {requestBody}
     */
    saveCustomer: async function (requestBody) {
        logger.debug('saveCustomer :' + JSON.stringify(requestBody));
        return await Domain.models.customer.create(requestBody);
    },

    /**
     * 删除用户信息
     * @param requestBody
     * @returns {requestBody}
     */
    removeCustomerById: async function (requestBody) {
        logger.debug('removeCustomerById :' + JSON.stringify(requestBody));
        return await Domain.models.customer.findOneAndRemove({_id:requestBody.id});
    },

    /**
     * 更新用户信息
     * @param requestBody
     * @returns {requestBody}
     */
    modifyCustomer: async function (requestBody) {
        logger.debug('modifyCustomer :' + JSON.stringify(requestBody));
        let user=await this.queryCustomer({_id:requestBody.id});
        console.log(user)
        return await Domain.models.customer.updateOne({_id:requestBody.id},
            {
                $set:{
                    ...requestBody,
                    previou:{//上一次的疫苗计划
                        plan:user.rs[0].next.plan,
                        date:user.rs[0].next.date
                    },
                    next:{//下一次接种计划
                        plan:requestBody.nexid,
                        date:requestBody.date
                    },
                }
            },{new:true}
        );
    },

    /**
     * 查询用户信息
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
                limit:requestBody.size||10
            }
        );
        return {rs:result.docs,total:result.total};
    },

    /**
     * 根据条件查询用户信息
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