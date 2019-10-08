const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 出入库记录`
 */
const inoutSchema = mongoose.Schema({
    type: Number, //类型(1:入库;2:出库)
    user: {type:mongoose.Schema.Types.ObjectId, ref:'user'},//用户
    device: {type:mongoose.Schema.Types.ObjectId, ref:'device'}, //设备
    deviceType: Number, //设备类型
    unitCode: String,   //所属单位编号
    unitName: String,   //所属单位
    x: Number,          //横坐标
    y: Number,          //纵坐标
    supervisionCode: String,//药品监管码
    vaccineCode: String,//疫苗编号
    vaccineName: String,//疫苗名称
    total: Number,      //数量(入库总数)
    surplus: Number,    //剩余数量
    use: Number,        //使用数量(use=total-surplus)
    createDate: {type: Date, default: Date.now}  //更新时间
}, {autoIndex: false});
inoutSchema.plugin(paginate);
module.exports = mongoose.model('inout', inoutSchema);