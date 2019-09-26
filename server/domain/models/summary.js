const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 日汇总信息（单日入库总数、使用数量、剩余数量）
 */
const logSchema = mongoose.Schema({
    device: {type:mongoose.Schema.Types.ObjectId, ref:'device'}, //设备
    code: String,  //疫苗编号
    name: String,  //疫苗名称
    total: Number, //数量(入库总数)
    use: Number,   //使用数量
    surplus: Number,//剩余数量(surplus=total-use)
    date: Date     //日期
}, {autoIndex: false});
logSchema.plugin(paginate);
module.exports = mongoose.model('log', logSchema);