const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 报警信息
 */
const alarmSchema = mongoose.Schema({
    type: Number,       //报警类型(1:温度异常;2:库存不足)
    reason: String,     //原因
    solution : String,  //解决情况
    createDate: Date,   //报警时间
    finishDate: Date    //解决时间
}, {autoIndex: false});
alarmSchema.plugin(paginate);
module.exports = mongoose.model('alarm', alarmSchema);