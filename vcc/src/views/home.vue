<template>
<<<<<<< HEAD
    <div class="layout">
=======
    <!-- <div class="layout">
        <header class="layout-nav">
            <div class="layout-nav-wrap">
                <div class="layout-logo">
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
>>>>>>> 629a1754c8bc540f0f8863c8b089fb75ac43843e
        <div class="layout-main">
            <router-view ref="contentView" class="layout-main-page"></router-view>
            <Spin fix v-show="loading">
                <Icon type="load-c" size=18 class="demo-spin-icon-load"></Icon>
                <div>加载中...</div>
            </Spin>
        </div>

    </div> -->
        <div class="layout">
            <div class="menuList"  v-bind:class='{menuNone:ifShowMenu == false}'>
                    <img class="menuListLogo" src="/static/img/logo.png">
                    <p class="logoDes">银信疫苗接种平台</p>
                    <div class="local">西湖区</div>
                    <p class="menuListP">菜单列表</p>
                    <div class="menuBlock" v-for="(item,index) in menu" @click="changeMenu(index)" v-bind:class='{bg:index==isactive}'>
                        <img class="menuImg" :src="item.img">
                        {{item.name}}
                    </div>
                </div>
            <div class="head">
                <div class="menu" @click="openCloseMenu()">
                    <div class="menuContent">
                        <img src="/static/img/userph1.png">
                        {{menuStatus}}
                    </div>
                </div>
                <div class="pageName">{{pageName}}</div>
                <div class="code">{{code}}</div>
                <div class="user">
                    <p>李晓文</p>
                    <img src="/static/img/userph1.png">
                </div>

            </div>
            <div class="main">
                <router-view ref="contentView" class="layout-main-page"></router-view>
            </div>
            <div class="foot">
                <p class="dateTime">{{nowdate}}</p>
            </div>
        </div>
</template>
<script>
    import {mapGetters, mapActions, mapState} from 'vuex'
    import md5 from 'js-md5'
    import routerConfig from '@/router'
    import moment from 'moment';
    moment.locale('zh-cn');
    global.moment = moment;

    export default {
        name: "datetime",
        data () {
            return {
                version: this.$config.version,
                title: this.$config.appName,
<<<<<<< HEAD
                loading: false
=======
                nowdate: null,
                code: '17694709073',
                pageName: '主页',
                menu: [{name:'主页',img:'/static/img/home.png'},{name:'库存',img:'/static/img/inventory.png'},{name:'报警',img:'/static/img/alarm.png'},{name:'设置',img:'/static/img/setting.png'}],
                isactive: 0,
                ifShowMenu: false ,
                menuStatus: '展开菜单'
>>>>>>> 629a1754c8bc540f0f8863c8b089fb75ac43843e
                }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                device: 'device'
            })
        },
        created() {
            this.dateint();
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
            dateint() {
                setInterval(() => {
                    let nowdate = moment().format("YYYY-MM-DD HH:mm:ss dddd");
                    this.nowdate = nowdate;
                }, 500);
            },
            changeMenu: function(index){
                this.isactive = index;
            },
            openCloseMenu: function(){
                this.ifShowMenu = !this.ifShowMenu
                if(this.ifShowMenu == true){
                    this.menuStatus = '折叠菜单'
                }else{
                    this.menuStatus = '展开菜单'
                }
            }
        },
        mounted(){
            //获取设备信息
<<<<<<< HEAD
            this.$device.getDeviceCode().then(res => {console.log("======HOME======="+res.deviceId);
                this.$api.get('/device/queryDeviceByCondition',{code:res.deviceId}).then((res)=>{
                    console.log("DEVICE======="+JSON.stringify(res));
                    this.saveDevice(res.data[0]);
                });
            });

=======
            console.log(this.$route.path);
            console.log('111111')
>>>>>>> 629a1754c8bc540f0f8863c8b089fb75ac43843e
            if(this.$route.path == '/'){
                this.$router.push('/setting/setting');
            }
        }
    }
</script>
<style lang="less" scoped>
@import "~@/style/home.less";
</style>