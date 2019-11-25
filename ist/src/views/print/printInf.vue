<template>
    <div class="container">
        <div class="personInf">
            <p>基本信息</p>
            <div>
                <span class="infTitle">姓名:</span><span class="infContent">黄江华</span><span class="infTitle">性别:</span><span class="infContent">男</span>
                <span class="infTitle">年龄:</span><span class="infContent">3周岁</span><span class="infTitle">距离上次接种时间:</span><span class="infContent day">28天</span>
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
            customer: null
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
