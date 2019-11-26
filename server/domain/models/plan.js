const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 用户信息
 */
const planSchema = mongoose.Schema({
    sort: Number, //序号
    vaccine: String, //疫苗名称
    vaccinateDate: String, //接种时间
    createDate: {type: Date, default: Date.now},   //创建时间
    updateDate: {type: Date, default: Date.now}    //更新时间
}, {autoIndex: false});
planSchema.plugin(paginate);
module.exports = mongoose.model('plan', planSchema);