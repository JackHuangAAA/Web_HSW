<template>
  <div class="inbound">
    <div class="bound-header">
      <nav-header :lists="indexlist"
                  :active="1">请选择疫苗入库的抽屉</nav-header>
    </div>
    <div class="bound-content card">
      <vaccine-table :type="`add`"
                     @submit="submit"
                     :drawers='drawers'
                     @click-button="inbound()">
      </vaccine-table>
    </div>
  </div>
</template>

<script>
import indexlist from "./indexlist.js";
import NavHeader from "_c/main/NavHeader";
import vaccineTable from "_c/main/vaccineTable";
export default {
  name: "inbound",
  components: {
    vaccineTable,
    NavHeader
  },
  data () {
    return {
      indexlist,
      drawers: []
    };
  },
  computed: {
    deviceId () {
      return this.$store.getters.deviceid;
    }
  },
  created () {
    this.init()
  },
  methods: {
    init () {
      this.getdrawers()
    },
    async getdrawers () {
      this.drawers = await this.$api.get(`/device/queryDeviceByCondition`, { code: this.deviceId })
    },
    async submit (total, drawerid, vaccines) {
      let vaccinesid = [];
      let totals = [];
      let vaccine = vaccines.map(el => ({
        ...el,
        total: total[el.name]
      }));
      for (let el of vaccine) {
        vaccinesid.push(el._id);
        totals.push(el.total);
      }
      let res = this.$api.post("/vaccine/modifyVaccine", {
        id: vaccinesid,
        vaccine: totals
      });
      this.getdrawers()
      // this.$store.dispatch("updateDrawe");
    },
    inbound () {
      this.$router.push({
        name: "complete",
        params: {
          type: 1
        }
      });
    }
  }
};
</script>

<style lang="less">
.inbound {
  height: 100%;
}
.bound-header {
  height: 70px;
}
.bound-content {
  // height: ~"calc(100% - 70px)";
}
</style>