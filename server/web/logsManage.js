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
        let d = moment().subtract(90, 'days').format('YYYYMM');
        let directories = fs.readdirSync('log');
        for (let dir of directories) {
            let fname = path.basename(dir);
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
}

later.setInterval(housekeeping,later.parse.cron('0 0 * * *'));
//later.setInterval(housekeeping, later.parse.recur().every(24).hour());



