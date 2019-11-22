<template>
    <div class="mt">
        <div class="complete">
            <div class="countDown">
                <img src="/static/img/clock.png" alt="">
                <div>倒计时00:{{count}}</div>
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
            <div v-if="!ifCash">
                <div class="complete-notice">请前往等待区，等待接种</div>
                <div class="complete-message">您当前处于089号，前面还有32位</div>
            </div>
            <div v-if="ifCash">
                <p class="cashPay">
                    疫苗预选成功，请前往人工付费窗口缴费
                </p>
            </div>
            <div class="complete-confirm" @click="confirm()">确认</div>
        </div>
    </div>
</template>
<script>
import detail from '../../components/detail';
export default {
    data () {
        return {
            type:true,  //true是选择成功，false是支付成功
            ifCash: false,  //是否是现金支付
            count: 15,
            timer: ''
        }    
    },
    components:{
        detail
    },
    methods:{
        confirm: function(){
            if(this.type){
                this.$router.push('/pay/pay')
            }else{
                this.$router.push('/main')
            }

        }
    },
    mounted(){
        console.log(this.$route.query.type);
        if(this.$route.query){
            this.type = this.$route.query.type
            this.ifCash = this.$route.query.ifCash
        }
        this.timer = setInterval(() => {
            this.count --;
            if(this.count == 0){
                clearInterval(this.timer);
                if(this.type){
                    this.$router.push('/pay/pay')
                    }else{
                        this.$router.push('/main')
                    }
                }
        }, 1000);
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/complete.less';
</style>