<template>
    <div class="layout">
        <div class="header">
            <img src="/static/img/whiteLogo.png" alt="">
            <div>设备编号：{{deviceId}}</div>
        </div>
        <div class="main">
            <router-view></router-view>
        </div>
        <div class="copyright">银信博荣疫苗接种一体化解决方案</div>
    </div>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';

export default {
    data () {
        return {
            deviceId: ''
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
            });
        },
        scanBarcode(){
            this.$device.subscribe('SCAN_BARCODE', (data) => {
                console.log('SERVER_PUSH==>SCAN_BARCODE,result:' + JSON.parse(data.res));
                //扫描结果存入vuex user
                this.saveUser();

            });
        }
    },
    mounted(){
        //获取设备信息
        this.getDevice();
        //监听扫描条形码结果
        this.scanBarcode();
        //this.$router.push('/main');
        this.$router.push('/register/register');
        //this.$router.push('/print/printInf');
    }
}
</script>
<style lang="less" scoped>
@import '~@/style/home.less';
</style>