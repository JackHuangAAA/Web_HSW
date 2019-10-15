<template>
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
                <div class="user" @click="logout()">
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
                nowdate: null,
                code: '17694709073',
                pageName: '主页',
                menu: [{name:'主页',img:'/static/img/home.png'},{name:'库存',img:'/static/img/inventory.png'},{name:'报警',img:'/static/img/alarm.png'},{name:'设置',img:'/static/img/setting.png'}],
                isactive: 0,
                ifShowMenu: false ,
                menuStatus: '展开菜单'
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
            console.log(this.$route.path);
            console.log('111111')
            if(this.$route.path == '/'){
                //this.$router.push('main');
                this.$router.push('setting/setting');
            }
        }
    }
</script>
<style lang="less" scoped>
@import "~@/style/home.less";
</style>