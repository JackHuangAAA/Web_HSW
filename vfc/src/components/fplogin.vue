<template>
  <div @click="disclick()" style="position:relative;height:100%">
    <img src="/static/img/fplogin.png" class="fpbg">
    <P class="fingerTip">使用指纹一键登录</P>
  </div>
</template>

<script>
export default {
  name: "fplogin",
  data() {
    return {};
  },
  methods: {
    disclick() {
      this.$emit("click");
    }
  }
};
</script>

<style lang="less">
  @import "~@/style/fplogin.less";
</style>