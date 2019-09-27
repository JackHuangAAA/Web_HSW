<template>
  <div class="alarm card">
    <div class="alarm-header">
      <div class="alarm-header-title">
        <p>报警记录</p>
      </div>
      <div class="alarm-header-info">
        <p>排序依照最近时间排序</p>
      </div>
    </div>
    <div class="alarm-main">
      <alarmcard :lists="lists"
                 :columns="columns"></alarmcard>
    </div>
  </div>
</template>

<script>
import alarmcard from "_c/main/alarmcard"
import columns from "./data/columns"
import testdata from "./data/testdata"
export default {
  name: 'alarm',
  components: {
    alarmcard
  },
  data () {
    return {
      columns: columns,
      lists: testdata
    }
  }
}
</script>

<style lang="less">
@import "~@/style/color";
.alarm {
  height: 100%;
  width: 100%;
}
.alarm-main {
  width: 100%;
  height: ~"calc(100% - 65px)";
  padding-left: 49px;
  padding-right: 44px;
}
.alarm-header {
  height: 65px;
  width: 100%;
  display: flex;
}
.alarm-header-title {
  margin: auto 0px 20px 49px;
}
.alarm-header-info {
  margin: auto 0px 20px 10px;
}
.alarm-header-title p {
  font-size: 18px;
  font-weight: bold;
  color: @black;
}
.alarm-header-info p {
  font-size: 14px;
  font-weight: bold;
  color: @gray2;
}
</style>