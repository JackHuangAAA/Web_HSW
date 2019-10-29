<template>
  <div class="lform">

    <Row class="loginform-row">
      <Input v-model="user" autocomplete="off" placeholder="账号" @on-blur="accountBlur">
             <img src="/static/img/user.png" class="icon" slot="suffix">
      </Input>
      <div class="loginform-notice" :class="{'loginform-notice-hide':account}">账户不能为空</div>
    </Row>
    
    <Row style="padding-top:24px;" class="loginform-row">
      <Input v-model="pwd" type="password" autocomplete="off" placeholder="密码" @on-blur="passwordBlur">
        <img src="/static/img/pwd.png" class="icon" slot="suffix">
      </Input>
      <div class="loginform-notice" :class="{'loginform-notice-hide':state?password:state}">{{state?'密码不能为空':'账户或密码不正确'}}</div>
    </Row>

    <!--<Row style="padding-top:19px">
      <Checkbox style="font-size:16px;" v-model="rember">记住密码</Checkbox>
    </Row>-->
    <Row style="padding-top:34px">
      <Button style="height:53px;font-size:20px;margin-bottom:7px;" type="primary" @click="handleSubmit()" long>登录</Button>
    </Row>
  </div>
</template>

<script>
//import { Storages } from "@/libs/util.js";
import config from "@/config";
export default {
  name: "loginform",
  data() {
    return {
      value: "",
      rember: false,
      user: "",
      pwd: "",
      account:true,
      password:true,
    };
  },
  props:{
    state:{
      type:Boolean,
      default:true
    }
  },
  watch: {
    rember(value) {
      if (!value) {
        this.CleanUser();
      }
    },
  },
  computed:{
  },
  methods: {
    SaveUser() {
      //Storages.SaveStorage("user", this.user);
      //Storages.SaveStorage("password", this.pwd);
    },
    CleanUser() {
      //Storages.CleanStorage("user");
      //Storages.CleanStorage("password");
    },
    GetUser() {
      //this.user = Storages.GetStorage("user");
      //this.pwd = Storages.GetStorage("password");
    },
    //关闭指纹登录的指纹查找方法
    un_fingerSearch(){
      this.$device.un_fingerSearch();
    },
    accountBlur(){
      if(!this.user){
        this.account=false;
      }else{
        this.account=true;
      }
    },
    passwordBlur(){
      if(!this.pwd){
        this.password=false;
      }else{
        this.password=true;
      }
    },
    handleSubmit() {
      if(!this.user && !this.pwd){
        this.account=false;
        this.password=false;
        return
      }else if(!this.user){
        this.account=false;
        return
      }else if(!this.pwd){
        this.password=false;
        return
      }
      if (this.rember) {
        this.SaveUser();
      }
      let form = {
        code: this.user,
        password: this.pwd
      };
      this.$emit("Submit", form);
    }
  },
  mounted() {
    /*if (Storages.GetStorage("user") != null) {
      this.rember = true;
      this.GetUser();
    }*/
    if(config.env != 'development'){
      this.un_fingerSearch();
    }
  }
};
</script>

<style lang="less">
@import "~@/style/loginform.less";
</style>