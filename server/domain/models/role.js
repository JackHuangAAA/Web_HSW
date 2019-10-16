/**
 * Created by Administrator on 2019/10/15 0005.
 */

const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

/**
 * 角色
 */
const roleSchema = mongoose.Schema({
    name: String,    //名称
    type: Number,    //类型(0:;1:)
    permission: {type:mongoose.Schema.Types.ObjectId, ref:'permission'}, //权限
    notes: String,   //说明
    createDate: {type: Date, default: Date.now}, //创建时间
    updateDate: {type: Date, default: Date.now}  //更新时间
}, {autoIndex: false});

roleSchema.plugin(paginate);
module.exports = mongoose.model('role', roleSchema);