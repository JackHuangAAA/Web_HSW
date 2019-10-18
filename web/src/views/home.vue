<template>
    <div class="layout">
        <div class="menu-list">
            <div class="menu-logo">
                <img src="/static/img/s_logo.png" alt="">
            </div>
            <div class="menu-line"></div>
            <div class="menuBlock" v-for="(item,index) in menu" @click="changeMenu(index)" v-bind:class='{bg:index==isactive}'>
                <img class="menuImg" :src="item.img">
                {{item.name}}
            </div>
            <div class="defind">技术支持：银信博荣</div>
        </div>
        <HeaderComponent></HeaderComponent>
        <div class="layout-main">
            <div class="layout-main-right">
                <div class="layout-content">
                    <div class="layout-content-main">
                        <router-view ref="contentView"></router-view>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>
<script>
    import {mapGetters, mapActions, mapState} from 'vuex'
    import md5 from 'js-md5'
    import HeaderComponent from '@/components/header.vue'

    export default {

        data() {
            return {
                loading: false,
                menus: [],
                menu: [
                    {name:'首页',img:'/static/img/home.png'},
                    {name:'疫苗柜运行监控',img:'/static/img/monitor.png'},
                    {name:'疫苗柜库存监控',img:'/static/img/stock.png'},
                    {name:'上报温度查询',img:'/static/img/alarm.png'},
                    {name:'出入库记录',img:'/static/img/inout.png'}
                ],
                isactive:0
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                // isactive:'isactive'
            })
        },
        components: {
            HeaderComponent,
        },
        methods: {
            // ...mapActions({
            //     CurrentMenu: 'setCurrentMenu',
            // }),
            changeMenu(index){
                this.isactive = index;
                if(index==0){  this.$router.push('/main'); }
                if(index==1){  this.$router.push('/monitor'); }
                if(index==2){  this.$router.push('/stock/stock'); }
                if(index==3){  this.$router.push('/temperature'); }
                if(index==4){  this.$router.push('/inout/inout'); }
            },
        },
        mounted() {
            this.isactive=this.$route.name
            // this.$api.get('/user/current').then((result) => {console.log("VVV9-----------"+JSON.stringify(result.data));
            //     this.menus = result.data.permission.children;
            //     this.menus.unshift({
            //         expand: true,
            //         icon: "",
            //         pid: null,
            //         sort: 0,
            //         url:'/homePage',
            //         title: "首页"
            //     });
            //     this.saveUser(result.data);
            //     this.setCurrentMenu(this.menus);
            //     sessionStorage.setItem('menus', JSON.stringify( this.menus) );
            //     sessionStorage.setItem('user', JSON.stringify( result.data) );
            // });
            // if (this.$route.path == '/') {
            //     this.$router.push('/main');
            // }
        }
    }
</script>
<style lang="less" scoped>
    @import "~@/style/layout.less";
</style>
