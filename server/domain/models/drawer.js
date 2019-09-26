/**
 * 抽屉信息
 */
const drawerSchema = mongoose.Schema({
    device: {type:mongoose.Schema.Types.ObjectId, ref:'device'}, //设备
    x: String,     //横坐标
    y: String,      //纵坐标
    vaccine: [{type:mongoose.Schema.Types.ObjectId, ref:'vaccine'}] //疫苗
}, {autoIndex: false});
drawerSchema.plugin(paginate);
module.exports = mongoose.model('drawer', drawerSchema);