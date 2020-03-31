const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 房屋
 */
const plotSchema = mongoose.Schema({
    name: String, //房屋名称
    plot:String,//小区名称
    plate:String,//板块
    type:String,//户型
    years:String,//年限
    trade:Number,//是否满二，0:不满二；1：满二不满五；2：满五
    decoration:Number,//装修情况 0：毛坯，1：简装，2：精装
    area:Number,//面积
    unit:Number,//单价
    total:Number,//总价
    createDate: {type: Date, default: Date.now},   //创建时期
}, {autoIndex: false});
plotSchema.plugin(paginate);
module.exports = mongoose.model('plot', plotSchema);