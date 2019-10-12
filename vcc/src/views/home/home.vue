<template>
  <div class="homebox">
    <div class="leftbox">
      <Row :gutter="13">
        <Col span="8"
             v-for="card in homecard">
        <item-card :title="card.title"
                   :icon="card.icon"
                   :unit="card.unit">{{card.value}}</item-card>
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
        <thermometer :value="0"
                     :indoor="20"></thermometer>
      </div>
      <div class="alarminfo card">
        <alarminfo :alarm="alarmcode"></alarminfo>
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
    el.name;
    if (el.name === key) {
      el.value = value;
    }
    return el;
  });
};
import homecard from "./data/homecard";
import alarminfo from "./alarminfo";
import thermometer from "./thermometer";
import itemCard from "_c/main/itemcard";
import lackinventory from "./lackinventory";
export default {
  components: {
    thermometer,
    itemCard,
    lackinventory,
    alarminfo
  },
  data() {
    return {
      homecard: homecard,
      lists: [],
      alarmlist: [],
      alarmcode: 0
    };
  },
  created() {
    this.getHomeData();
  },
  computed: {
    deviceid() {
      return this.$store.state.deviceid;
    },
    device() {
      return this.$store.getters.device;
    }
  },
  methods: {
    getHomeData() {
      // this.queryVaccine();//查询疫苗种类
      // this.queryAlarmDailyInfo();//查询告警次数
      // this.queryDrawerEmpty();//查询空药柜
      // this.queryVaccineLowerThreshold();//查询库存
      this.queryAlarmTemperature();//查询温度告警
      this.queryVaccinationByCondition();//查询接种人数
    },
    queryVaccinationByCondition() {
      this.getTotal("customer", "/vaccination/queryVaccinationByCondition", {
        device: this.deviceid
      });
    },
    queryVaccine() {
      this.getTotal("inoculate", "/Vaccine/queryVaccine", {
        device: this.deviceid
      });
    },
    queryAlarmDailyInfo() {
      this.getTotal("alarm", "/Alarm/queryAlarmByByCondition", {
        device: this.deviceid
      }).then(res => {
        this.alarmlist = res.rs;
      });
    },
    queryAlarmTemperature() {
      this.getTotal("temperature", "/Alarm/queryAlarmByByCondition", {
        device: this.deviceid,
        type: 1
      }).then(res => {
        this.alarmlist = res.rs;
      });
    },
    queryDrawerEmpty() {
      this.getTotal("drawer", "/Drawer/queryDrawerEmpty", {
        device: this.deviceid
      });
    },
    async queryVaccineLowerThreshold() {
      await this.getTotal("inventory", "/vaccine/queryVaccineLowerThreshold", {
        device: this.deviceid
      }).then(res => {
        this.lists = res.rs;
      });
    },
    getTotal(key, url, params = null) {
      return new Promise((resolve, reject) => {
          this.$api.get(url, params).then(res => {
            let data = res.data;
            let total = data.total || data.length
            changeItem(key, total, this.homecard);
            resolve(data);
          });
      });
    },
    routerto(link) {
      this.$router.push({ name: link });
    }
  }
};
</script>

<style lang="less">
@import "./home.less";
</style>