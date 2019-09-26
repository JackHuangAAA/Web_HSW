const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 设备基本信息
 */
const deviceSchema = mongoose.Schema({
    code: String, //设备编号（设备上送）
    alias: String,//别名（客户自定义）
    address:{
        provinceCode: String,   //省份编码
        provinceName: String,   //省份名称
        cityCode: String,       //市
        cityName: String,       //市
        countyCode: String,     //县(区)
        countyName: String,     //县(区)
        townCode: String,        //镇(乡、街道)
        townName: String        //镇(乡、街道)
    },
    unit: String,     //所属单位
    notes: String,    //备注
    createDate: {type: Date, default: Date.now}, //创建时间
    updateDate: {type: Date, default: Date.now}  //更新时间
}, {autoIndex: false});
deviceSchema.plugin(paginate);
module.exports = mongoose.model('device', deviceSchema);