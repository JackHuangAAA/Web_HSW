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

        // const allMenu = await Domain.models.menu.find({}, null);
        // let allMenuMap = new Map();
        // allMenu.forEach(item => {
        //     allMenuMap.set(item._id.toString(), item)
        // });
        // let newMenu = [];
        // let itemParent = {};
        // let allParentArray = []
        // // 获取所有可能出现的菜单父类
        // menuData.forEach(item => {
        //     if (!item.children || item.children.length == 0) {
        //         if (allMenuMap.get(item.pid)) {
        //             itemParent = allMenuMap.get(item.pid);
        //             itemParent.children.push(item);
        //             allParentArray.push(itemParent)
        //         }
        //     }
        //     else {
        //         newMenu.push(item)
        //     }
        // });
        // // 删除重复出现的父菜单
        // let _allParentArray = [];
        // var temp = []; //一个新的临时数组
        // allParentArray.forEach((item, index) => {
        //     if (temp.indexOf(allParentArray[index]['_id'].toString()) == -1) {
        //         temp.push(allParentArray[index]['_id'].toString());
        //         _allParentArray.push(allParentArray[index])
        //     }
        // });
        // // 筛选出需要删减子菜单的父菜单（勾选了父菜单下的某几个子菜单）
        // menuData.forEach(ele => {
        //     if (allMenuMap.get(ele._id)) {
        //         _allParentArray.forEach((ele2, index2) => {
        //             if (ele._id == ele2._id) {
        //                 _allParentArray.splice(index2, 1)
        //             }
        //         })
        //     }
        // });
        // // 从所有子菜单中筛选出勾选的子菜单
        // _allParentArray.forEach(ele => {
        //     let _ele = JSON.parse(JSON.stringify(ele));
        //     _ele.children = [];
        //     this.filterArray(ele.children, _ele.children );
        //     newMenu.push(_ele);
        // });
        // return newMenu;
        

        //================================
        // let newMenu=[];
        // let pid_arr=new Set();
        // let sub_arr=new Set();
        // //获取唯一父子节点id
        // menuData.forEach((item)=>{
        //     if(!item.children || item.children.length==0){
        //         sub_arr.add(item._id);
        //         if(!pid_arr.has(item.pid)){
        //             pid_arr.add(item.pid);
        //         }
        //     }
        // });
        // const allMenu = await Domain.models.menu.find({}, null);
        // allMenu.forEach((el)=>{
        //     let arrItem={};
        //     //判断是否当前父节点
        //     if(pid_arr.has(el._id.toString())){
        //         arrItem=el;
        //         let children=[];
        //         el.children.forEach((e)=>{
        //             if(sub_arr.has(e._id.toString())){
        //                 children.push(e);
        //             }
        //         });
        //         arrItem.children=children;
        //         newMenu.push(arrItem);
        //     }
        // });
        // console.log(newMenu);
        // return newMenu;

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
        console.log(newMenu);
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
