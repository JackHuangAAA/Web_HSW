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
        //定时发送刷新指令

         let timedData = {
             type: "refresh"
         };
        value.emit( Domain.enum.TIMEDATA, timedData);
        //value.emit( 'test2', '再一次');
    });
}

later.setInterval(execute, later.parse.cron('0/1 * * * * ?'));
//setInterval(execute,6000);
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
Domain.redis.pub.subscribe('UpdateQueueStatus');
Domain.redis.pub.subscribe('NextVaccination');
Domain.redis.pub.subscribe('VaccinationCheck');
