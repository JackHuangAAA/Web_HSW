const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 用户信息
 */
const planSchema = mongoose.Schema({
    sort: Number, //序号
    vaccine:String, //疫苗名称
    vaccinateDate: {type: Date,default: Date.now},   //接种时间
    createDate: {type: Date, default: Date.now},   //创建时间
}, {autoIndex: false});
planSchema.plugin(paginate);
module.exports = mongoose.model('plan', planSchema);