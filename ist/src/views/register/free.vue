<template>
    <div class="mt">
        <div class="complete">
            <div class="countDown">
                <img src="/static/img/clock.png" alt="">
                <div>倒计时00:{{zero?'0':''}}{{count}}</div>
            </div>
            <div class="complete-success">
                <img src="/static/img/success.png" alt="">
                <div>选择疫苗成功!</div>
            </div>
            <div class="complete-detail">
                <detail />
            </div>
            <div>
                <div class="complete-notice">请前往等待区，等待接种</div>
                <div class="complete-message">您当前处于089号，前面还有32位</div>
            </div>
            <div class="complete-confirm" @click="back()">确认</div>
        </div>
    </div>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';
import detail from '../../components/detail';

export default {
    data () {
        return {
            count: 10,
            zero: false,
            timer: '',
            data: null //接种信息
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
        back(){
            clearInterval(this.timer);
            this.saveUser(null);
            this.$router.push('/main');
        },
        initData(){
            //从vuex user 里获取接种数据
            //this.data = this.user;
        }
    },
    mounted(){
        //倒计时
        this.countdown();
        //初始接种数据
        this.initData();
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/select.less';
</style>