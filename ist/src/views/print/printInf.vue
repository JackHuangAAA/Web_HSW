<template>
    <div class="container">
        <div class="personInf">
            <p>基本信息</p>
            <div>
                <span class="infTitle">姓名:</span><span class="infContent">{{customer?customer.name:''}}</span><span class="infTitle">性别:</span><span class="infContent">{{customer?customer.sex:''}}</span>
                <span class="infTitle">年龄:</span><span class="infContent">{{customer?customer.age:''}}周岁</span><span class="infTitle">距离上次接种时间:</span><span class="infContent day">{{customer?customer.intervalTime:''}}天</span>
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
            //调用打印接口

            this.$router.push('/print/printEnd')
        },
        back: function(){
            this.$router.push('/print/printMain')
        },
        initData(){
            //获取客户信息和疫苗信息
            this.customer = this.user;
            console.log(this.customer)

        }
    },
    mounted(){
        //初始数据
        this.initData();
    }
}
</script>

<style lang="less" scoped>
@import '~@/style/printInf.less';
</style>
