<template>
    <div class="container">
        <img src="/static/img/logo.png" class="logo" alt="">
        <div class="device">设备编号：{{device.id}}</div>
        <div class="title">扫描疫苗本，排队挂号，智能接种</div>
        <div class="scanNotice">
            <div>扫码挂号</div>
            <div>请将疫苗本条码对准扫描口，进行扫码</div>
        </div>
        <div class="awaitPeople">当前等待接种<span>{{queueLength?queueLength:''}}</span>人</div>
    </div>
</template>

<script>
import { mapGetters, mapActions, mapState } from 'vuex';

export default {
    data() {
        return {
            queueLength:null
        }
    },
    computed: {
        ...mapGetters({
            user: 'user',
            device: 'device'
        })
    },
    methods:{
        async queryQueue() {
            //获取未完成接种的排队人数
            let queue = await this.$api.get('/queue/queryQueueByCondition', {
                status:1
            });
            this.queueLength = queue.data.length?queue.data.length:0;
        },
        scanBarcode(){
            this.$device.subscribe('SCANNER_RESULT', async (data) => {
                console.log('SERVER_PUSH==>SCANNER_RESULT,result:' + JSON.stringify(data));
                this.code = JSON.parse(data.data);
                this.$router.push({path:'/register/register',query:{code:this.code}});
            });
        }
    },
    mounted(){
        this.queryQueue();
        //监听扫描条形码结果
        this.scanBarcode();
    }
}
</script>

<style lang="less" scoped>
@import '~@/style/main.less';
</style>
