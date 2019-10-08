const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 日志信息(操作日志)
 */
const logSchema = mongoose.Schema({
    userCode: String,   //用户账号
    userName: String,   //用户名称
    device: {type:mongoose.Schema.Types.ObjectId, ref:'device'}, //设备
    deviceType: Number, //设备类型
    unitCode: String,   //所属单位编号
    unitName: String,   //所属单位
    action: String,     //动作（1、签到；2、签退;3、取疫苗;4、入库;5、出库）
    content: String,    //详细内容
    operatorDate: {type: Date, default: Date.now}  //操作时间
}, {autoIndex: false});
logSchema.plugin(paginate);
module.exports = mongoose.model('log', logSchema);