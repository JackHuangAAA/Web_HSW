/**
 * Created by huangshaowei on 2019/10/16.
 */
const logger = Libs.logger.getLogger('devicesOfflineManage');
let later = require('later');
let moment = require('moment');
later.date.localTime();//设置本地时区

//设置定时任务，存储出库汇总信息
async function saveSummaryDaily() {
    let query = [];
    let today = moment();
    let dailyInfo={ '$gte': today.startOf('day').toDate(), '$lte': today.endOf('day').toDate() };
    //query.push({ "createDate": dailyInfo });
    query.push({ "type": 2 });
    query.push({ "deviceType": 1 });
    query = query.length>1?{"$and": query} : query.length==1 ? query[0] : {};
    //let result = await Domain.models.inout.find(query);
    let result_aggregate = await Domain.models.inout.aggregate([{$match:query},
        {
            "$group":{
                _id:{
                    "device":"$device",
                    "code":"$code",
                    "deviceType":"$device",
                    "unitCode":"$code",
                    "unitName":"$device",
                    "name":"$code"
                },
                count:{
                    $sum:"$use"}
            }
        }
    ]);

    let index;

    for(index in result_aggregate){
        let device = result_aggregate[index]._id.device;
        let code = result_aggregate[index]._id.code;
        let use = result_aggregate[index]._id.count;
        //查找最新疫苗出入库记录，获取该疫苗在该接种柜的总量和使用量
        let result_last =  await Domain.models.inout.findOne({"type":2,"deviceType":1,"createDate": dailyInfo,"device":device,"code":code}).sort({createDate:-1});

        await Domain.models.summary.create({
            device:device,
            deviceType:result_last.deviceType,
            unitCode:result_last.unitCode,
            unitName:result_last.unitName,
            code:code,
            name:result_last.name,
            total:result_last.total,
            surplus:result_last.surplus,
            use:use,
            date:result_last.createDate
        });
    };

};
later.setInterval(saveSummaryDaily,later.parse.cron('0 22 * * *'));



