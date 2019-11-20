<template>
    <div class="pay">
        <div class="pay-top">
            <div class="pay-back" @click="back()">返回上一页</div>
            <div class="pay-top-title">请选择付费方式，并付费</div>
        </div>
        <div class="pay-content">
            <div class="pay-content-title">脊髓灰质炎疫苗<span>自费</span></div>
            <div class="pay-content-price">￥300.00</div>
            <div class="pay-content-notice">请选择您要支付的方式:</div>
            <div class="pay-content-box">
                <div class="pay-content-choose" v-for="(item,index) in payWays" :key="item.payWay" @click="selectPayWays(index)">
                    <div class="recommend" v-if="item.ifRecommend">推荐</div>
                    <img :src="item.img" alt="" class="payImg">
                    <div class="payDescribe">{{item.payWay}}</div>
                </div>
            </div>
            <detail/>
        </div>
        
    </div>
</template>
<script>
import detail from '../../components/detail';
export default {
    data () {
        return {
            payWays:[{ifRecommend: true,img:'/static/img/zfbBig.png',payWay:'支付宝支付'},
                    {ifRecommend: true,img:'/static/img/wxBig.png',payWay:'微信支付'},
                    {ifRecommend: false,img:'/static/img/peoplePay.png',payWay:'人工付费'}]
        }    
    },
    components:{
        detail
    },
    
    methods:{
        selectPayWays: function(index){
            this.$router.push({path:'/pay/payQrcode',query:{way:index}});
        },
        back: function(){
            this.$router.push({path:'/complete/complete',query:{type:true}});
        }
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/pay.less';
</style>