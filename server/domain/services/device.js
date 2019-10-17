/**
 * Created by Administrator on 2019/9/27 0027.
 */
const moment = require('moment');
const logger = Libs.logger.getLogger('device');
const mongoose = require('mongoose');
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
        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
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
     * 修改设备信息
     * @param requestBody
     * @returns {Promise.<Query>}
     */
    modifyDevice: async function(requestBody){
        logger.debug("modifyDevice:" + JSON.stringify(requestBody));
        return await Domain.models.device.updateOne({'_id': requestBody.id},
            {
                $set: {
                    ...requestBody,
                    updateDate: new Date()
                }
            });
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
        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        return await Domain.models.device.find(query);
    },

    /**
     * 聚合查询，各单位单天各设备类型不同状态的设备数量统计(所属单位编号待定)
     * @param requestBody
     * @returns {JSON}  Object  version model数组，不同类型的数量统计
     */
    queryDeviceByAggregate: async function(requestBody){
        logger.debug(`queryDeviceByAggregate param: ${JSON.stringify(requestBody)}`);
        let today = moment();
        let query = [];
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({"unitCode": requestBody.unitCode});
        }
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": parseInt(requestBody.type)});
        }
        //0查询总体设备状态统计；1查询各单位设备状态统计
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": parseInt(requestBody.type)});
        }
        query = query.length >1 ? { "$and": query } : query.length == 1 ? query[0] : {};
        if(requestBody.flag=="1"){

            return await Domain.models.device.aggregate([{$group:{
                    _id:{
                        "type":"$type",
                        "status":"$status",
                        "unitCode":"$unitCode"
                    },
                    count:{$sum:1}
                }}]);

        }else{
            return await Domain.models.device.aggregate([{$match:query},{$group:{
                    _id:{
                        "status":"$status"
                    },
                    count:{$sum:1}
                }}]);
        }
    },

    /**
     * 疫苗柜库存查询
     * @param requestBody
     * @returns
     */
    queryDeviceStock: async function(requestBody){
        logger.debug(`queryDeviceStock param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.type)) {
            query.push({"type": requestBody.type});
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({"unitCode": requestBody.unitCode});
        }
        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        //查询疫苗不足的设备信息
        let result = await Domain.models.vaccine.find({surplus:0});
        let deviceId_array = [];
        for (let index in result){//生成疫苗不足的设备ID数组
            deviceId_array.push(result[index].device.toString());
        };

        let result_device = await Domain.models.device.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size)
        });

        let result_objarray=[];
        for(let i=0; i<result_device.docs.length;i++)
        {
            let newObj = {
                _id:result_device.docs[i]._id,
                code:result_device.docs[i].code,
                alias:result_device.docs[i].alias,
                type:result_device.docs[i].type,
                status:result_device.docs[i].address,
                cabinetNo:result_device.docs[i].cabinetNo,
                unitCode:result_device.docs[i].unitCode,
                unitName:result_device.docs[i].unitName,
                notes:result_device.docs[i].notes,
                createDate:result_device.docs[i].createDate,
                updateDate:result_device.docs[i].updateDate,
                flag:1 //1库存正常，0库存不足
            };
            if(deviceId_array.includes(result_device.docs[i]._id.toString())){
                newObj.flag=0;
            }
            result_objarray.push(newObj)
        };
        return {rs:result_objarray,total:result_device.total};

    },

    /**
     * 指定疫苗柜内疫苗库存查询
     * @param requestBody
     * @returns
     */
    queryDeviceByVaccineStock: async function(requestBody){
        logger.debug(`queryDeviceByVaccineStock param: ${JSON.stringify(requestBody)}`);
        let query={device:requestBody.deviceId};
        let result = await Domain.models.vaccine.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size)
        });
        return {rs: result.docs, total: result.total};
    },
};