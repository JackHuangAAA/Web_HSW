<template>
<div class="page">
   <div class="login-main">
        <div class="header">
            <div class="wrap">
                <div class="image">
                    <img src="/static/img/ETHINK-lan.png"/>
                </div>
                <div class="version">
                    版本号：{{version}}
                </div>
            </div>
        </div>
        <div class="content">
            <div class="wrap">
                <div class="system-name">
                    <span>欢迎登陆银信博荣智慧疫苗物联平台</span>
                </div>
                <div class="login-img">
                    <img src="/static/img/login.png"/>
                </div>
                <div class="login">
                    <div class="title"><span>请登录</span></div>
                    <Form ref="loginFrm" :model="loginFrm"  class="login-form"><!--:rules="loginFrmRule"-->
                        <FormItem><!--prop="user"-->
                            <Input class="login-input" ref="userInput"  type="text" v-model="loginFrm.user" size="large" placeholder="用户名" @on-change="userChange" required autocomplete="on">
                            <Icon type="ios-person-outline" slot="prepend"></Icon>
                            </Input>
                            <div class="errortip" v-if="userErrShow"><span>{{usererror}}</span></div>
                        </FormItem>
                        <FormItem><!--prop="password"-->
                            <Input class="login-input" type="password" icon="locked" v-model="loginFrm.password" size="large" placeholder="密码" @on-change="pwdChange" @on-enter="login(loginFrm)">
                            <Icon type="ios-lock-outline" slot="prepend"></Icon>
                            </Input>
                            <div class="errortip" v-if="pwdErrShow"><span>{{pwderror}}</span></div>
                            <Checkbox style="padding: 10px 0px 0px 21px;" v-model="rember" @on-change="save()">记住密码</Checkbox>
                        </FormItem>
                        <!-- <FormItem>
                            <Button type="primary" class="btn" @click="login(loginFrm)" @on-enter="login(loginFrm)" long>登录</Button>
                        </FormItem> -->
                    </Form>
                    <Button type="primary" class="btn" @click="login(loginFrm)" @on-enter="login(loginFrm)" >登录</Button>
                </div>
            </div>
        </div> 
    </div>

    <div class="footer">
        <div>
            <div class="address">地址：浙江省杭州市余杭区文一西路998号4栋903室</div>
            <div class="contact">联系电话：0571-89266953 &nbsp;&nbsp;&nbsp;&nbsp; 服务邮箱：service@ethinkbank.com</div>
        </div>
        <hr class="divide"/>
        <div class="copyright">
            <span>Copyright © 2018 <a href="http://www.ethinkbank.com" target="_blank">浙江银信博荣电子科技股份有限公司</a> All rights reserved.</span>
        </div>
    </div>
</div>  
</template>
<style lang="less" scoped>
    @import "../style/vars";
    @header-height: 60px;

    .page{
        height:100%; 
        min-width: @viewport-width;
        width:100%
    }
    .wrap{
        width: 1200px;
        margin: 0 auto;
    }
    .login-main {
        width: 100%;
        height: calc(100% - 156px);
        min-height: 768px;
        .header {
            position:relative;
            height: @header-height;
            line-height: @header-height;
            background-color: #ffffff;
            z-index: 1;
            .image {
                margin: 0;
                width: 140px;
                height: @header-height;
                line-height: @header-height;
                img{
                    vertical-align: middle;
                    height: 20px;
                    width: 140px;
                }
            }
            .version{
                vertical-align: top;
                float: right;
                margin-top: -@header-height;
                margin-right: 80px;
                line-height: @header-height;
                height: @header-height;
                font-weight: bold;
                font-size: 14px;
                color: #4f5f6f;
            }
        }
        .content {
            width: 100%;
            height: 100%;
            margin-top:  -@header-height;
            background-color: #387BD1;
            .system-name {
                text-align:center;
                font-size: 28px;
                color: white;
                padding-top: @header-height + 80px;
            }
            .login-img {
                float: left;
                width: 574px;
                height: 549px;
                padding-top: 50px;
                margin-left: 60px;
                img{
                    height: 100%;
                    width: 100%;
                }
            }
            .login {
                float: right;
                background-color: #ffffff;
                margin-top: 106px;
                margin-right: 64px;
                border-radius: 10px;
                height: 324px;
                width: 310px;
                .title{
                    text-align:center;
                    font-size: 16px;
                    color: #4F5F6F;
                    padding: 32px 0px 28px 0px;
                    font-weight: bold;
                }
                .btn{
                    width:270px;
                    height:36px;
                    margin-left: 21px;
                    margin-top: 30px;
                }
                form{
                    height: 150px;
                    overflow: hidden;
                }
            }
            .login-input {
                width: 290px;
                padding-left: 20px;
            }
        }
        .errortip {
            padding-left: 20px;
            height:10px;
            font-size: 12px;
            color: #FF0000;
        }
    }
    .footer {
        text-align:center;
        color: #bbbbbb;
        font-size: 12px;
        margin-top: 0;
        height: 156px;
        width: 100%;
        background-color: #1C1C1C;

        .address{
            padding-top: 40px;
            line-height: 12px;
        }
        .contact{
            margin-top: 10px;
            line-height: 12px;
        }
        .divide{
            margin-top: 10px;
            height:1px;
            border:none;
            border-top:1px solid #555555;
        }

        .copyright{
            text-align:center;
            margin-top: 10px;
        }
    }

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
                                this.$Message.error(`用户${data.user}不存在`);
                            }else if(result.code == '0003'){
                                this.$Message.error(`密码错误`);
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
            if(this.loginUser){
                this.$router.push('/');
            }
            if(localStorage.user != null){
                this.loginFrm.user = localStorage.user;
                this.rember = true;
            }
            if(localStorage.password != null){
                this.loginFrm.password = localStorage.password;
            }
            this.$refs.userInput.focus();
        }
    }

</script>
