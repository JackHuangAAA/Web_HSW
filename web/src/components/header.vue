<template>
    <div>
        <header class="layout-nav">
            <div class="layout-nav-wrap">
                <div class="layout-nav-title">智慧门诊疫苗接种应用系统</div>
                <div class="layout-nav-profile">
                    <Dropdown>
                        <div class="layout-avatar">
                            <img src="/static/img/user.png" />
                        </div>
                        <DropdownMenu slot="list" style="width:110px;">
                            <DropdownItem @click.native="setting">
                                <div style="display: inline-block;vertical-align: middle;"><img src="/static/img/passwd.png"></img>
                                </div>
                                <div style="display: inline-block;margin-left: 12px;margin-right: 4px;">修改密码</div>
                            </DropdownItem>
                            <DropdownItem @click.native="logout">
                                <div style="display: inline-block;vertical-align: middle;"><img src="/static/img/logout.png"></img>
                                </div>
                                <div style="display: inline-block;margin-left: 12px;margin-right: 4px;">退出登录</div>
                            </DropdownItem>
                        </DropdownMenu>
                    </Dropdown>
                    <div class="layout-welcome">
                        {{ user ? user.name : '--'}}
                    </div>
                </div>
            </div>
        </header>
        <!--设置-->
        <Modal v-model="setShow" title="修改密码" width="400px" :closable="false" :mask-closable="false" :footerHide="true">
            <Form ref="frmSetting" :model="frmSetting" :rules="ruleValidate" :label-width="100">
                <FormItem label="请输入旧密码:" prop="password">
                    <Input type="password" v-model="frmSetting.password" placeholder=""></Input>
                </FormItem>
                <FormItem label="请输入新密码:" prop="newpassword">
                    <Input type="password" v-model="frmSetting.newpassword" placeholder=""></Input>
                </FormItem>
                <FormItem label="请确认新密码:" prop="newpassword1">
                    <Input type="password" v-model="frmSetting.newpassword1" placeholder=""></Input>
                </FormItem>
            </Form>
            <div align="right">
                <Button @click="cancelSetting('frmSetting')" style="margin-right:10px;">取 消</Button>
                <Button type="primary" @click="saveSetting('frmSetting')" >保 存</Button>
            </div>
        </Modal>
    </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex'
import md5 from 'js-md5'

export default {
    data() {
        const checkOldPwd = (rule, value, callback, source, options) => {
            if (value == '') {
                return callback(new Error('请输入密码'));
            } else if (md5(value).toUpperCase() != this.user.password) {
                return callback(new Error('输入旧密码不正确!'));
            } else {
                callback();
            }
        };
        const checkPwd = (rule, value, callback, source, options) => {
            if (value == '') {
                return callback(new Error('请再次输入密码'));
            } else if (value != this.frmSetting.newpassword) {
                return callback(new Error('两次密码不一致'));
            } else {
                callback();
            }
        };
        return {
            setShow: false,
            frmSetting: {
                //name: '',
                //phone: '',
                password: '',
                newpassword: '',
                newpassword1: ''
            },
            ruleValidate: {
                /*name: [
                    { required: true, message: '用户名称不能为空', trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '移动电话不能为空', trigger: 'blur' },
                    { min: 11, message: '移动电话号超短', trigger: 'blur' },
                    { max: 11, message: '移动电话号超长', trigger: 'blur' },
                    {
                        validator(rule, value, callback, source, options) {
                            var errors = [];
                            if (!/^[0-9]{11}$/.test(value)) {
                                callback('手机号输入错误');
                            }
                            callback(errors);
                        }
                    }
                ],*/
                password: [
                    { required: true, message: '请输入旧密码', trigger: 'blur' },
                    { validator: checkOldPwd }
                ],
                newpassword: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                    { min: 6, message: '请输入最少6位' }
                ],
                newpassword1: [
                    { required: true, message: '请再次输入密码', trigger: 'blur' },
                    { min: 6, message: '请输入最少6位' },
                    { validator: checkPwd }
                ]
            }
        }
    },
    computed: {
        ...mapGetters({
            user: 'user'
        })
    },
    components: {

    },
    methods: {
        ...mapActions({
            saveUser: 'saveUser',
        }),
        logout: function() {
            this.$api.get('/user/logout').then(() => {
                this.saveUser(null);
                window.location.href = "/";
            });
        },
        setting: function() {
            this.frmSetting = {
                password: '',
                newpassword: '',
                newpassword1: ''
            };
            this.setShow=true;
        },
        saveSetting: function(name) {
            let sets = { id: this.user.id };
            sets.password = md5(this.frmSetting.newpassword).toUpperCase();
            this.$refs[name].validate((valid) => {
                if (valid) {
                    this.$api.post('/user/modifyUser', sets).then(rsp => {
                        //修改密碼后重新存入緩存currentUser
                        this.$Message.success('修改密码成功!');
                        this.logout();
                        this.$refs[name].resetFields();
                        this.setShow=false;
                    });
                }
            })
        },
        cancelSetting: function(name) {
            this.$refs[name].resetFields();
            this.setShow=false;
        }
    },
    mounted() {

    }
}
</script>
<style lang="less" scoped>
    @import "~@/style/layout.less";
</style>

