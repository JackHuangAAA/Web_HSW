/**
 * Created by Administrator on 2019/9/27 0027.
 */
const moment = require('moment');
const logger = Libs.logger.getLogger('temperature');

module.exports = {

    /**
     * 查询温度信息
     * @param requestBody
     * @returns {Promise.<{rs: *, total: (*|number)}>}
     */
    queryTemperatures: async function(requestBody){
        logger.debug(`queryTemperatures param: ${JSON.stringify(requestBody)}`);
        let query = [];
        if (!_.isEmpty(requestBody.deviceId)) {
            query.push({"deviceId": requestBody.deviceId});
        }
        if (!_.isEmpty(requestBody.deviceType)) {
            query.push({"deviceType": requestBody.deviceType});
        }
        if (!_.isEmpty(requestBody.unitCode)) {
            query.push({"unitCode": requestBody.unitCode});
        }
        if (!_.isEmpty(requestBody.unitName)) {
            query.push({"unitName":  new RegExp(requestBody.unitName)});
        }
        if (!_.isEmpty(requestBody.begin)) {
            let begin = moment(requestBody.begin);
            begin = begin.startOf('day').toDate();
            query.push({"createDate": {"$gte": begin}});
        }
        if (!_.isEmpty(requestBody.end)) {
            let end = moment(requestBody.end);
            end = end.endOf('day').toDate();
            query.push({"createDate": {"$lte": end}});
        }
        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.temperature.paginate(query, {
            sort: {"_id": -1},
            populate:[{path:'device',select:'code alias'}],
            page: requestBody.page,
            limit: parseInt(requestBody.size),
            lean:true
        });
        return {rs: result.docs, total: result.total};
    },

    /**
     * 保存温度信息
     * @param requestBody
     * @param temperature  温度值
     * @param device  设备id
     * @param deviceType  设备类型
     * @param unitCode  单位编号
     * @param unitName  单位名称
     * @returns {Promise.<requestBody>}
     */
    saveTemperatures: async function(requestBody){
        logger.debug(`saveTemperatures param: ${JSON.stringify(requestBody)}`);
        await Domain.models.device.updateOne({'_id': requestBody.device},
            {
                $set: {
                    temperature:requestBody.temperature,
                    updateDate: new Date()
                }
            });

        let query = [{"device":requestBody.device}];
        let today = moment();
        let dailyInfo={ '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() };
        query.push({ "createDate": dailyInfo });
        query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.temperature.find(query);

        //判断温度是否在安全区间，不是则插入alarm

        if(requestBody.temperature>Domain.enum.TEMPERATURE_REGION.MAX){
            await Domain.models.alarm.create({
                device:requestBody.device,
                deviceType:requestBody.deviceType,
                unitCode:requestBody.unitCode,
                unitName:requestBody.unitName,
                type:1,
                reason:'温度过高',
                createDate:new Date()
            });
        }else if(requestBody.temperature<Domain.enum.TEMPERATURE_REGION.MIN){
            await Domain.models.alarm.create({
                device:requestBody.device,
                deviceType:requestBody.deviceType,
                unitCode:requestBody.unitCode,
                unitName:requestBody.unitName,
                type:1,
                reason:'温度过低',
                createDate:new Date()
            });
        };

        //若存在当天的时间记录，则将温度信息插入已有记录的温度数组

        if(!_.isEmpty(result)){
            return await Domain.models.temperature.updateOne(
                query,
                {
                    $push:{
                        "degree":{
                            time: new Date(),
                            value: requestBody.temperature
                        }
                    }
                });
        }else{
            return await Domain.models.temperature.create({
                device: requestBody.device,
                deviceType: requestBody.deviceType,
                unitCode: requestBody.unitCode,
                unitName: requestBody.unitName,
                degree: [{
                time: new Date(),
                value: requestBody.temperature
            }],
                createDate: new Date()})
        }
    }
};