
<template>
  <div>
    <div class="tip">
      <img src="static/img/404.png"></img>
      <div class="info">
        <div class="title">哎呀，页面找不到了！</div>
        <p>很抱歉，你来晚了一步，它已经任性的消失了...</p>
        <p>你要坚强些，继续发掘别的页面去吧</p>
      </div>
    </div>
  </div>
</template>
<style lang="less" scoped>
.tip {
  padding-top: 140px;
  text-align: center;
  .info {
    display: inline-block;
    vertical-align: top;
    margin-left: 60px;
    margin-top: 60px;
    .title {
      font-size: 24px;
      font-weight: bold;
      color: #ff9000;
      margin-bottom: 20px;
    }
    p {
      font-size: 20px;
      color: #333;
    }
  }
}
</style>
<script>
export default {
  data () {
    return {
    }
  },
  mounted () {

  }
}
</script>
