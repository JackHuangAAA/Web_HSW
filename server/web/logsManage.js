/**
 * Created by apple on 2019/9/24.
 */
const logger = Libs.logger.getLogger('logsManage');
let later = require('later');
let moment = require('moment');
let path = require('path');
let fs = require('fs');
later.date.localTime();//设置本地时区

async function housekeeping () {
    let start = Date.now(), status=0, time=0, msg='';
    let exp = /^(\d{4})-(\d{2})$/;
    try{
        logger.info('start clean log files!');
        let d = moment().subtract(90, 'days').format('YYYYMM');//获取90天前的日期
        let directories = fs.readdirSync('log');//返回一个包含“指定目录下所有文件名称”的数组对象。
        for (let dir of directories) {
            let fname = path.basename(dir);  //提取出用 ‘/' 隔开的path的最后一部分,可添加过滤参数
            if (!exp.test(fname)) continue;
            let f = moment(fname).format('YYYYMM');
            if (f < d) {
                //fs.unlinkSync(path.join('log', file));
                let filePath = path.join('log', fname);
                if (fs.existsSync(filePath)) {
                    let files = fs.readdirSync(filePath);
                    files.forEach(function (file, index) {
                        var curPath = path.join(filePath, file);
                        // delete file
                        fs.unlinkSync(curPath);
                    });
                    // delete directory
                    fs.rmdirSync(filePath);
                }
            }
        }
    }catch(err) {
        status = 1;
        msg = err.message;
    }finally {
        time = Date.now() - start;
        //await Domain.services.execution.saveExecution({name:'logsManage', time:time, status:status, reason:msg});
    }
    logger.info('end clean log files,use time:'+time);
    //console.log("clean over");
}
let basic = {h:[0],m:[0]};
let composite=[basic];
let sched = { schedules:composite };
var t=later.setInterval(housekeeping,sched);
//later.setInterval(housekeeping,later.parse.cron('0 0 * * *'));
//later.setInterval(housekeeping, later.parse.recur().every(24).hour());



