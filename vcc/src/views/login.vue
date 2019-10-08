<template>
  <div class="layout">
    <div class="login-main">
      <div class="bg1"></div>
      <div class="bg2"></div>
      <div class="login-header">
        <div class="logo item"><img :src="ETHINK"></div>
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
          <fplogin v-if="login1"
                   @click="test()"></fplogin>
          <loginform v-if="login2"
                     @Submit="checkUser"></loginform>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ETHINK from '@/assets/logo.png'
import loginform from '_c/loginform'
import fplogin from '_c/fplogin'
import { mapActions } from 'vuex'
export default {
  components: {
    loginform,
    fplogin
  },
  data () {
    return {
      login1: true,
      login2: false,
      ETHINK
    }
  },
  mounted () {
    this.getDeviceId()
  },
  methods: {
    getDeviceId () {
      let a = this.$device.getDeviceId()
      console.log(a)
    },
    ...mapActions(['saveUser', 'saveUserInfo']),
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
    },
    test () {
      let test = this.$device.finger('open').catch(err => {
        console.log(err)
      })
      console.log(test)
    },
    async checkUser (form) {
      let res = await this.$api.get('/zcy/checkUser', form)
      let check = res.data.check
      if (check) {
        let data = res.data
        await this.saveUser(form.code)
        await this.saveUserInfo(data)
        await this.$api.post('/user/modifyUserByCode', { code: data.code, name: data.name })
        this.$router.push({ name: 'home' })
      }
    }
  }
}
</script>

<style lang="less">
@import "~@/style/main/login.less";
</style>