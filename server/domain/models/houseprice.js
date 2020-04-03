const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 房屋价格
 */
const housepriceSchema = mongoose.Schema({
    user: {type:mongoose.Schema.Types.ObjectId, ref:'user'}, //用户名
    house: {type:mongoose.Schema.Types.ObjectId, ref:'house'}, //房源
    area:Number,//面积
    unit:Number,//单价
    total:Number,//总价
    createDate: {type: Date, default: Date.now},   //更新时间
}, {autoIndex: false});
housepriceSchema.plugin(paginate);
module.exports = mongoose.model('houseprice', housepriceSchema);