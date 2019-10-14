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
import { mapGetters } from "vuex";
export default {
  name: "userinfo",
  data() {
    return {};
  },
  computed: {
    userinfo(){
      return this.$store.getters.user
    },
    username(){
      if(this.userinfo != null) return this.userinfo.name
      else return ''
    }
  },
  created() {
    this.init()
  },
  methods: {
    init(){
    }
  }
};
</script>

<style lang="less" scoped>
@import "./userinfo.less";
</style>