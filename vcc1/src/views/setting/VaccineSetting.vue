<template>
  <div class="card">
    <vaccine-table @vaccine-add='vaccineadd'
                   @vaccine-del='vaccinedel'
                   :vacclists='vacclists'
                   :drawers='drawers'
                   type="edit"></vaccine-table>
  </div>
</template>

<script>
import vaccineTable from "_c/main/vaccineTable";
export default {
  name: "VaccineSetting",
  components: {
    vaccineTable
  },
  data () {
    return {
      vacclists: [],
      drawers: []
    };
  },
  computed: {
    deviceId () {
      return this.$store.getters.deviceid;
    }
  },
  created () {
    this.init();
  },
  methods: {
    init () {
      this.getdrawers();
      this.getvacclists();
    },
    async getdrawers () {
      this.drawers = await this.$api.get(`/device/queryDeviceByCondition`, { code: this.deviceId })
    },
    async getvacclists () {
      let res = await this.$api.get(`/zcy/queryVaccineKinds`);
      this.vacclists = res.data
    },
    vaccineadd (va, drawerid) {
      this.addvaccine(va, drawerid);
    },
    vaccinedel (va, drawerid) {
      this.delvaccine(va, drawerid);
    },
    async addvaccine (vaccine, id) {
      if (vaccine !== undefined) {
        vaccine.device = this.deviceid;
        let checked = await this.checkVaccineOfDrawer(vaccine);
        if (checked) {
          let res = await this.$api.post("/drawer/modifyDrawerById", {
            id,
            vaccine
          });
          // this.$store.dispatch("updateDrawe");
          this.getdevice()
        } else {
          this.$Message.error("请勿重复添加");
        }
      }
    },
    async delvaccine (vaccine, id) {
      let res = await this.$api.post("/drawer/modifyDrawerByIdDec", {
        id,
        vaccineId: vaccine._id
      });
      // this.$store.dispatch("updateDrawe");
      this.getgetdrawers()
    },
    async checkVaccineOfDrawer (vaccine) {
      let drawer = this.$store.state.drawer;
      let deviceid = this.$store.state.deviceid;
      let res = await this.$api.get("/vaccine/queryVaccine", {
        device: deviceid
      });
      let vaccines = res.data.rs;
      let checked = vaccines.every(el => {
        return el.name != vaccine.name;
      });
      return checked;
    }
  }
};
</script>

<style lang="less">
</style>