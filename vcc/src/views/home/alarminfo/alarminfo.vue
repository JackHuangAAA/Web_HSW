<template>
  <div style="height:100%">
    <div class="alarminfo-title">
      <img src="~@/assets/icon/alarm-yellow.png">
      <p>设备报警</p>
    </div>
    <div class="alarminfo-main-null"
         v-if="isNull">
      <p>暂无数据</p>
    </div>
    <div v-else
         class="alarminfo-main">
      <div class="alarminfo-main-data">
        <div class="alarminfo-main-data-title">
          警示:{{alarmpart}}
        </div>
        <div class="alarminfo-main-data-memo">
          {{alarmsolution}}
        </div>
      </div>

    </div>

  </div>
</template>

<script>
export default {
  name: "alarminfo",
  props: {
    alarm: {
      type: Number,
      default() {
        return [];
      }
    }
  },
  data() {
    return {
      alarmtype: {}
    };
  },
  computed: {
    isNull() {
      return this.alarm === 0;
    },
    alarmpart() {
      return _static.alarminfo[this.alarm].part;
    },
    alarmsolution() {
      return _static.alarminfo[this.alarm].solution;
    }
  },
  mounted() {}
};
</script>

<style lang="less">
@import "./alarminfo.less";
</style>