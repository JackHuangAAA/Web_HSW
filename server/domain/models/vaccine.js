const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 疫苗信息(抽屉疫苗信息)
 */
const vaccineSchema = mongoose.Schema({
    device: {type:mongoose.Schema.Types.ObjectId, ref:'device'}, //设备
    code: String,     //疫苗编号
    name: String,     //疫苗名称
    batchNo: String,  //批次号
    expiry: Date,     //有效日期
    total: {type: Number, default: 0},    //数量(入库总数)
    surplus: {type: Number, default: 0},  //剩余数量
    updateDate: {type: Date, default: Date.now}  //更新时间
}, {autoIndex: false});
vaccineSchema.plugin(paginate);
module.exports = mongoose.model('vaccine', vaccineSchema);