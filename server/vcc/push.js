/**
 * Created by apple on 2019/9/24.
 */
'use strict';
const logger = Libs.logger.getLogger('homeData');
let later = require('later');
let moment = require('moment');
let path = require('path');
let fs = require('fs');
later.date.localTime();//设置本地时区
global._ = require('lodash');
global.Config = require(`${process.cwd()}/config`);
// code和socket实例一一对应
let map = {

}
let push = {
    init(io) {
        io.on('connection', (socket) => {
            logger.info('set socket.id: ' + socket.id)
            //接收客户端
            socket.on('register', function (msg) {
                let _msg = JSON.parse(msg)
                map[_msg.code] = socket;
                logger.info("receive register data: " + msg);
            });

            socket.emit("test", '收到了吗')
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
        //查询温度报警、当天服务人数、查询抽屉疫苗信息
        let deviceData = await Domain.services.device.queryDeviceByCondition({ code: key });
        //查询温度报警
        let alarmNumber = (await Domain.services.alarm.queryAlarmByByCondition({ device: deviceData[0]._id,type:1,ifToday:'today' })).length;
        //当天服务人数
        let customerNumber = (await Domain.services.vaccination.queryVaccinationDailyInfo({ device: deviceData[0]._id,ifToday:'today' })).length;
        // 查询抽屉疫苗信息
        let res = await Domain.services.drawer.queryDrawerByCondition({ device: deviceData[0]._id });
        let vaccineData=[];
        let array = res;
        for (let i = 0; i < 10; i++) {
            let num = array[i].vaccine.length, vaccine = array[i].vaccine, temp = {};
            if (num > 0) {
                for (let k = 0; k < num; k++) {
                    if (k == 0) {
                        temp.vaccineOneName = vaccine[k].name;
                        temp.vaccineOneCount = vaccine[k].surplus;
                    }
                    if (k == 1) {
                        temp.vaccineTwoName = vaccine[k].name;
                        temp.vaccineTwoCount = vaccine[k].surplus;
                    }
                }
            } else {
                temp.vaccineOneName = '';
                temp.vaccineOneCount = '';
                temp.vaccineTwoName = '';
                temp.vaccineTwoCount = '';
            }
            vaccineData.push(temp);
        }


         let timedData = {
             alarmNumber: alarmNumber,
             customerNumber: customerNumber,
             vaccineData: vaccineData,
         };
        value.emit( Domain.enum.TIMEDATA, timedData);
        value.emit( 'test2', '再一次');
    });
}

later.setInterval(execute, later.parse.cron('0/1 * * * * ?'));

//监听主题信息
Domain.redis.pub.on('message', (channel, message) => {
    logger.info('receive sub '+channel+' data; '+message);
    let result = JSON.parse(message);

    let value= map[result.code];

    if (value!=undefined){
        value.emit( channel, result);
    }

});

//订阅主题
Domain.redis.pub.subscribe('receiveVaccination');
Domain.redis.pub.subscribe('receiveVaccinationStatus');
