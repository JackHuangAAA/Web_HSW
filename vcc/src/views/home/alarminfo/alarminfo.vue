<template>
  <div style="height:100%">
    <div class="alarminfo-title">
      <img src="~@/assets/icon/alarm-yellow.png">
      <p>设备报警</p>
    </div>
    <div class="alarminfo-main">
      <div class="alarminfo-main-data">
        <div class="alarminfo-main-data-title">
          警示:接种柜温度异常
        </div>
        <div class="alarminfo-main-data-memo">
          接种柜温度过高,请及将温度调整至2-8℃范围内!
        </div>
      </div>
      <div class="alarminfo-main-null"
           v-if="isNull">
        <p>暂无数据</p>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'alarminfo',
  props: {
    alarmlist: {
      type: Array,
      default () {
        return []
      }
    }
  },
  data () {
    return {
      alarmtype: {

      }
    }
  },
  computed: {
    isNull () {
      return this.alarmlist.length != 0 ? false : true
    }
  },
  mounted () {
  },
}
</script>

<style lang="less">
@import "~@/style/color";
.alarminfo-main-data-title {
  color: #ff5e45;
  font-size: 16px;
  font-weight: bold;
}
.alarminfo-main-data-memo {
  font-size: 16px;
  font-weight: bold;
  margin-top: 12px;
  line-height: 40px;
}
.alarminfo-main {
  position: relative;
  width: 100%;
  height: ~"calc(100% - 40px)";
}
.alarminfo-main-data {
  position: absolute;
  display: flex;
  flex-direction: column;
  padding: 26px;
}
.alarminfo-main-null {
  position: absolute;
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  justify-content: center;
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