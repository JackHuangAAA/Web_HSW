const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 用户信息
 */
const queueSchema = mongoose.Schema({
    code: String, //接种编号
    name: String, //姓名
    sex: String, //性别
    age: Number, //年龄
    vaccine:{
        name: String, //疫苗名称
        code: String, //疫苗code
        producer:String, //生产厂家
        count:Number, //接种数量
        date: {type: Date, default: Date.now}  //接种时间
    },
    status: Number, //状态0:完成; 1:排队中;
    finishDate: {type: Date},   //完成时间，未完成则为空
    createDate: {type: Date, default: Date.now}   //创建时间
}, {autoIndex: false});
queueSchema.plugin(paginate);
module.exports = mongoose.model('queue', queueSchema);