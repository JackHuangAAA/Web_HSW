<template>
    <div class="mt">
        <div class="complete">
            <div class="countDown">
                <img src="/static/img/clock.png" alt="">
                <div>倒计时00:15</div>
            </div>
            <div v-if="type" class="complete-success">
                <img src="/static/img/success.png" alt="">
                <div>选择疫苗成功!</div>
            </div>
            <div v-if="!type" class="complete-success">
                <img src="/static/img/success.png" alt="">
                <div>支付成功!</div>
            </div>
            <div class="complete-detail">
                <detail/>
            </div>
            <div class="complete-notice">请前往等待区，等待接种</div>
            <div class="complete-message">您当前处于089号，前面还有32位</div>
            <div class="complete-confirm">确认</div>
        </div>
    </div>
</template>
<script>
import detail from '../../components/detail';
export default {
    data () {
        return {
            type:true
        }    
    },
    components:{
        detail
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/complete.less';
</style>