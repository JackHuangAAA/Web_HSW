const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 我的关注
 */
const celectionSchema = mongoose.Schema({
    user: {type:mongoose.Schema.Types.ObjectId, ref:'user'}, //用户名
    house: [type:mongoose.Schema.Types.ObjectId, ref:'house'], //房源
    createDate: {type: Date, default: Date.now},   //更新时间
}, {autoIndex: false});
celectionSchema.plugin(paginate);
module.exports = mongoose.model('celection', celectionSchema);