<template>
  <div class="userinfoType">
    <div class="headportait">
      <img src="/static/img/images/userph1.png">
    </div>
    <div class="username">
      {{username}}
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'userinfo',
  data () {
    return {

    }
  },
  computed: {
    ...mapGetters(['username'])
  },
  created () {
  },
  methods: {

  },
}
</script>

<style lang="less" scoped>
.userinfoType {
  display: flex;
  flex-wrap: nowrap;
}
.username {
  margin: auto 0px auto 14px;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
}
.headportait {
  margin: auto;
  width: 32px;
  height: 32px;
}
</style>