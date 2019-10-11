<template>
  <div class="userinfoType">
    <div class="headportait">
      <img src="~@/assets/userph1.png">
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
    username(){
      if(this.$store.getters.userinfo != null) return this.$store.getters.userinfo.username
      return ''
    }
  },
  created() {},
  methods: {}
};
</script>

<style lang="less" scoped>
@import "./userinfo.less";
</style>