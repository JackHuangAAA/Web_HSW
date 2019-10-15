/**
 * Created by Administrator on 2019/9/24.
 */
'use strict';
if(process.argv[2] == 'version' || process.argv[2] == 'v'){
    console.log(require('../package').version);
    return;
}
global.Promise = require('bluebird');
global._ = require('lodash');
global.Config = require(`${process.cwd()}/config`);
global.Libs = require('../libs');
global.Domain = require('../domain');
Libs.logger.configure(Config.logger.vcc);
const logger = Libs.logger.getLogger('main');
const cluster = require('cluster');
const numCPUs = require('os').cpus().length;

if(process.argv[0] == 'version' || process.argv[0] == 'v'){
    console.log(require('../package').version);
}

process.on('uncaughtException', function (err) {
    logger.error(err);
});

if (cluster.isMaster) {
    logger.info(`主进程[${process.pid}]启动...`);
    //日志清理
    require('./logsManage');
    require('./homeData');

    for (let i = 0; i < numCPUs; i++) {
        cluster.fork();
    }
    cluster.on('online', function (worker, address) {
        logger.info(`工作进程[${worker.process.pid}] 启动...`);
    });
    cluster.on('exit', function (worker, code, signal) {
        logger.info(`工作进程 [${worker.process.pid}] 重启`);
        setTimeout(function () {
            cluster.fork();
        }, 2000);
    });
} else {
    const Koa = require('koa');
    const app = new Koa();
    const koaBody = require('koa-body');
    const router = require('./router');
    // const homeData = require('./homeData');
    const log4js = require('log4js');
    const filter = require('./filter');
    const koaLogger = require('koa-logger');


    app.use(koaLogger((str, args) => {
            logger.info(...args)
    }));
    app.use(koaBody());

    let start = async ()=>{
        await filter.register(app);
        await router.register(app);

        app.use(async (ctx,next) => {
            let err = new Error('Not Found');
            ctx.body = Libs.response.error(err);
        });

        app.on('error',(err,ctx)=>{
            logger.error(err);
        });

        app.listen(Config.vcc.port || 8080, Config.vcc.host || '0.0.0.0');
    };


    start();

}

//设备心跳信息筛选掉线设备（设备更新时间超过3分钟）
let later = require('later');
let moment = require('moment');
later.date.localTime();//设置本地时区
async function updateDeviceStatus() {
    //接种柜设备查询
    let result = await Domain.models.device.find({type:1});
    let timestamp_dev = await Domain.redis.client.getAsync("5da19b346baebc8f36de1877_heartbeat");

    let time_now = Date.now();


    for(let index in result){
        let deviceId = result[index]._id.toString();
        let deviceId_h = deviceId+'_heartbeat';
        let timestamp_dev = await Domain.redis.client.getAsync(deviceId_h);
        if(timestamp_dev!=null){
            let time_now = new Date();

            if((time_now-timestamp_dev)<(1000*60*3)){
                let status = 1;
            }else{
                let status = 0;
            };

            await Domain.models.device.updateOne(
                { _id: deviceId },
                {$set:{ status:status }}
            )
        }
    }
};
later.setInterval(updateDeviceStatus,later.parse.cron('*/3 * * * *'));