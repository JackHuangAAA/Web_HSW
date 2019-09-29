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
@import "~@/style/main/alarm";
</style>