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
.loginform {
  height: 297px;
}
.bg1 {
  background-image: url("/static/img/loginp1.png");
  background-size: 51px 43px;
  width: 51px;
  height: 43px;
  position: absolute;
  top: 44px;
  right: 30px;
}
.bg2 {
  background-image: url("/static/img/loginp2.png");
  background-size: 134px 210px;
  width: 134px;
  height: 210px;
  position: absolute;
  left: 69px;
  bottom: 45px;
}
.layout {
  display: flex;
  width: 100%;
  height: 100%;
  background-image: url("/static/img/LoginBack.png");
  background-size: 100% 100%;
}
.header {
  align-items: center;
  justify-content: center;
  display: flex;
  // flex-direction: column;
  position: absolute;
  flex-wrap: wrap;
  width: 100%;
  top: 41px;
}
.login {
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  display: flex;
  position: absolute;
  bottom: 127px;
  width: 100%;
}
.item {
  display: flex;
  align-items: inherit;
  justify-content: inherit;
  width: 100%;
}
.tab {
  display: flex;
  padding-bottom: 38px;
}
.tab p {
  cursor: pointer;
  font-size: 19px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(62, 73, 85, 1);
}
.tabactive {
  color: rgba(42, 122, 255, 1) !important;
}
.main {
  min-height: 658px;
  min-width: 402px;
  margin: 60px 112px 82px 112px;
  position: relative;
  left: 0px;
  top: 0px;
  width: 100%;
  background: rgba(255, 255, 255, 1);
}
.logo {
  width: 189px;
  height: 25px;
}
.logoinfo {
  padding-top: 13px;
  font-size: 19px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(62, 73, 85, 1);
}
.title {
  padding-top: 27px;
  font-size: 32px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(201, 211, 237, 1);
}
</style>