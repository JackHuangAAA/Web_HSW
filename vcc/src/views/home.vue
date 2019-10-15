<template>
    <div class="layout">
        <div class="layout-main">
            <router-view ref="contentView" class="layout-main-page"></router-view>
            <Spin fix v-show="loading">
                <Icon type="load-c" size=18 class="demo-spin-icon-load"></Icon>
                <div>加载中...</div>
            </Spin>
        </div>

    </div>
</template>
<script>
    import {mapGetters, mapActions, mapState} from 'vuex'
    import md5 from 'js-md5'
    import routerConfig from '@/router'

    export default {
        data () {
            return {
                version: this.$config.version,
                title: this.$config.appName,
                loading: false
                }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        components:{},
        watch:{
           /* '$route.path':function(n,o){
                this.currentPath = n
            }*/
        },
        methods: {
            ...mapActions({
                saveUser: 'saveUser',
                saveDevice: 'saveDevice'
            }),
            logout: function () {
                this.$api.get('/user/logout').then(()=>{
                    this.saveUser(null);
                    window.location.href="/";
                });
            },
        },
        mounted(){
            //获取设备信息
            this.$device.getDeviceCode().then(res => {console.log("======HOME======="+res.deviceId);
                this.$api.get('/device/queryDeviceByCondition',{code:res.deviceId}).then((res)=>{
                    console.log("DEVICE======="+JSON.stringify(res));
                    this.saveDevice(res.data[0]);
                });
            });

            if(this.$route.path == '/'){
                this.$router.push('/setting/setting');
            }
        }
    }
</script>
