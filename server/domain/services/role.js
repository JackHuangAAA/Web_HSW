
const logger = Libs.logger.getLogger('role');

module.exports = {

    /**
     * 查询角色信息
     * @param requestBody
     * @returns {Promise.<{rs: (*|Array), total}>}
     */
    queryRole: async function (requestBody) {
        logger.debug('queryRole:' + JSON.stringify(requestBody));
        let query = [];
        if (!_.isEmpty(requestBody.name)) {
            query.push({ "name": new RegExp(requestBody.name) });
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        query = query.length > 0 ? { "$and": query } : {};
        let result = await Domain.models.role.paginate(query, {
            populate: 'permission',
            sort: { "_id": -1 },
            page: requestBody.page || 1,
            limit: parseInt(requestBody.size) || 10,
            lean: true
        });
        return { rs: result.docs, total: result.total };
    },

    /**
     * 增加角色信息
     * @param requestBody
     * @returns {requestBody}
     */
    saveRole: function (requestBody) {
        logger.debug('saveRole:' + JSON.stringify(requestBody));
        return Domain.models.role.create(requestBody);
    },

    /**
     * 修改角色信息
     * @param requestBody
     * @returns {Query}
     */
    modifyRole: function (requestBody) {
        logger.debug('modifyRole:' + JSON.stringify(requestBody));
        return Domain.models.role.updateOne({ _id: requestBody.id }, {
            $set: {
                ...requestBody,
                updateDate: new Date()
            }
        });
    },
    /**
     * 根据id删除角色信息
     * @param requestBody
     * @returns {Query}
     */
    removeRoleById: async function (requestBody) {
        logger.debug('removeRoleById:' + JSON.stringify(requestBody));
        let result = await Domain.models.role.findOneAndRemove({ _id: requestBody.id });
        return await Domain.models.permission.findOneAndRemove({ _id: result.permission });
    },

    /**
     * 根据类型查询角色信息
     * @param requestBody
     * @returns {Query}
     */
    queryRoleByType: async function (currentUser, requestBody) {
        logger.debug('queryRoleByType:' + JSON.stringify(requestBody));
        let query = [];
        if(requestBody.type != null && requestBody.type != '') {
            query.push({type: requestBody.type});
        }else{
            query.push({type: currentUser.type});
        }
        if(!_.isEmpty(requestBody.name)) {
            query.push({name: new RegExp(requestBody.name)});
        }
        query = query.length > 0 ? {"$and": query} : {};
        return await Domain.models.role.find(query);

    },

    /**
     * 删除角色的permission字段
     * @param requestBody
     * @returns {Query}
     */
    removePermissionByRoleId: async function (requestBody) {
        logger.debug('removePermissionByRoleId:' + JSON.stringify(requestBody));
        let result  = await Domain.models.role.findOneAndUpdate({ _id: requestBody.id }, {
            $unset: {
                permission: '',
            }
        });
        return await Domain.models.permission.findOneAndRemove({ _id: result.permission }); // _id ?
    },

    /**
     *根据指定条件查询角色
     * @param requestBody
     * @returns {Query}
     */
    queryRoleByCondition: async function (requestBody) {
        logger.debug('queryRoleByCondition:' + JSON.stringify(requestBody));
        let query = [];
        if (!_.isEmpty(requestBody.name)) {
            query.push({ "name": requestBody.name });
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({ "type": requestBody.type });
        }
        if (!_.isEmpty(requestBody.permission)) {
            query.push({ "permission": requestBody.permission });
        }
        query = query.length > 0 ? { "$and": query } : {};
        return await Domain.models.role.find(query);
    }

};



