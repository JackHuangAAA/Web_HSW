<template>
    <div class="mt">
        <div class="complete">
            <div class="countDown">
                <img src="/static/img/clock.png" alt="">
                <div>倒计时00:{{zero?'0':''}}{{count}}</div>
            </div>
            <div class="complete-success">
                <img src="/static/img/success.png" alt="">
                <div>信息打印成功！</div>
            </div>
            <div class="complete-detail">
                <detail/>
            </div>
            <div class="complete-confirm" @click="confirm()">确认</div>
        </div>
    </div>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';
import detail from '../../components/detail';

export default {
    data () {
        return {
            zero: false,
            count: 10,
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
        countdown(){
            this.timer = setInterval(() => {
                this.count --;
                if(this.count<10){
                    this.zero = true;
                }
                if(this.count == 0){
                    clearInterval(this.timer);
                    this.$router.push('/print/printMain');
                }
            }, 1000);
        },
        confirm: function(){
            clearInterval(this.timer);
            this.saveUser(null);
            this.$router.push('/print/printMain')
        }
    },
    mounted(){
        this.countdown();
    },
}
</script>
<style lang="less" scoped>
@import '~@/style/printEnd.less';
</style>