const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 用户信息
 */
const userSchema = mongoose.Schema({
    code: String,    //登录账号
    name: String,    //用户名
    type: Number,    //类型
    role: {type:mongoose.Schema.Types.ObjectId, ref:'role'},        //所属角色
    password: String,    //密码
    phone: String,    //手机号
    finger: [String],//指纹(必须2个指纹信息)
    token: String,   //token值
    lastLogin: Date, //最近一次登录时间
    notes: String    //备注
}, {autoIndex: false});
userSchema.plugin(paginate);
module.exports = mongoose.model('user', userSchema);