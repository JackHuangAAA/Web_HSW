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
                        <keep-alive>
                            <router-view ref="contentView"></router-view>
                        </keep-alive>
                        <!-- <Spin fix v-show="loading">
                            <Icon type="load-c" size=18 class="demo-spin-icon-load"></Icon>
                            <div>加载中...</div>
                        </Spin> -->
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
                    //{name:'报废',img:'/static/img/delete.png'},
                    {name:'出入库记录',img:'/static/img/inout.png'}
                ],
                isactive:0
            }
        },
        computed: {
            ...mapGetters({
                currentMenu: 'currentMenu',
                user: 'user'
            })
        },
        components: {
            HeaderComponent,
        },
        methods: {
            ...mapActions({
                setCurrentMenu: 'setCurrentMenu',
                saveUser: 'saveUser'
            })
        },
        mounted() {
            this.$api.get('/user/current').then((result) => {console.log("VVV9-----------"+JSON.stringify(result.data));
                this.menus = result.data.permission.children;
                this.menus.unshift({
                    expand: true,
                    icon: "",
                    pid: null,
                    sort: 0,
                    url:'/homePage',
                    title: "首页"
                });
                this.saveUser(result.data);
                this.setCurrentMenu(this.menus);
                sessionStorage.setItem('menus', JSON.stringify( this.menus) );
                sessionStorage.setItem('user', JSON.stringify( result.data) );
            });
            if (this.$route.path == '/') {
                this.$router.push('/main');
            }
        }
    }
    /*export default {

        data () {
            const checkOldPwd = (rule, value, callback, source, options) => {
                if(value == '') {
                    return callback(new Error('请输入密码'));
                } else if(md5(value).toUpperCase() != this.user.password) {
                    return callback(new Error('输入旧密码不正确!'));
                } else {
                    callback();
                }
            };
            const checkPwd = (rule, value, callback, source, options) => {
                if(value == '') {
                    return callback(new Error('请再次输入密码'));
                } else if(value != this.frmSetting.newpassword) {
                    return callback(new Error('两次密码不一致'));
                } else {
                    callback();
                }
            };
            return {
                currentPath: null,
                version: this.$config.version,
                showSet:false,
                loading: false,
                menus: [],
                title: this.$config.appName,
                //userPic: 'tuichu',
                frmSetting: {
                    name: '',
                    phone: '',
                    password: '',
                    newpassword: '',
                    newpassword1: ''
                },
                ruleValidate: {
                    name: [
                        { required: true, message: '用户名称不能为空', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: '移动电话不能为空', trigger: 'blur'},
                        {min: 11, message: '移动电话号超短', trigger: 'blur'},
                        {max: 11, message: '移动电话号超长', trigger: 'blur'},
                        {
                            validator(rule, value, callback, source, options) {
                                var errors = [];
                                if (!/^[0-9]{11}$/.test(value)) {
                                    callback('手机号输入错误');
                                }
                                callback(errors);
                            }
                        }
                    ],
                    password: [
                        { required: true, message: '请输入旧密码', trigger: 'blur'},
                        { validator: checkOldPwd}
                    ],
                    newpassword: [
                        { required: true, message: '请输入新密码', trigger: 'blur'},
                        { min: 6, message:'请输入最少6位'}
                    ],
                    newpassword1:  [
                        { required: true, message: '请再次输入密码', trigger: 'blur'},
                        { min: 6, message:'请输入最少6位'},
                        { validator: checkPwd}
                    ]
                }
            }
        },
        computed: {
            ...mapGetters({
                user: 'user',
                currentMenu: 'currentMenu'
            })
        },
        components:{
            HeaderComponent,
            LeftComponent
        },
        watch:{
            '$route.path':function(n,o){
                this.currentPath = n
            }
        },
        methods: {
            ...mapActions({
                saveUser: 'saveUser',
                setCurrentMenu: 'setCurrentMenu'
            }),
            logout: function () {
                this.$api.get('/user/logout').then(()=>{
                    this.saveUser(null);
                    window.location.href="/";
                });
            },
            //菜单切换
            selectMenu: function (item) {
                this.$router.push(item.url)
            },
            setting: function () {
                this.frmSetting = {
                    password: '',
                    newpassword: '',
                    newpassword1: ''
                };
                this.$refs.settingModal.show();
            },
            saveSetting: function (name) {
                let me = this;
                let sets = {id:me.user._id};
                sets.name = '超级管理员';
                sets.password = md5(me.frmSetting.newpassword).toUpperCase();
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$api.post('/user/updatePassword', sets).then(rsp => {
                            //修改密碼后重新存入緩存currentUser
                            this.$Message.success('修改密码成功！');
                            this.logout();
                            this.$refs[name].resetFields();
                            this.$refs.settingModal.close();
                        });
                    }
                })
            },
            cancelSetting: function (name) {
                this.$refs[name].resetFields();
                this.$refs.settingModal.close();
            }
        },

        updated() {
            this.$nextTick(()=> {
                if(this.$refs.menuView){
                    this.$refs.menuView.updateOpened();
                }
            });
        },
        mounted(){
            this.$api.get('/user/current').then((result) => {
                this.menus.unshift({
                    expand: true,
                    icon: "",
                    pid: null,
                    sort: 0,
                    url:'/homePage',
                    title: "首页"
                });
                this.saveUser(result.data);
                this.menus = result.data;
                if(this.$route.path == '/'){
                    this.$router.push('/main');
                }
            });
        }
    }*/
</script>
<style lang="less" scoped>
    @import "~@/style/layout.less";
</style>
