const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 接种记录信息
 */
const vaccinationSchema = mongoose.Schema({
    user: {type:mongoose.Schema.Types.ObjectId, ref:'user'}, //操作医生
    sort: String,       //排队序号(叫号码)
    customer:{          //顾客
        code: String,   //接种序号
        name: String,   //姓名
        age: Number,    //年龄
        vaccineCode: String,//疫苗编号
        vaccineName: String,//疫苗名称
        vaccineNum: Number //疫苗数量
    },
    match:{                 //匹配信息(政采云响应结果)
        vaccineCode: String,//疫苗编号
        vaccineName: String,//疫苗名称
        supervisionCode: String,//药品监管码
        expiry: Date,       //药品监管码
        producer: String    //生产商
    },
    result:Number,          //匹配结果(0:不匹配;1:匹配)
    createDate: {type: Date, default: Date.now}  //匹配时间
}, {autoIndex: false});
vaccinationSchema.plugin(paginate);
module.exports = mongoose.model('vaccination', vaccinationSchema);