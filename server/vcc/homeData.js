/**
 * Created by apple on 2019/9/24.
 */
const logger = Libs.logger.getLogger('homeData');
let later = require('later');
let moment = require('moment');
let path = require('path');
let fs = require('fs');
later.date.localTime();//设置本地时区
global._ = require('lodash');
global.Config = require(`${process.cwd()}/config`);
// deviceId和socket实例一一对应
let map = {

} 
   logger.info('==================================== ')
let push = {
    init(io) {
        io.on('connection', (socket) => {
            logger.info('set socket.id: ' + socket.id)
            //接收客户端
            socket.on('register', function (msg) {
                let _msg = JSON.parse(msg)
                map[_msg.deviceId] = socket;
                logger.info("receive register data: " + msg);
            });
        });
        //断开连接
        io.on('disconnect', function (reason) {
            logger.info("websocket服务断开:" + reason);
        });
    }
};

const http = require('http').Server();
const io = require('socket.io')(http);
push.init(io);
http.listen(9996 || 8080, '0.0.0.0');

async function execute() {
    _.mapKeys(map, async function (value, key) {
         logger.info(key, '===============deviceid=========')
        // 报警信息 （温度报警次数、报警次数、报警信息）

        let deviceData = await Domain.services.device.queryDeviceByCondition({ code: key });
        let alarmData = await Domain.services.alarm.queryAlarmByByCondition({ device: deviceData._id });
        // 疫苗种类
        let vaccineNum = await Domain.services.vaccine.queryVaccine({ device: deviceData._id });
        
        // 缺少库存
         let vaccineLowerThreshold = await Domain.services.vaccine.queryVaccineLowerThreshold({ device: deviceData._id });
        // 空余药柜
         let drawerEmptyArr = await Domain.services.drawer.queryDrawerEmpty({ device: deviceData._id });
         
        // 接种顾客
         let customerNum = await Domain.services.vaccination.queryVaccinationByCustomerCode({ device: deviceData._id });
         let timedData = {
             alarmData: alarmData,
             vaccineNum: vaccineNum,
             vaccineLowerThreshold: vaccineLowerThreshold,
             drawerEmptyArr: drawerEmptyArr,
             customerNum: customerNum,
         }
        value.emit( Domain.enum.TIMEDATA, timedData);
    });



}

later.setInterval(execute, later.parse.cron('10 * * * * ?'));



