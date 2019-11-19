<template>
    <div class="pay">
        <div class="pay-top">
            <div class="pay-back">返回上一页</div>
            <div class="pay-top-title">请选择付费方式，并付费</div>
        </div>
        <div class="pay-content">
            <div class="pay-content-title">脊髓灰质炎疫苗<span>自费</span></div>
            <div class="pay-content-price">￥300.00</div>
            <div class="pay-content-notice">请选择您要支付的方式:</div>
            <div class="pay-content-box">
                <div class="pay-content-choose">
                    <div>推荐</div>
                    <img src="/static/img/zfbBig.png" alt="">
                    <div>支付宝支付</div>
                </div>
                <div class="pay-content-choose">
                    <div>推荐</div>
                    <img src="/static/img/wxBig.png" alt="">
                    <div>微信支付</div>
                </div>
                <div class="pay-content-choose">
                    <img src="/static/img/wxBig.png" alt="">
                    <div>人工付费</div>
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
            
        }    
    },
    components:{
        detail
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/pay.less';
</style>