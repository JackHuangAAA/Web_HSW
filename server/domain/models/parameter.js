/**
 * Created by Administrator on 2019/11/07 0003.
 */

const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 参数管理
 */
const parameterSchema = mongoose.Schema({
    name: String, //显示名称(中文)
    key: String,  //使用名称(英文大写)
    value: String,//值
    notes: String,//说明
    isActive:{type:Boolean, default:false},     //是否启用
    createDate: {type: Date, default: Date.now}, //创建时间
    updateDate: {type: Date, default: Date.now}  //更新时间
}, {autoIndex: false});

parameterSchema.index({ name: 1, key: 1 });
parameterSchema.plugin(paginate);
module.exports= mongoose.model('parameter', parameterSchema);

