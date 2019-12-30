const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 用户信息
 */
const customerSchema = mongoose.Schema({
    code: String, //接种编号
    name: String, //姓名
    sex: String, //性别
    age: Number, //年龄
    row:Number,//测试打印行数
    previou:{
        plan: {type:mongoose.Schema.Types.ObjectId, ref:'plan'}, //上一次接种计划
        date: {type: Date, default: Date.now}  //上一次接种计划的时间
    },
    next:{
        plan: {type:mongoose.Schema.Types.ObjectId, ref:'plan'}, //下一次接种计划
        date: {type: Date, default: Date.now}
    },
    status:Number, //状态0:接种完成; 1:挂号完成; 2:缴费完成; 3:登记完成
    createDate: {type: Date, default: Date.now},   //创建时间
}, {autoIndex: false});
customerSchema.plugin(paginate);
module.exports = mongoose.model('customer', customerSchema);