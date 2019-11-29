<template>
    <div class="layout" v-if="bgShow">
        <div class="header"  >
            <img src="/static/img/whiteLogo.png" alt="">
            <div>设备编号：{{deviceId}}</div>
        </div>
        <div class="main">
            <router-view></router-view>
        </div>
        <div class="copyright">银信博荣疫苗接种一体化解决方案</div>
    </div>
    <router-view v-else></router-view>
</template>
<script>
import { mapGetters, mapActions, mapState } from 'vuex';

export default {
    data () {
        return {
            bgShow: true,
            deviceId:''
        }    
    },
    computed: {
        ...mapGetters({
            user: 'user',
            device: 'device'
        })
    },
    watch: {
        $route(to, from) {
            if(to.path == '/main'){
                this.bgShow = false;
            }else{
                this.bgShow = true;
            }
        }
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
                //console.log('SERVER_PUSH==>SCAN_BARCODE,result:' + JSON.parse(data.res));
                this.$router.push({path:'/register/register',query:{code:this.code}});


            });
            this.code = "12306"
        }
    },
    mounted(){
        //获取设备信息
        this.getDevice();
        //监听扫描条形码结果
        this.scanBarcode();
        // this.$router.push('/main');

    }
}
</script>
<style lang="less" scoped>
@import '~@/style/home.less';
</style>