<template>
    <div class="container">
        <div class="personInf">
            <p>基本信息</p>
            <div>
                <span class="infTitle">姓名:</span><span class="infContent">{{user?user.name:''}}</span><span class="infTitle">性别:</span><span class="infContent">{{user?user.sex:''}}</span>
                <span class="infTitle">年龄:</span><span class="infContent">{{user?user.age:''}}周岁</span><span class="infTitle">距离上次接种时间:</span><span class="infContent day">{{user?user.intervalTime:''}}天</span>
            </div>
        </div>
        <div class="vaccineInf">
            <p>疫苗信息</p>
            <detail class="detail"></detail>
        </div>
        <div class="btn-box">
            <div class="cancel" @click="back">取消</div>
            <div class="confirm" @click="confirm()">打印信息</div>
        </div>
        <p class="confirmTip">请将疫苗本放入打印机，点击打印信息按钮</p>
    </div>
</template>

<script>
import { mapGetters, mapActions, mapState } from 'vuex';
import detail from '../../components/detail';
import moment from 'moment';
export default {
    data() {
        return {
            customer:{}
        }
    },
    components:{
        detail
    },
    computed: {
        ...mapGetters({
            user: 'user',
            device: 'device'
        })
    },
    methods:{
        ...mapActions({
            saveUser: 'saveUser',
            saveDevice: 'saveDevice'
        }),
        confirm: function(){
            console.log('调用打印接口');
            let params={
                num:1,
                need_name:true,
                vaccine:this.user.vaccine.name,
                vaccine_date:this.user.vaccine.date,
                lot_no:this.user.vaccine.batchNo,
                vaccine_unit:this.device.unitName,
                vaccine_site:'左手臂',
                signature:this.user.name
            };
            console.log(JSON.stringify(params))
            //调用打印接口
            this.$device.printBook({
                num:1,
                need_name:true,
                vaccine:this.user.vaccine.name,
                vaccine_date:moment(this.user.vaccine.date).format('YYYY/MM/DD'),
                lot_no:this.user.vaccine.batchNo,
                vaccine_unit:this.device.unitName,
                vaccine_site:'左手臂',
                signature:this.user.name
            }).then(res=>{
                console.log('打印返回结果'+JSON.stringify(res));
                this.$router.push('/print/printEnd');
            });
            console.log("这里是末尾")
        },
        back: function(){
            this.$router.push('/print/printMain')
        }
        // initData(){
        //     //获取客户信息和疫苗信息
        //     this.customer = this.user;
        //     console.log(this.customer)

        // }
    },
    mounted(){
        //初始数据
        // this.initData();
        console.log(this.device)
        console.log(this.user)
    }
}
</script>

<style lang="less" scoped>
@import '~@/style/printInf.less';
</style>
