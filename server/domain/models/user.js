const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 用户信息
 */
const userSchema = mongoose.Schema({
    code: String,//登录账号
    password: String,//登录密码
    token: String,//token值
    lastLogin: Date,//最近一次登录时间
    remark: String//备注
}, {autoIndex: false});
userSchema.plugin(paginate);
module.exports = mongoose.model('user', userSchema);