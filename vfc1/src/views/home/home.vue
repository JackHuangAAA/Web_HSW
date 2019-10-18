<template>
  <div class="homebox">
    <div class="leftbox">
      <Row :gutter="16">
        <Col span="12"
             v-for="card in homecard">
          <item-card :title="card.title"
                   :icon="card.icon"
                   :type="card.type">{{card.value}}</item-card>
        </Col>
        <div class="thermometer">
          <thermometer :value="0"
              :indoor="20"></thermometer>
        </div>
      </Row>
    </div>
    <div class="lackinventory">
      <lackinventory :lists="lists"></lackinventory>
      <div class="vaccine-button">
        <div class="vaccine-set vaccine-in"
          @click="routerto('inbound')">
          <p>疫苗入库</p>
          <img src="/static/img/images/vaccine-in.png">
        </div>
        <div class="vaccine-set vaccine-out"
            @click="routerto('outbound')">
          <p>疫苗出库</p>
          <img src="/static/img/images/vaccine-out.png">
        </div>
      </div>
    </div>
    <div class="datetime">
        <datetime></datetime>
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
import homecard from './data/homecard'
import thermometer from './thermometer'
import itemCard from '_c/main/itemcard'
import lackinventory from './lackinventory'
import datetime from '_c/datetime'
export default {
  components: {
    thermometer,
    itemCard,
    lackinventory,
    datetime,
  },
  data () {
    return {
      homecard: homecard,
      lists: [1,2,3,4,5,6,7,8,9,10,11,12],
    }
  },
  created () {
    this.getHomeData()
  },
  computed: {
    deviceid () {
      return this.$store.state.deviceid
    }
  },
  methods: {
    getHomeData () {
      // this.queryVaccine()
      // this.queryAlarmDailyInfo()
      // this.queryDrawerEmpty()
      // this.queryVaccineLowerThreshold()
      // this.queryAlarmTemperature()
      // this.queryVaccinationByCustomerCode()
    },
    queryVaccinationByCustomerCode () {
      this.getTotal('customer', '/Vaccine/queryVaccinationByCustomerCode', { device: this.deviceid })
    },
    queryVaccine () {
      this.getTotal('inoculate', '/Vaccine/queryVaccine', { device: this.deviceid })
    },
    queryAlarmDailyInfo () {
      this.getTotal('alarm', '/Alarm/queryAlarmByByCondition', { device: this.deviceid }).then(res => {
        this.alarmlist = res.rs
      })
    },
    queryAlarmTemperature () {
      this.getTotal('temperature', '/Alarm/queryAlarmByByCondition', { device: this.deviceid, type: 1 }).then(res => {
        this.alarmlist = res.rs
      })
    },
    queryDrawerEmpty () {
      console.log(this.deviceid)
      this.getTotal('drawer', '/Drawer/queryDrawerEmpty', { device: this.deviceid })
    },
    async queryVaccineLowerThreshold () {
      await this.getTotal('inventory', '/vaccine/queryVaccineLowerThreshold', { device: this.deviceid }).then(res => {
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
.datetime{
  position: absolute;
  bottom: -15px !important;
  width: 100%;
  height: 60px;
}
</style>