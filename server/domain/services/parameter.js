const logger = Libs.logger.getLogger('parameter');

module.exports = {

    /**
	 * 查询参数信息
     * @param requestBody
     * @returns {Promise.<Query|*|T|{}>}
     */
	queryParameter:async function(requestBody){
		logger.debug(`queryParameter params: ${JSON.stringify(requestBody)}`);
		let query=[];
		if(!_.isEmpty(requestBody.name)){
			query.push({name:new RegExp(requestBody.name)});
		};
		if(!_.isEmpty(requestBody.key)){
			query.push({key:new RegExp(requestBody.key)});
		};
		query=query.length>0?{"$and":query}:{};
		let result=await Domain.models.parameter.paginate(query,{
			sort:{'_id':-1},
			page:requestBody.page||1,
			limit:requestBody.size||10,
		});
		return { rs: result.docs, total: result.total };
	},

    /**
	 * 增加参数信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
	saveParameter: async function (requestBody) {
		logger.debug(`saveParameter: ${JSON.stringify(requestBody)}`);
		await Domain.services.cache.setCacheToString(requestBody.key, requestBody.value);
		return await Domain.models.parameter.create(requestBody);
	},
    /**
	 * 修改参数信息
     * @param requestBody
     * @returns {Promise.<Query>}
     */
	modifyParameter:async function(requestBody){
		logger.debug(`modifyParameter params: ${JSON.stringify(requestBody)}`);
		await Domain.services.cache.setCacheToString(requestBody.key, requestBody.value);
		return await Domain.models.parameter.updateOne({'_id':requestBody.id},{
			$set:{
				...requestBody,
				updateDate:new Date()
			}
		});
	},
	/**
	* 根据id删除参数信息
	* @param requestBody
	* @returns {Promise.<*>}
	*/
	removeParameterById:async function(requestBody){
		logger.debug(`removeParameterById params:${JSON.stringify(requestBody)}`);
		await Domain.services.cache.del(requestBody.key);
		return await Domain.models.parameter.findOneAndRemove({ '_id': requestBody.id });
	},
	/**
	* 根据id停用启用参数信息
	* @param requestBody
	* @returns {Promise.<*>}
	*/
	displayParameter: async function (requestBody) {
		logger.debug('displayParameter' + JSON.stringify(requestBody));
		if(requestBody.isActive){
            await Domain.services.cache.setCacheToString(requestBody.key, requestBody.value);
		}else{
            await Domain.services.cache.del(requestBody.key);
		}

		return await Domain.models.parameter.updateOne({ '_id': requestBody.id },
			{
				$set: {
					isActive: requestBody.isActive,
					updateDate: new Date()
				}
			});
	},

    /**
	 * 按指定条件查询参数信息
     * @param requestBody
     * @returns {Promise.<*|Query|T>}
     */
    queryParameterByCondition: async function( requestBody) {
        logger.debug('queryParameterByCondition:' + JSON.stringify(requestBody));
        let query = [];
        if(!_.isEmpty(requestBody.key)) {
            query.push({key: requestBody.key});
        }
        if(!_.isEmpty(requestBody.value)) {
            query.push({value: requestBody.value});
        }
        if(requestBody.isActive) {
            query.push({isActive: requestBody.isActive});
        }
        query = query.length > 0 ? {"$and": query} : {};
        return await Domain.models.parameter.find(query);
    }

	

}