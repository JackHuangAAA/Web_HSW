<template>
    <div class="mt">
        <div class="complete">
            <div class="countDown">
                <img src="/static/img/clock.png" alt="">
                <div>倒计时00:{{count}}</div>
            </div>
            <div class="complete-success">
                <img src="/static/img/success.png" alt="">
                <div>信息打印成功！</div>
            </div>
            <div class="complete-detail">
                <detail/>
            </div>
            <div class="complete-confirm" style="margin-top:11.5625rem;" @click="confirm()">确认</div>
        </div>
    </div>
</template>
<script>
import detail from '../../components/detail';
export default {
    data () {
        return {
            count: 15,
            timer: ''
        }    
    },
    components:{
        detail
    },
    mounted(){
        this.timer = setInterval(() => {
            this.count --;
            if(this.count == 0){
                clearInterval(this.timer);
                this.$router.push('/print/printMain')
                }
        }, 1000);
    },
    methods:{
        confirm: function(){
            this.$router.push('/print/printMain')
        }
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/complete.less';
</style>