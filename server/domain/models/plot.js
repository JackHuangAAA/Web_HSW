const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 小区
 */
const plotSchema = mongoose.Schema({
    name: String, //小区名称
    plate:String,//板块
    years:String,//年限
    unit:Number,//单价
    createDate: {type: Date, default: Date.now},   //创建时期
}, {autoIndex: false});
plotSchema.plugin(paginate);
module.exports = mongoose.model('plot', plotSchema);