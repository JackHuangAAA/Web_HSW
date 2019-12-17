<template>
    <div class="container">
        <img src="/static/img/logo.png" class="logo" alt="">
        <p class="code">设备编号：{{device.code}}</p>
        <p class="tip">安全用药，打印信息，排队接种</p>
        <div class="scanNotice">
            <div>请将疫苗本条码对准扫描口进行扫码</div>
        </div>
    </div>
</template>

<script>
import { mapGetters, mapActions, mapState } from 'vuex';
import moment from 'moment';
export default {
    data() {
        return {
            code:null,
            customer:{}
        }
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
         async scanBarcode(){
            this.$device.subscribe('SCANNER_RESULT', async (data) => {
                console.log('SERVER_PUSH==>SCANNER_RESULT,result:' + JSON.stringify(data));
                if(this.$route.path!='/print/printMain'){
                    return false;
                }
                this.code = data.data;
                // this.code = 'A014';
                console.log("这里是扫码获取的结果=====>"+JSON.stringify(this.code));
                let customer = await this.$api.get('/queue/queryQueueByCondition',{code:this.code});
                this.customer = customer.data[0];
                //获取距离上次接种的时间
                let pre = await this.$api.get('/customer/queryCustomerByCondition',{code:this.code});
                let previou_time = pre.data[0].previou.date?pre.data[0].previou.date:new Date();
                this.$set(this.customer,'intervalTime',moment(new Date()).diff(moment(previou_time),'days'));
                this.$set(this.customer,'row',pre.data[0].row);
                //扫描结果存入vuex user
                this.saveUser(this.customer);
                this.$router.push({path:'/print/printInf'});
            });
        }
    },
    mounted(){
        //监听扫描条形码结果
        this.scanBarcode();
    }
}
</script>

<style lang="less" scoped>
@import '~@/style/printMain.less';
</style>
