<template>
  <div class="homebox">
    <div class="leftbox">
      <Row :gutter="13">
        <Col span="8"
             v-for="card in homecard">
        <item-card :title="card.title"
                   :icon="card.icon">{{card.value}}</item-card>
        </Col>
      </Row>
      <Row class="lackinventory card">
        <Col span="24">
        <lackinventory :lists="lists"></lackinventory>
        </Col>
      </Row>
    </div>
    <div class="rightbox">
      <div class="thermometer">
        <!-- <thermometer></thermometer> -->
      </div>
      <div class="alarminfo card">
        <alarminfo :alarmlist="alarmlist"></alarminfo>
      </div>
      <div class="vaccine-set vaccine-in button"
           @click="routerto('inbound')">
        <p>疫苗入库</p>
        <img src="~@/assets/vaccine-in.png">
      </div>
      <div class="vaccine-set vaccine-out button"
           @click="routerto('outbound')">
        <p>疫苗出库</p>
        <img src="~@/assets/vaccine-out.png">
      </div>
    </div>
  </div>
</template>

<script>
const changeItem = (key, value, obj) => {
  let tmp = obj.map(el => {
    el.name
    if (el.name === key) {
      el.value = value
    }
    return el
  })
}
import testli from './data/testlackitem.js'
import alarminfo from './alarminfo'
import homecard from './data/homecard'
import thermometer from './thermometer'
import itemCard from '_c/main/itemcard'
import lackinventory from './lackinventory'
export default {
  components: {
    thermometer,
    itemCard,
    lackinventory,
    alarminfo
  },
  data () {
    return {
      homecard: homecard,
      lists: [],
      alarmlist: [],
    }
  },
  created () {
    this.getHomeData()
  },
  methods: {
    getHomeData () {
      this.queryVaccine()
      this.queryAlarmDailyInfo()
      this.queryDrawerEmpty()
      this.queryVaccineLowerThreshold()
      this.queryAlarmTemperature()
    },
    queryVaccine () {
      this.getTotal('inoculate', '/Vaccine/queryVaccine')
    },
    queryAlarmDailyInfo () {
      this.getTotal('alarm', '/Alarm/queryAlarmByByCondition').then(res => {
        this.alarmlist = res.rs
      })
    },
    queryAlarmTemperature () {
      this.getTotal('temperature', '/Alarm/queryAlarmByByCondition', 1).then(res => {
        this.alarmlist = res.rs
      })
    },
    queryDrawerEmpty () {
      this.getTotal('drawer', '/Drawer/queryDrawerEmpty')
    },
    async queryVaccineLowerThreshold () {
      await this.getTotal('inventory', '/vaccine/queryVaccineLowerThreshold').then(res => {
        this.lists = res.rs
      })
    },
    getTotal (key, url, params = null) {
      return new Promise((resolve, reject) => {
        if (params != null) {
          this.$api.get(url, params).then(res => {
            let data = res.data
            changeItem(key, data.total, this.homecard)
            resolve(data)
          })
        } else {
          this.$api.get(url).then(res => {
            let data = res.data
            changeItem(key, data.total, this.homecard)
            resolve(data)
          })
        }
      })
    },
    routerto (link) {
      this.$router.push({ name: link })
    },
  },
}
</script>

<style lang="less">
@import "~@/style/main/home.less";
</style>