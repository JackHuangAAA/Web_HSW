<template>
    <div class="pay">
        <div class="pay-top">
            <div class="pay-back" @click="back()">返回上一页</div>
            <div class="pay-top-title">请扫码付费</div>
        </div>
        <div class="pay-content">
            <div class="tips">
                请用支付宝扫码付款:
            </div>
            <div class="vaccineName">
                脊髓灰质炎疫苗    自费
            </div>
            <div class="price">
                ￥300.00
            </div>
            <div class="qrcodeImg" v-show="!ifCash">

            </div>
            <div class="cashPay" v-show="ifCash">
                <div class="cashTip">请前往人工付费窗口支付费用</div>
                <div class="complete-confirm" @click="confirm()">确认</div>
            </div>
            <div class="otherWaysTip">
                其他支付的方式:
            </div>
            <div class="otherWays">
                <div class="otherWaysImg" v-for="(item,index) in otherWays" :key="item.payName" @click="changeWays(item)">
                    <img :src="item.img" class="otherWaysImg_img">
                    <div class="payName">{{item.payName}}</div>
                </div>
            </div>
            <detail class="qrCodeDetail"/>
        </div>
        
    </div>
</template>
<script>
import detail from '../../components/detail';
export default {
    data () {
        return {
            otherWays:[{payName: '支付宝支付',img:'/static/img/zfbBig.png'},{payName: '人工支付',img:'/static/img/peoplePay.png'}],
            ifCash: false,
            timer: ''
        }    
    },
    components:{
        detail
    },
    mounted() {
        console.log(this.$route.query.way)
        if(this.$route.query.way == 0){
            this.otherWays = [{payName: '微信支付',img:'/static/img/wxBig.png'},{payName: '人工支付',img:'/static/img/peoplePay.png'}];
            this.finishQrcode();
        }else if(this.$route.query.way == 1){
            this.otherWays = [{payName: '支付宝支付',img:'/static/img/zfbBig.png'},{payName: '人工支付',img:'/static/img/peoplePay.png'}];
            this.finishQrcode();
        }else{
            this.ifCash = true;
            this.otherWays = [{payName: '支付宝支付',img:'/static/img/zfbBig.png'},{payName: '微信支付',img:'/static/img/wxBig.png'}]
        }
    },
    methods:{
        changeWays: function(item){
            console.log(item)
            if(item.payName == "支付宝支付"){
                clearTimeout(this.timer);
                this.ifCash = false;
                this.otherWays = [{payName: '微信支付',img:'/static/img/wxBig.png'},{payName: '人工支付',img:'/static/img/peoplePay.png'}];
                this.finishQrcode();
            }else if(item.payName == "微信支付"){
                clearTimeout(this.timer);
                this.ifCash = false;
                this.otherWays = [{payName: '支付宝支付',img:'/static/img/zfbBig.png'},{payName: '人工支付',img:'/static/img/peoplePay.png'}];
                this.finishQrcode();
            }else if(item.payName == "人工支付"){
                clearTimeout(this.timer);
                this.ifCash = true;
                this.otherWays = [{payName: '支付宝支付',img:'/static/img/zfbBig.png'},{payName: '微信支付',img:'/static/img/wxBig.png'}]
            }
        },
        confirm: function(){
            this.$router.push({path:'/complete/complete',query:{type: false,ifCash: true}});
        },
        finishQrcode: function(){
            this.timer = setTimeout(()=>{
                console.log(111)
                this.$router.push({path:'/complete/complete',query:{type: false,ifCash: false}});
            },5000)
        },
        back: function(){
            clearTimeout(this.timer);
            this.$router.push({path:'/pay/pay'});
        }
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/pay.less';
</style>