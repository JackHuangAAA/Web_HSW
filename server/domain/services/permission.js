const logger = Libs.logger.getLogger('permission');
const mongoose = require('mongoose');

module.exports = {

    /**
     * 查询权限信息树
     * @param requestBody
     * @returns {Promise.<Array>}
     */
    loadTree: async function (requestBody) {
        logger.debug('loadTree:' + JSON.stringify(requestBody));
        //type=0:运营商;type=1:企业
        let result = await Domain.models.menu.find({type: parseInt(requestBody.type)}, null, { sort: { 'sort': 1 }, lean: true });
        //增加根节点
        let root = { title: '根节点', pid: null, expand: true };
        root.children = result;
        return new Array(root);
    },

    /**
     * 查询权限信息
     * @param requestBody
     * @returns {Promise.<{rs: (*|Array), total}>}
     */
    queryPermission: async function (requestBody) {
        logger.debug('queryPermission:' + JSON.stringify(requestBody));
        let query = {};
        // if (!_.isEmpty(requestBody.name)) {
        //     query = { "title": new RegExp(requestBody.name) };
        // }
        let result = await Domain.models.permission.paginate(query, {
            sort: { "sort": -1 },
            page: requestBody.page || 1,
            limit: parseInt(requestBody.size) || 10,
            lean: true
        })
        return { rs: result.docs, total: result.total };
    },

    /**
     * 根据权限id查询权限信息
     * @param requestBody
     * @returns {Promise.<{rs: (*|Array), total}>}
     */
    queryPermissionById: async function (requestBody) {
        logger.debug('queryPermissionById:' + JSON.stringify(requestBody));
        return await Domain.models.permission.find({_id: requestBody.id})

    },

    /**
     * 增加权限信息
     * @param requestBody
     * @returns {*}
     */
    savePermission: async function (ctx, requestBody) {
        logger.debug('savePermission:' + JSON.stringify(requestBody));
        let newMenu =  await this.createPermissionTree(requestBody.menuData);
        let createMsg = await Domain.models.permission.create({children: newMenu});
        return await Domain.models.role.findOneAndUpdate({_id: requestBody.id},{
            $set: {
                permission: createMsg._id,
                updateDate: new Date()
            }
        })
    },

    /**
     * 修改权限信息
     * @param requestBody
     * @returns {Query}
     */
    modifyPermission: async function (requestBody) {
        logger.debug('modifyPermission:' + JSON.stringify(requestBody));
        let newMenu =  await this.createPermissionTree(requestBody.menuData);
        let findMsg = await Domain.models.role.findOne({_id: requestBody.id})
        return await Domain.models.permission.findOneAndUpdate({ _id: findMsg.permission }, {
            $set: {
                children: newMenu,
                updateDate: new Date()
            }
        }, { new: true });
    },

    /**
     * 根据id删除权限信息
     * @param requestBody
     * @returns {Query}
     */
    removePermissionById: async function (requestBody) {
        logger.debug('removePermissionById:' + JSON.stringify(requestBody));
        return await Domain.models.permission.findOneAndRemove({ _id: requestBody._id }); // _id ?
    },

    /**
     *
     * @param {Array} menuData 前端传来的菜单数组
     * @returns newMenu 删选出的新菜单
     */
    createPermissionTree: async function (menuData) {
        let pid_array=[],cid_array=[];
        menuData.forEach(item => {
            if (!item.children || item.children.length == 0) {
                cid_array.push(item._id);//保存接收数据中的子节点id
                if(pid_array.indexOf(item.pid)<0){
                    pid_array.push(item.pid);//不重复保存接收数据中的父节点id
                }
            }
        });
        let allMenu = await Domain.models.menu.find({}, null);
        let newMenu = [];//接收数据中父节点的Map
        allMenu.forEach(item => {
            let children_array=[];
            if(pid_array.indexOf(item._id.toString()) > -1){
                item.children.forEach(c_item=>{
                    if(cid_array.indexOf(c_item._id.toString()) > -1){
                        children_array.push(c_item)
                    }
                })
                item.children=children_array;
                newMenu.push(item);
            }
        });
        return newMenu;
    }
}