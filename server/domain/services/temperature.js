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
        query = query.length==2?{"$and": query} : query.length==1 ? query[0] : {};
        let result = await Domain.models.temperature.paginate(query, {
            sort: {"_id": -1},
            page: requestBody.page,
            limit: parseInt(requestBody.size),
            lean:true
        });
        return {rs: result.docs, total: result.total};
    },

    /**
     * 保存温度信息
     * @param requestBody
     * @returns {Promise.<requestBody>}
     */
    saveTemperatures: async function(requestBody){
        logger.debug(`saveTemperatures param: ${JSON.stringify(requestBody)}`);
        await Domain.models.device.updateOne({'_id': requestBody.device},
            {
                $set: {
                    temperature:requestBody.degree[0].value,
                    updateDate: new Date()
                }
            });
<<<<<<< HEAD

        let query = {device:requestBody.device};
        let result = await Domain.models.temperature.find(query).sort({ createDate : -1 });
        let time_now = new Date();
        //判断温度是否在安全区间，不是则插入alarm
        let enum_js = require("../enum.js");
        if(requestBody.degree[0].value>enum_js.TEMPERATURE_REGION.MAX){
            await Domain.models.alarm.create({
                device:requestBody.device,
                deviceType:requestBody.deviceType,
                unitCode:requestBody.unitCode,
                unitName:requestBody.unitName,
                type:1,
                reason:'温度过高',
                createDate:requestBody.createDate
            });
        }else if(requestBody.degree[0].value<enum_js.TEMPERATURE_REGION.MIN){
            await Domain.models.alarm.create({
                device:requestBody.device,
                deviceType:requestBody.deviceType,
                unitCode:requestBody.unitCode,
                unitName:requestBody.unitName,
                type:1,
                reason:'温度过低',
                createDate:requestBody.createDate
            });
        };

        //若存在当天的时间记录，则将温度信息插入已有记录的温度数组

        time_now_end = time_now.setHours(23,59,59,999);
        time_now_start = time_now.setHours(0,0,0,0);

        if((result !=null)&&((result[0].createDate.toString().substr(0,10))==(time_now.toString().substr(0,10)))){
            return await Domain.models.temperature.updateOne(
                { device: requestBody.device,
                  createDate:{$gte:time_now_start,$lte:time_now_end}
                },
                {
                    $push:{
                        "degree":requestBody.degree
                    }
                }
            );
        }else{
            return await Domain.models.temperature.create(requestBody);
        }
=======
        return Domain.models.temperature.create(requestBody);
>>>>>>> develop
    }

};