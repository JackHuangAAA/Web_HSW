<template>
    <div class="pay">
        <div class="pay-top">
            <div class="pay-back"  v-if="!ifCash" @click="back(true)">返回</div>
            <div class="pay-top-title">{{type=='hand'?'请到人工窗口付费':'请扫码付费'}}</div>
            <div class="countDown" v-show="ifCash">
                <img src="/static/img/clock.png" alt="">
                <div>倒计时00:{{zero?'0':''}}{{count}}</div>
            </div>
        </div>
        <div class="pay-content">
            <div class="tips"  v-if="!ifCash">
                请用{{type=='ali'?'支付宝':'微信'}}扫码付款:
            </div>
            <div class="vaccineName">
                {{data?data.vaccine.name:''}}    自费
            </div>
            <div class="price">
                ￥{{data?data.vaccine.cost:''}}
            </div>
            <div class="qrcodeImg" v-show="!ifCash">
                <img :src="payPic" @click="finishPay"/>
            </div>

            <div class="otherWaysTip" v-if="!ifCash">
                其他支付的方式:
            </div>
            <div class="otherWays" v-if="!ifCash">
                <div class="otherWaysImg" v-for="(item,index) in otherWays" :key="item.payName" @click="changeWays(item.type)">
                    <img :src="item.img" class="otherWaysImg_img">
                    <div class="payName">{{item.payName}}</div>
                </div>
            </div>
            <detail />
            <div class="cashPay" v-show="ifCash">
                <!--<div class="cashTip">请前往人工付费窗口支付费用</div>-->
                <div class="complete-confirm" @click="back(false)">返回首页</div>
            </div>
        </div>
        
    </div>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';
import detail from '../../components/detail';

export default {
    data () {
        return {
            data:{},
            zero: false,
            count: 10,
            type: '', //支付方式
            payPic: '',
            otherWays:[],
            ifCash: false,
            timer: ''
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
        changeWays(type){
            this.type = type;
            this.ifCash = false;
            this.initPays();
        },
        finishPay(){
            this.$router.push({path:'/pay/finish'});
        },
        back(val){
            clearInterval(this.timer);
            if(val){
                this.$router.push({path:'/register/register'});
            }else{
                this.saveUser(null);
                this.$router.push({path:'/main'});
            }
        },
        //倒计时
        countdown(){
            this.timer = setInterval(() => {
                this.count --;
                if(this.count<10){
                    this.zero = true;
                }
                if(this.count == 0){
                    clearInterval(this.timer);
                    this.$router.push('/main');
                }
            }, 1000);
        },
        initPays(){
            this.otherWays = [
                {payName: '支付宝',img:'/static/img/zfbBig.png', type:'ali'},
                {payName: '微信支付',img:'/static/img/wxBig.png', type:'we'},
                {payName: '人工支付',img:'/static/img/peoplePay.png', type:'hand'}
            ];
            if(this.type == 'ali'){
                this.payPic = '/static/img/alipay.png';
                this.otherWays.splice(0,1);
                //this.finishPay();
            }else if(this.type =='we'){
                this.payPic = '/static/img/wepay.png';
                this.otherWays.splice(1,1);
                //this.finishPay();
            }else{
                this.otherWays.splice(2,1);
                this.ifCash = true;
                this.countdown();
            }
        },
        initData(){
            //获取vuex里的客户信息
            this.data = this.user;
            this.type = this.$route.query.way;
        },
    },
    mounted() {
        //初始接种数据
        this.initData();
        //初始页面支付方式
        this.initPays();
    },
}
</script>
<style lang="less" scoped>
@import '~@/style/pay.less';
</style>