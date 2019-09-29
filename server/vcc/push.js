/**
 * Created by Administrator on 2018/9/26 0026.
 */
if(process.argv[2] == 'version' || process.argv[2] == 'v'){
    console.log(require('../package').version);
    return;
}
global.Promise = require('bluebird');
global._ = require('lodash');
global.Config = require(`${process.cwd()}/config`);
global.Libs = require('../libs/index');
global.Domain = require('../domain/index');
let checkDevStatus = require('./checkDevStatus');
let dailyEndTask = require('./dailyEndTask');
Libs.logger.configure(Config.logger.admin);
const logger = Libs.logger.getLogger('push');

if(process.argv[0] == 'version' || process.argv[0] == 'v'){
    console.log(require('../package').version);
}

process.on('uncaughtException', function (err) {
    logger.error(err);
});

let sessions = {};

//map中
let map = {
   DEV_STATUS:[],//设备状态
   RECIPE_PROCESS:[],//处方执行进度
   RECIPE_ADD: [],//新增处方
};



let push = {
    init(io){
        io.on('connection', (socket) => {
            //预置socket到socket.io客户端接收目标
            map['DEV_STATUS'].push(socket);
            map['RECIPE_PROCESS'].push(socket);
            map['RECIPE_ADD'].push(socket);
            logger.info('set socket.id: '+socket.id)
            //接收客户端
            socket.on('clientData', function(msg) {
                logger.info("receive client data: "+msg);
            });
        });
        //断开连接
        io.on('disconnect', function(reason) {
            //Domain.Domain.redis.sub.unsubscribeAsync(channel);
            logger.info("websocket服务断开:"+reason);
        });
    }
};

const http = require('http').Server();
const io = require('socket.io')(http);
push.init(io);

//监听主题消息
Domain.redis.sub.on('message', (channel, message) => {
    logger.info('receive sub '+channel+' data; '+message);
    _.map(map[channel],s=>{
        logger.info('socket id '+s.id+' send data: '+message+' to channel ' +channel);
        s.emit(channel,message);
    })
});
//订阅主题
Domain.redis.sub.subscribe('DEV_STATUS');
Domain.redis.sub.subscribe('RECIPE_PROCESS');
Domain.redis.sub.subscribe('RECIPE_ADD');

http.listen(Config.push.port || 8080, Config.push.host || '0.0.0.0');

//初始化系统配置信息到缓存
Domain.services.cache.initParam();
//Domain.services.cache.initMedDic();
//Domain.services.cache.initEquivalent();