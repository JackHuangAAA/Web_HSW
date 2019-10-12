const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 日汇总信息（单日入库总数、使用数量、剩余数量）
 */
const summarySchema = mongoose.Schema({
    device: {type:mongoose.Schema.Types.ObjectId, ref:'device'}, //设备
    deviceType: Number, //设备类型
    unitCode: String,   //所属单位编号
    unitName: String,   //所属单位
    code: String,    //疫苗编号
    name: String,    //疫苗名称
    total: Number,   //数量(入库总数)
    surplus: Number, //剩余数量
    use: Number,     //使用数量(use=total-surplus)
    date: Date       //日期(vaccine的updateDate)
}, {autoIndex: false});
summarySchema.plugin(paginate);
module.exports = mongoose.model('summary', summarySchema);