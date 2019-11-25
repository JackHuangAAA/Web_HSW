<template>
    <div class="container">
        <img src="/static/img/logo.png" class="logo" alt="">
        <p class="code">设备编号：7549360545</p>
        <p class="tip">安全用药，打印信息，排队接种</p>
        <div class="scanNotice">
            <div>接种信息打印</div>
            <div>请将疫苗本条码对准扫描口，进行扫码</div>
        </div>
    </div>
</template>

<script>
import { mapGetters, mapActions, mapState } from 'vuex';

export default {
    data() {
        return {
               
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
        scanBarcode(){
            this.$device.subscribe('SCAN_BARCODE', (data) => {
                console.log('SERVER_PUSH==>SCAN_BARCODE,result:' + JSON.parse(data.res));
                //扫描结果存入vuex user
                this.saveUser();
                this.$router.push('/print/printInf');
            });
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
