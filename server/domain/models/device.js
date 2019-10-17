const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 设备基本信息
 */
const deviceSchema = mongoose.Schema({
    code: String, //设备编号（设备上送）
    alias: String,//别名（客户自定义）
    type: Number, //类型(1:接种柜;2:冷藏柜)
    status: Number, //状态（0:离线 1:在线 2:故障）
    address:{
        provinceCode: String,   //省份编码
        provinceName: String,   //省份名称
        cityCode: String,       //市编码
        cityName: String,       //市
        countyCode: String,     //县(区)编码
        countyName: String,     //县(区)
        townCode: String,       //镇(乡、街道)编码
        townName: String        //镇(乡、街道)
    },
    cabinetNo: String,//接种台号
    unitCode: String, //所属单位编号
    unitName: String, //所属单位
    notes: String,    //备注
    temperature:Number, //最新一次设备温度
    createDate: {type: Date, default: Date.now}, //创建时间
    updateDate: {type: Date, default: Date.now}  //更新时间
}, {autoIndex: false});
deviceSchema.plugin(paginate);
module.exports = mongoose.model('device', deviceSchema);