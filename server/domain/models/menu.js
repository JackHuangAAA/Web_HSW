/**
 * Created by Administrator on 2019/10/16 0013.
 */

const mongoose = require('mongoose');
const paginate = require('mongoose-paginate');

// 三级菜单
const sub2MenuSchema = mongoose.Schema({
    pid: mongoose.Schema.Types.ObjectId, //父节点id
    title: String, //名称
    url: String,   //访问url
    type: Number,  //类型(0:运营公司(银信);1:服务公司(药企))
    icon: String,  //图标
    sort: Number,  //排序
    expand: {type: Boolean, default: true}, //收缩状态
}, {autoIndex: false});

// 二级级菜单
const subMenuSchema = mongoose.Schema({
    pid: mongoose.Schema.Types.ObjectId, //父节点id
    title: String,  //名称
    url: String,    //访问url
    icon: String,   //图标
    sort: Number,   //排序
    expand: {type: Boolean, default: true} //收缩状态
    //children: [sub2MenuSchema]
}, {autoIndex: false});

// 菜单
const menuSchema = mongoose.Schema({
    pid: {type: mongoose.Schema.Types.ObjectId, default: null}, //父节点id
    title: String,  //名称
    url: String,    //访问url
    icon: String,   //图标
    sort: Number,   //排序
    expand: {type: Boolean, default: true}, //收缩状态
    children: [subMenuSchema]
}, {autoIndex: false});

module.exports = mongoose.model('menu', menuSchema);