<template>
    <div class="layout">
        <!-- 导航头部 -->
        <header class="layout-nav">
            <div class="layout-nav-wrap">
                <div class="layout-logo">
                    <img src="/static/img/logo.png"/>
                </div>
                <div class="layout-menu">
                    hhhhhhhhhh

                </div>

                <div class="layout-nav-profile">
                    <div class="layout-welcome">
                        欢迎，【{{ user ? user.name : ''}}】
                    </div>

                </div>
            </div>
        </header>

        <div class="layout-nav-divider"></div>
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

            if(this.$route.path == '/'){
                this.$router.push('/main');
            }
        }
    }
</script>
