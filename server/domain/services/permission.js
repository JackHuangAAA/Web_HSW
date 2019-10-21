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
        logger.debug('createPermissionTree:' + JSON.stringify(menuData));
        const allMenu = await Domain.models.menu.find({}, null);
        let allMenuMap = new Map();
        allMenu.forEach(item => {
            allMenuMap.set(item._id.toString(), item)
        });
        let newMenu = [];
        let itemParent = {};
        let allParentArray = []
        // 获取所有可能出现的菜单父类
        menuData.forEach(item => {
            if (!item.children || item.children.length == 0) {
                if (allMenuMap.get(item.pid)) {
                    itemParent = allMenuMap.get(item.pid);
                    itemParent.children.push(item);
                    allParentArray.push(itemParent)
                }
            }
            else {
                newMenu.push(item)
            }
        });
        // 删除重复出现的父菜单
        let _allParentArray = [];
        var temp = []; //一个新的临时数组
        allParentArray.forEach((item, index) => {
            if (temp.indexOf(allParentArray[index]['_id'].toString()) == -1) {
                temp.push(allParentArray[index]['_id'].toString());
                _allParentArray.push(allParentArray[index])
            }
        });
        // 筛选出需要删减子菜单的父菜单（勾选了父菜单下的某几个子菜单）
        menuData.forEach(ele => {
            if (allMenuMap.get(ele._id)) {
                _allParentArray.forEach((ele2, index2) => {
                    if (ele._id == ele2._id) {
                        _allParentArray.splice(index2, 1)
                    }
                })
            }
        });
        // 从所有子菜单中筛选出勾选的子菜单
        _allParentArray.forEach(ele => {
            let _ele = JSON.parse(JSON.stringify(ele));
            _ele.children = [];
            this.filterArray(ele.children, _ele.children );
            newMenu.push(_ele);
        });
        return newMenu;
    },

    /**
     *
     * @param {any} allArray 含有重复字段的数组
     * @param {any} _allArray  将重复字段取出来 组成新的数组
     */
    filterArray: function (allArray, _allArray) {
        logger.debug('filterArray:' + JSON.stringify(allArray));
        var temp = []; //一个新的临时数组
        allArray.forEach((item, index) => {
            if (temp.indexOf(allArray[index]['_id'].toString()) == -1) {
                temp.push(allArray[index]['_id'].toString())
            } else {
                _allArray.push(allArray[index])
            }
        })
    }

};