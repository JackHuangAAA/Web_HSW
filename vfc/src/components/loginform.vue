<template>
  <div class="lform">

    <Row>
      <Input v-model="user" autocomplete="off" placeholder="账号">
             <img src="/static/img/user.png" class="icon" slot="suffix">
      </Input>
    </Row>
    
    <Row style="padding-top:24px;">
      <Input v-model="pwd" type="password" autocomplete="off" placeholder="密码">
      <img src="/static/img/pwd.png" class="icon" slot="suffix"></Input>
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
export default {
  name: "loginform",
  data() {
    return {
      value: "",
      rember: false,
      user: "",
      pwd: ""
    };
  },
  watch: {
    rember(value) {
      if (!value) {
        this.CleanUser();
      }
    }
  },
  mounted() {
    /*if (Storages.GetStorage("user") != null) {
      this.rember = true;
      this.GetUser();
    }*/
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
    handleSubmit() {
      if (this.rember) {
        this.SaveUser();
      }
      let form = {
        code: this.user,
        password: this.pwd
      };
      this.$emit("Submit", form);
    }
  }
};
</script>

<style lang="less">
@import "~@/style/loginform.less";
</style>