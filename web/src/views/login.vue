<template>
<div class="page">
    <div class="login-main">
        <div class="login">
            <div class="login-title">Welcome back</div>
            <img src="/static/img/ETHINK-LOGO.png" class="logo"/>
            <div class="login-info">智慧门诊疫苗接种应用系统</div>
            <Form ref="loginFrm" :model="loginFrm"  class="login-form">
                <FormItem>
                    <!-- <Input class="login-input" ref="userInput"  type="text" v-model="loginFrm.user" size="large" placeholder="账户" @on-change="userChange">
                        <Icon type="ios-person-outline" slot="suffix"></Icon>
                    </Input> -->
                    <Input placeholder="账号" v-model="loginFrm.user" @on-change="userChange" class="login-input" autocomplete="off" size="large">
                        <img src="/static/img/account.png" slot="suffix" class="login-icon" />
                    </Input>
        
                    <div class="errortip" v-if="userErrShow"><span>{{usererror}}</span></div>
                </FormItem>
                <FormItem>
                    <!-- <Input class="login-input" type="password" icon="locked" v-model="loginFrm.password" size="large" placeholder="密码" @on-change="pwdChange" @on-enter="login(loginFrm)">
                        <Icon type="ios-lock-outline" slot="suffix"></Icon>
                    </Input> -->
                    <Input placeholder="密码" type="password" size="large"  v-model="loginFrm.password" @on-change="pwdChange" @on-enter="login(loginFrm)" autocomplete="off" class="login-input">
                        <img src="/static/img/pwd.png" slot="suffix" class="login-icon" />
                    </Input>
                    <div class="errortip" v-if="pwdErrShow"><span>{{pwderror}}</span></div>
                </FormItem>
                <Checkbox style="padding: 13px 0px; font-size:19px" v-model="rember" @on-change="save()">记住密码</Checkbox>
            </Form>
            <Button type="primary" class="btn" @click="login(loginFrm)" @on-enter="login(loginFrm)" >登录</Button>
        </div>
    </div>
</div>  
</template>
<style lang="less" scoped>
    @import "../style/vars";
    @import '../style/color.less';
    @import"../style/login.less";
</style>
<script>
    import md5 from 'js-md5'
    import {mapGetters} from 'vuex'

    export default{
        data(){
            return{
                usererror: '',
                pwderror: '',
                userErrShow: false,
                pwdErrShow: false,
                showLogin: false,
                loginFrm:{
                    user: '',
                    password: '',
                },
                title: this.$config.appName,
                rember: false,
                version: this.$config.version
                //登录框校验
                /*loginFrmRule:{
                    password: [
                        { required: true, message: '密码不能为空', trigger: 'blur' }
                    ],
                    user: [
                        { required: true, message: '用户名不能为空', trigger: 'blur'}
                    ]
                }*/
            }
        },        
        computed: {
            ...mapGetters({
                loginUser: 'user'
            }),

        },
        methods: {
            userChange:function () {
                this.usererror = "";
                this.userErrShow = false;
                if(localStorage.user == this.loginFrm.user){
                    this.loginFrm.user = localStorage.user;
                    this.loginFrm.password = localStorage.password;
                    this.rember = true;
                }else{
                    this.loginFrm.password = '';
                    this.rember = false;
                }
            },
            pwdChange:function () {
                this.pwderror = "";
                this.pwdErrShow = false;
            },
            //登录
            login: function(data){
                this.usererror = "";this.pwderror ="";
                this.userErrShow = false;this.pwdErrShow = false;
                if(_.isEmpty(this.loginFrm.user) && _.isEmpty(this.loginFrm.password)){
                    this.usererror = "用户名不能为空";
                    this.pwderror = "密码不能为空";
                    this.userErrShow = true;
                    this.pwdErrShow = true;
                    return;
                }
                if(_.isEmpty(this.loginFrm.user)){
                    this.usererror = "用户名不能为空";
                    this.userErrShow = true;
                    return;
                }
                if(_.isEmpty(this.loginFrm.password)){
                    this.pwderror = "密码不能为空";
                    this.pwdErrShow = true;
                    return;
                }
                //this.$refs['loginFrm'].validate((valid) => {
                    //if (valid) {
                        this.$api.post('/user/login',{code: this.loginFrm.user,password: md5(this.loginFrm.password).toUpperCase()}).then((result)=>{
                            if(result.code == '0002'){
                                this.usererror = `用户${data.user}不存在`;
                                this.userErrShow = true;
                            }else if(result.code == '0003'){
                                this.pwderror = `密码错误`;
                                this.pwdErrShow = true;
                            }else if(result.code == '0000'){
                                this.$router.push('/');
                            }
                        });
                /*} else {
                        this.$Message.error('表单验证失败!');
                    }*/
                //});
            },

            //记住密码
            save: function(){
                if(this.rember){
                    localStorage.user = this.loginFrm.user;
                    localStorage.password = this.loginFrm.password;
                }else{
                    delete localStorage.user;
                    delete localStorage.password;
                }
            }
        },

        mounted(){
            // if(this.loginUser){
            //     this.$router.push('/');
            // }
            // if(localStorage.user != null){
            //     this.loginFrm.user = localStorage.user;
            //     this.rember = true;
            // }
            // if(localStorage.password != null){
            //     this.loginFrm.password = localStorage.password;
            // }
            // this.$refs.userInput.focus();
        }
    }

</script>
