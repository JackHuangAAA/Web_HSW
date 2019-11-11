const logger = Libs.logger.getLogger('permission');
const mongoose = require('mongoose');

module.exports = {

    /**
     * 查询权限信息
     * @param requestBody
     * @returns {Promise.<{rs: (*|Array), total}>}
     */
    queryPermission: async function (requestBody) {
        logger.debug('queryPermission:' + JSON.stringify(requestBody));
        let query = {};
        let result = await Domain.models.permission.paginate(query, {
            sort: { "sort": -1 },
            page: requestBody.page || 1,
            limit: parseInt(requestBody.size) || 10,
            lean: true
        })
        return { rs: result.docs, total: result.total };
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
        return await Domain.models.permission.findOneAndRemove({ _id: requestBody.id }); 
    },

    /**
     *
     * @param {Array} menuData 前端传来的菜单数组
     * @returns newMenu 删选出的新菜单
     */
    createPermissionTree: async function (menuData) {
        logger.debug('createPermissionTree:' + JSON.stringify(menuData));
        const allMenu = await Domain.models.menu.find({}, null);
        let allMenuMap = new Map();
        allMenu.forEach(item => {
            allMenuMap.set(item._id.toString(), item)
        });
        let newMenu = [];
        let temp=new Set();
        // 获取的菜单父类
        menuData.forEach(item => {
            if (!item.children) {
                if (allMenuMap.get(item.pid) && !temp.has(item.pid)) {
                    temp.add(item.pid);//去重
                    allsubMuneMap.set(item._id,item);
                    let obj=allMenuMap.get(item.pid);
                    obj.children=[];
                    newMenu.push(obj);//添加一级菜单
                }
            }
        });
        temp.clear();
        menuData.forEach(item=>{
            if(!item.children){
                newMenu.forEach(el=>{
                    if(item.pid==el._id){//添加二级菜单
                        el.children.push(item);
                    }
                });
            }
        });
        return newMenu;
    },

    /**
     * 根据指定条件查询权限信息
     * @param requestBody
     * @returns {Promise.<{rs: (*|Array), total}>}
     */
    queryPermissionByCondition: async function (requestBody) {
        logger.debug('queryPermissionById:' + JSON.stringify(requestBody));
        let query=[];
        if(!_.isEmpty(requestBody.id)){
            query.push({_id:requestBody.id});
        }
        query=query.length>0?{$and:query}:{};
        return await Domain.models.permission.find(query)

    },

};
