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
  name: 'fplogin',
  data () {
    return {

    }
  },
  methods: {
    disclick () {
      this.$emit('click')
    }
  }
}
</script>

<style lang="less">
.fpbg {
  width: 228px;
  height: 160px;
  background-image: url("/static/img/images/fplogin.png");
}
.fpinfo {
  width: 153px;
  height: 19px;
  font-size: 19px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(201, 211, 237, 1);
}
</style>