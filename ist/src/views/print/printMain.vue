<template>
    <div class="container">
        <img src="/static/img/logo.png" class="logo" alt="">
        <p class="code">设备编号：PT0001</p>
        <p class="tip">安全用药，打印信息，排队接种</p>
        <div class="scanNotice">
            <div>接种信息打印</div>
            <div>请将疫苗本条码对准扫描口，进行扫码</div>
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
        getDevice(){
            this.$device.getDeviceCode().then(res => {
                this.deviceId = res;
                this.saveDevice({id:res})
            });
        },
         async scanBarcode(){
            /*
            this.$device.subscribe('SCAN_BARCODE', (data) => {
                console.log('SERVER_PUSH==>SCAN_BARCODE,result:' + JSON.parse(data.res));
                //扫描结果存入vuex user
                //this.saveUser();
            });
            */

            this.code = "12306"

            let customer = await this.$api.get('/queue/queryQueueByCondition',{code:this.code});
            this.customer = customer.data[0];

            //获取距离上次接种的时间
            customer = await this.$api.get('/customer/queryCustomerByCondition',{code:this.code});

            let previou_time = customer.data[0].previou.date?customer.data[0].previou.date:new Date();

            this.customer.intervalTime = moment(new Date()).diff(moment(previou_time),'days');

            this.saveUser(this.customer);
            this.$router.push({path:'/print/printInf'});
        }
    },
    mounted(){
        //获取设备信息
        //this.getDevice();
        //监听扫描条形码结果
        this.scanBarcode();
        //this.$router.push('/print/printInf');

    }
}
</script>

<style lang="less" scoped>
@import '~@/style/printMain.less';
</style>
