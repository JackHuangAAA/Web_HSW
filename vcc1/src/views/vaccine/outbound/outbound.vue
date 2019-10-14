<template>
  <div class="bound">
    <div class="bound-header">
      <nav-header :lists="indexlist"
                  :active="1">请选择疫苗出库的抽屉</nav-header>
    </div>
    <div class="bound-content card">
      <vaccine-table :type="`check`"
                     :drawers='drawers'
                     @click-button="outbound()">
      </vaccine-table>
    </div>
  </div>
</template>

<script>
import indexlist from "./indexlist.js";
import NavHeader from "_c/main/NavHeader";
import vaccineTable from "_c/main/vaccineTable";
export default {
  name: "outbound",
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
    outbound () {
      this.$router.push({
        name: "opendrawer"
      });
    }
  }
};
</script>

<style lang="less">
</style>