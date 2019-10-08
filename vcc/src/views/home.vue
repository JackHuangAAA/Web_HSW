<template>
    <div class="layout">
        <!-- 导航头部 -->
        <header class="layout-nav">
            <div class="layout-nav-wrap">
                <div class="layout-logo">
                    <img src="/static/img/logo.png"/>
                </div>
                <div class="layout-menu">
                    <div v-for="(menu, index) in menus" :key="index" @click="selectMenu(menu)" class="layout-menu-button" :class="currentPath == menu.url? 'active':''">
                        <div>
                            <img :src="'/static/img/' + menu.icon +'.png'">
                        </div>
                        <span>{{menu.name}}</span>
                    </div>
                </div>

                <div class="layout-nav-profile">
                    <div class="layout-welcome">
                        欢迎，【{{ user ? user.name : ''}}】
                    </div>
                    <Dropdown>
                        <div class="layout-avatar">
                            <img src="/static/img/user_head.png" />
                        </div>
                    <DropdownMenu slot="list" style="width:110px;">
                        <DropdownItem @click.native="setting">
                            <div style="display: inline-block;vertical-align: middle;"><img src="/static/img/passwd.png" ></img></div>
                            <div style="display: inline-block;margin-left: 12px;margin-right: 4px;">修改密码</div>
                        </DropdownItem>
                        <DropdownItem @click.native="logout">
                            <div style="display: inline-block;vertical-align: middle;"><img src="/static/img/logout.png" ></img></div>
                            <div style="display: inline-block;margin-left: 12px;margin-right: 4px;">退出登录</div>
                        </DropdownItem>
                    </DropdownMenu>
                    </Dropdown>
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
        <!--设置-->
        <Modal ref="settingModal" title="密码修改" :width="534" :height="300" :closable="false" :mask-closable="false" :footerHide="true">
            <Form ref="frmSetting" :model="frmSetting" :rules="ruleValidate" :label-width="100" slot="content">
                <FormItem label="请输入旧密码" prop="password">
                    <Input type="password" v-model="frmSetting.password" placeholder=""></Input>
                </FormItem>
                <FormItem label="请输入新密码" prop="newpassword">
                    <Input type="password" v-model="frmSetting.newpassword" placeholder=""></Input>
                </FormItem>
                <FormItem label="请确认新密码" prop="newpassword1">
                    <Input type="password" v-model="frmSetting.newpassword1" placeholder=""></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button @click="cancelSetting('frmSetting')" style="margin-right:10px; height: 36px; width:88px;">取 消</Button>
                <Button type="primary" @click="saveSetting('frmSetting')" style="height: 36px; width:88px;">保 存</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
    import {mapGetters, mapActions, mapState} from 'vuex'
    import md5 from 'js-md5'
    import routerConfig from '@/router'

    export default {

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
                userPic: 'tuichu',
                //activeMenu: null,
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
                device: 'device'
            })
        },
        components:{},
        watch:{
            '$route.path':function(n,o){
                this.currentPath = n
            }
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
                this.saveUser(result.data);
            });
            //加载当设备信息
            this.$device.getDeviceId().then(res => {
                this.$api.get(`/device/queryDeviceByCondition?code=${res.deviceId}`).then((response) => {
                    console.log('response.data====>'+JSON.stringify(response.data));
                    this.saveDevice(response.data);
                });
            });
            if(this.$route.path == '/'){
                this.$router.push('/main');
            }
            console.log('====currentDevice=========%j',this.user)
            console.log('====currentDevice=========%j',this.device)
        }
    }
</script>
