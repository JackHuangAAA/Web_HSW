/**
 * Created by Administrator on 2019/9/27 0027.
 */
const moment = require('moment');
const logger = Libs.logger.getLogger('device');

module.exports = {

    /**
     * 查询设备信息
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryDevices: async function(requestBody){
        logger.debug(`queryDevices param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.alias)) {
            query.push({"alias": requestBody.alias});
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": requestBody.code});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": requestBody.type});
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({"unitCode": requestBody.unitCode});
        }
        if (!_.isEmpty(requestBody.status)) {
            query.push({"status": requestBody.status});
        }
        if (!_.isEmpty(requestBody.cabinetNo)) {
            query.push({"cabinetNo": requestBody.cabinetNo});
        }
        query = query.length==2?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.device.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size)
        });

        return {rs: result.docs, total: result.total};
    },

    /**
     * 保存设备信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveDevice: async function(requestBody){
        logger.debug(`saveDevice param: ${JSON.stringify(requestBody)}`);
        return await Domain.models.device.create(requestBody);
    },

    /**
     * 按条件查询设备信息（状态/类型/单位）
     * @param requestBody
     * @returns {Promise.<T|Query|*>}
     */
    queryDeviceByCondition: async function(requestBody){
        logger.debug(`queryDeviceByCondition param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.alias)) {
            query.push({"alias": requestBody.alias});
        }
        if (!_.isEmpty(requestBody.code)) {
            query.push({"code": requestBody.code});
        }
        if (!_.isEmpty(requestBody.cabinetNo)) {
            query.push({"cabinetNo": requestBody.cabinetNo});
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({"unitCode": requestBody.unitCode});
        }
        if (!_.isEmpty(requestBody.status)) {
            query.push({"status": requestBody.status});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": requestBody.type});
        }
        query = query.length==2?{"$and": query} : query.length==1 ? query[0] : {};
        return await Domain.models.device.find(query);
    },

    /**
     * 聚合查询，各单位各设备类型不同状态的设备数量统计(所属单位编号待定)
     * @param requestBody
     * @returns {JSON}  Object  version model数组，不同类型的数量统计
     */
    queryDeviceByAggregate: async function(currentUser,requestBody){
        logger.debug(`queryDeviceByAggregate param: ${JSON.stringify(requestBody)}`);
        console.log(currentUser);
        /*
        if(currentUser.unitCode!=undefined){
            let $unitCode = currentUser.unitCode;
        }else{
            let $unitCode = '0000';
        };
        */
        let $unitCode = '0002';
        let $group={
            _id:{
                "type":"$type",
                "status":"$status"
            },
            count:{$sum:1}
        };

        let $match={
            unitCode:$unitCode
        };
        return await Domain.models.device.aggregate([{$match:$match},{$group:$group}]);
    }
};