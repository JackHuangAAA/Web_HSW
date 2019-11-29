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
                <div class="complete-message">您当前处于{{sort?sort:''}}号，前面还有{{queueLength?queueLength:''}}位</div>
            </div>
            <div class="complete-confirm" @click="back()">返回首页</div>
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
            data: null, //接种信息
            sort:null,
            queueLength:null
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

        async queryQueue() {
            //获取最后一个排队编号
            let queue = await this.$api.get('/queue/queryQueueByCondition');
            let max = queue.data?queue.data.length:0;
            this.sort_now = (max!=0)?queue.data[max-1].sort:0;
            this.sort = this.sort_now+1;
            //获取未完成接种的排队人数
            queue = await this.$api.get('/queue/queryQueueByCondition', {
                status:1
            });
            this.queueLength = queue.data.length?queue.data.length:0;

             await this.$api.post('/queue/saveQueue', {
                sort: this.sort,
                code: this.user.customer.code,
                name: this.user.customer.name,
                sex: this.user.customer.sex,
                age: this.user.customer.age,
                vaccine:{
                    name: this.user.vaccine.name,
                    code: this.user.vaccine.code,
                    product:this.user.vaccine.product,
                    batchNo:this.user.vaccine.batchNo,
                    dosage:this.user.vaccine.dosage,
                    supervisionCode:this.user.vaccine.supervisionCode,
                    expiry:this.user.vaccine.expiry,
                    cost:this.user.vaccine.cost,
                    count:1,
                    date: new Date()
                },
                status: 1
            });
        }
    },
    mounted(){
        //将顾客接种信息插入排队队列
        this.queryQueue();
        //倒计时
        this.countdown();


    }
}
</script>
<style lang="less" scoped>
@import '~@/style/select.less';
</style>