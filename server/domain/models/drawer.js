const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 抽屉信息(仅疫苗柜使用抽屉)
 */
const drawerSchema = mongoose.Schema({
    device: {type:mongoose.Schema.Types.ObjectId, ref:'device'}, //设备
    unitCode: String, //所属单位编号
    unitName: String, //所属单位
    x: String,        //横坐标
    y: String,        //纵坐标
    vaccine: [{type:mongoose.Schema.Types.ObjectId, ref:'vaccine'}] //疫苗
}, {autoIndex: false});
drawerSchema.plugin(paginate);
module.exports = mongoose.model('drawer', drawerSchema);