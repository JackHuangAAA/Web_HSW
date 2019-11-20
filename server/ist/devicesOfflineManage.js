/**
 * Created by huangshaowei on 2019/11/12.
 */
const logger = Libs.logger.getLogger('summariesManage');
//设备心跳信息筛选掉线设备（设备更新时间超过3分钟）
let later = require('later');
let moment = require('moment');
later.date.localTime();//设置本地时区
async function updateDeviceStatus() {
    //综合服务终端设备查询
    let result = await Domain.models.device.find({type:3});
    //let timestamp_dev = await Domain.redis.client.getAsync("5da19b346baebc8f36de1877_heartbeat");

    for(let index in result){
        let deviceId = result[index]._id.toString();
        let deviceId_h = deviceId+'_heartbeat';
        let timestamp_dev = await Domain.redis.client.getAsync(deviceId_h);
        let status=new Number;
        if(timestamp_dev!=null){
            let time_now = new Date();

            if((time_now-timestamp_dev)<(1000*60*3)){
                status = 0;
            }else{
                status = 1;
            };

            await Domain.models.device.updateOne(
                { _id: deviceId },
                {$set:{ status:status }}
            )
        }
    }
};
later.setInterval(updateDeviceStatus,later.parse.cron('*/3 * * * *'));