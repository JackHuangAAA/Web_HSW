<template>
    <div class="layout" v-if="bgShow">
        <div class="header" >
            <img src="/static/img/whiteLogo.png" alt="">
            <div>设备编号：{{deviceId}}</div>
        </div>
        <div class="main">
            <router-view></router-view>
        </div>
        <!-- <div class="copyright">银信博荣疫苗接种一体化解决方案</div> -->
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
            if(to.path == '/print/printMain'){
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
            console.log("printHome getDevice")
            this.$device.getDeviceCode().then(res => {
                console.log("getDeviceCode result"+JSON.stringify(res));
                this.$api.get('/device/queryDeviceByCondition',{code:res}).then((res2)=>{
                    this.saveDevice(res2.data[0]);
                    this.deviceId = res2.data[0].code;
                    this.$router.push('/print/printMain');
                });
                
            });
        },
    },
    mounted(){
        console.log("printHome mounted")
        //获取设备信息
        this.getDevice();
        //this.code='12306'
        //this.$router.push({path:'/print/printInf',query:{code:this.code}});
    }
}
</script>
<style lang="less" scoped>
    @import '~@/style/printHome.less';
</style>