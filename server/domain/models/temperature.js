const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 温度信息(设备一天内的温度)
 */
const temperatureSchema = mongoose.Schema({
    device: {type:mongoose.Schema.Types.ObjectId, ref:'device'}, //设备
    degree: [{
        time: String, //时间
        value: Number //温度值
    }],//全天温度
    createDate: {type: Date, default: Date.now}  //操作时间
}, {autoIndex: false});
temperatureSchema.plugin(paginate);
module.exports = mongoose.model('temperature', temperatureSchema);