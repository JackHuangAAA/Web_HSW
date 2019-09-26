<template>
  <div>{{nowdate}}</div>
</template>

<script>
import { dateFmt } from '@/libs/util.js'
export default {
  name: 'datetime',
  data () {
    return {
      nowdate: 'asd'
    }
  },
  computed: {

  },
  created () {
    this.dateint()
  },
  methods: {
    dateint () {
      setInterval(() => {
        let date = new Date()
        //初步格式化
        let nowdate = this.DateConver(date) + " " + this.getday(date)
        this.nowdate = nowdate
      }, 500);
    },
    DateConver (date) {
      return dateFmt("yyyy-MM-dd hh:mm:ss", date)
    },
    getday (date) {
      let day = date.getDay(date)
      let week = ""
      switch (day) {
        case 1:
          week = "星期一"
          break;
        case 2:
          week = "星期二"
          break;
        case 3:
          week = "星期三"
          break;
        case 4:
          week = "星期四"
          break;
        case 5:
          week = "星期五"
          break;
        case 6:
          week = "星期六"
          break;
        default:
          week = "星期日"
          break;
      }
      return week
    }
  }
}
</script>

<style lang="less">
</style>