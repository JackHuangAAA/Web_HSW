<template>
  <div style="height:100%">
    <div class="alarminfo-title">
      <img src="/static/img/icon/alarm-yellow.png">
      <p>设备报警</p>
    </div>
    <div class="alarminfo-main">
      <div class="alarminfo-main-data"></div>
      <div class="alarminfo-main-null">
        <p>暂无数据</p>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'alarminfo',
  data () {
    return {

    }
  }
}
</script>

<style lang="less">
@import "~@/style/color";
.alarminfo-main {
  position: relative;
  width: 100%;
  height: ~"calc(100% - 40px)";
}
.alarminfo-main-data {
  position: absolute;
  display: flex;
}
.alarminfo-main-null {
  position: absolute;
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  justify-items: center;
}
.alarminfo-main-null p {
  margin: auto;
  font-size: 18px;
  font-weight: 400;
  color: @gray;
}
.alarminfo-title {
  padding-top: 20px;
  width: 100%;
  display: flex;
}
.alarminfo-title p {
  font-size: 16px;
  font-weight: bold;
  color: @black;
}
.alarminfo-title img {
  margin-left: 26px;
  width: 20px;
  height: 20px;
}
</style>