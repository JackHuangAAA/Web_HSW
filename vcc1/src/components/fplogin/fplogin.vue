<template>
  <div @click="disclick()">
    <Row type="flex"
         justify="center">
      <div class='fpbg'></div>
    </Row>
    <Row type="flex"
         justify="center"
         style="margin-top:56px;">
      <p class='fpinfo'>使用指纹一键登录</p>
    </Row>
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
@import "./fplogin.less";
</style>