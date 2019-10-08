/**
 * Created by apple on 2019/9/24.
 */
const logger = Libs.logger.getLogger('homeData');
let later = require('later');
let moment = require('moment');
let path = require('path');
let fs = require('fs');
later.date.localTime();//设置本地时区

async function execute () {
    logger.info('home data starting......');
    console.time("home data execute");

    let now = moment()+"";

    // //按设备、药品汇总每天的处方
    // let summary = await Domain.services.recipe.dailyEndSummary();
    let alarmData = await Domain.services.alarm.queryAlarmDailyInfo();
    console.log(alarmData, 'alarmData----------')
   
    console.timeEnd("home data execute");
    logger.info('home data end......');
}

later.setInterval(execute,later.parse.cron('0 0 * * *'));
//later.setInterval(housekeeping, later.parse.recur().every(24).hour());



