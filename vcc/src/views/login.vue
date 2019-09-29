<template>
  <div class="layout">
    <div class="main">
      <div class="bg1"></div>
      <div class="bg2"></div>
      <div class="header">
        <div class="logo item"><img src="/static/img/ETHINK-lan.png"></div>
        <div class="logoinfo item">银信疫苗接种平台</div>
        <div class="title item">welcome to login system</div>
      </div>
      <div class="login">
        <div class="tab item">
          <p :class="{'tabactive':login1}"
             @click="login('fp')">指纹登陆</p>
          <p :class="{'tabactive':login2}"
             style="padding-left:62px;"
             @click="login('up')">用户密码登录</p>
        </div>
        <div class="loginform item">
          <fplogin v-if="login1"></fplogin>
          <loginform v-if="login2"></loginform>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import loginform from '_c/loginform'
import fplogin from '_c/fplogin'
export default {
  components: {
    loginform,
    fplogin
  },
  data () {
    return {
      login1: true,
      login2: false
    }
  },
  methods: {
    login (type) {
      switch (type) {
        case 'fp':
          this.login1 = true;
          this.login2 = false;
          break;
        case 'up':
          this.login1 = false;
          this.login2 = true;
          break;
        default:
          break;
      }
    }
  }
}
</script>

<style lang="less">
@import "~@/style/main/login.less";
</style>