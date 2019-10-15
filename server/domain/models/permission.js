/**
 * Created by Administrator on 2019/10/15 0005.
 */

const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

// 二级菜单
const sub2PermissionSchema = mongoose.Schema({
    pid: mongoose.Schema.Types.ObjectId, //父节点id
    title: String, //名称
    url: String,   //访问url
    icon: String,  //图标
    sort: Number,  //排序
    expand: {type: Boolean, default: true}, //收缩状态
}, {autoIndex: false});

// 一级级菜单
const subPermissionSchema = mongoose.Schema({
    pid: mongoose.Schema.Types.ObjectId, //父节点id
    title: String,  //名称
    url: String,    //访问url
    icon: String,   //图标
    sort: Number,   //排序
    children: [sub2PermissionSchema],
    expand: {type: Boolean, default: true} //收缩状态
}, {autoIndex: false});

// 权限
const permissionSchema = mongoose.Schema({
    children: [subPermissionSchema],
    createDate: {type: Date, default: Date.now}, //创建时间
    updateDate: {type: Date, default: Date.now}  //更新时间
}, {autoIndex: false});

permissionSchema.plugin(paginate);
module.exports = mongoose.model('permission', permissionSchema);
