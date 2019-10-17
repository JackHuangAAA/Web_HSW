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
    require('./devicesOfflineManage');
    require('./summariesManage');
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
