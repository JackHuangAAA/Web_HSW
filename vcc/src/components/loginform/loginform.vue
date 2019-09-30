<template>
  <div class="lform">
    <Row>
      <Input v-model="user.user"
             autocomplete="off"
             placeholder="账号"><img :src="userpng"
           class="icon"
           slot="suffix"></Input>
    </Row>
    <Row style="padding-top:24px;">
      <Input v-model="user.pwd"
             type="password"
             autocomplete="off"
             placeholder="密码">
      <img :src="passpng"
           class="icon"
           slot="suffix"></Input>
    </Row>
    <Row style="padding-top:19px">
      <Checkbox style="font-size:16px;"
                v-model="rember">记住密码</Checkbox>
    </Row>
    <Row style="padding-top:34px">
      <Button style="height:53px;font-size:20px;margin-bottom:7px;"
              type="primary"
              long>登录</Button>
    </Row>
  </div>
</template>

<script>
import userpng from '@/assets/user.png';
import passpng from '@/assets/pwd.png'
export default {
  name: 'loginform',
  data () {
    return {
      userpng,
      passpng,
      value: '',
      rember: false,
      user: {
        user: '',
        pwd: ''
      }
    }
  }
}
</script>

<style lang="less">
.icon {
  margin-top: 15px;
  margin-bottom: 17px;
}
.lform .ivu-input {
  width: 372px;
  height: 53px;
  background: rgba(255, 255, 255, 1);
  box-shadow: 0px 2px 6px 0px rgba(107, 197, 159, 0.35);
  border-radius: 4px;
  font-size: 18px;
  color: rgba(79, 95, 111, 1);
}
</style>