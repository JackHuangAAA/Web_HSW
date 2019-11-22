<template>
    <div class="container">
        <div class="title">基本信息</div>
        <div class="userInfo">
            <div>姓名：<span>黄江华</span></div>
            <div>性别：<span>男</span></div>
            <div>年龄：<span>3周岁</span></div>
            <div>距离上次接种时间：<span class="yellow">28天</span></div>
        </div>
        <div class="title">请选择接种疫苗</div>
        <div class="content">
            <div class="left activebg">
                <div class="acitve-title">免费疫苗</div>
                <div class="left-box">
                    <div class="left-left">
                        <div class="box-top">
                            <div class="box-title">脊髓灰质炎疫苗</div>
                            <div>监管码：<span>Y750768560644</span></div>
                            <div>批次号：<span>Y750230-64368</span></div>
                            <div>疫苗剂量：<span>0.5ml/支</span></div>
                        </div>
                        <div class="mb">有效期：<span>2019-09-18 12:30:28   至  2022-12-29 12:20:19</span></div>
                        <div>生产厂家：<span>北京科兴生物制品有限公司</span></div>
                    </div>
                    <div class="left-right">
                        <div>免费￥0.00</div>
                        <!--<Checkbox v-model="self" value="" label=""> </Checkbox>-->
                    </div>
                </div>
            </div>
            <div class="right">
                <div class="acitve-title">自费疫苗</div>
                <div class="right-box">
                    <div class="right-left">
                        <div class="box-top">
                            <div class="box-title">脊髓灰质炎疫苗</div>
                            <div>监管码：<span>HU6894754377</span></div>
                            <div>批次号：<span>HU5747908488</span></div>
                            <div>疫苗剂量：<span>0.5ml/支</span></div>
                        </div>
                        <div class="mb">有效期：<span>2019-09-18 12:30:28   至  2022-12-29 12:20:19</span></div>
                        <div>生产厂家：<span>中科生物制药股份有限公司</span></div>
                    </div>
                    <div class="right-right">
                        <div>自费￥300.00</div>
                        <Checkbox v-model="self" value="" label=""> </Checkbox>
                    </div>
                </div>  
            </div>
        </div>
        <div class="btn-box">
            <div class="cancel" @click="quit()">退出</div>
            <div class="confirm" @click="confirm()">确认</div>
        </div>
    </div>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';

export default {
    data () {
        return {
            self:false
        }    
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
        confirm(){
            if(this.self){
                this.$router.push({path:'/pay/pay',query:{type: this.self}})
            }else{
                this.$router.push({path:'/register/free'})
            }
        },
        quit(){
            this.saveUser(null);
            this.$router.push('/main');
        }

    },
    mounted(){

    }
}
</script>
<style lang="less" scoped>
@import '~@/style/register.less';
</style>