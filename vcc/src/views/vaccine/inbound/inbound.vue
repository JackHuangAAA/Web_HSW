<template>
  <div class="inbound">
    <div class="bound-header">
      <nav-header :lists="indexlist"
                  :active="1">请选择疫苗入库的抽屉</nav-header>
    </div>
    <div class="bound-content card">
      <vaccine-table :type="`add`"
                     @submit="submit"
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
  data() {
    return {
      indexlist
    };
  },
  methods: {
    async submit(total, drawerid, vaccines) {
      let vaccinesid = [];
      let totals = [];
      let vaccine = vaccines.map(el => ({
        ...el,
        total: total[el.name]
      }));
      console.log(vaccine);
      for (let el of vaccine) {
        vaccinesid.push(el._id);
        totals.push(el.total);
      }
      console.log(vaccinesid, totals);
      let res = this.$api.post("/vaccine/modifyVaccine", {
        id: vaccinesid,
        vaccine: totals
      });
      this.$store.dispatch("updateDrawe");
    },
    inbound() {
      console.log("inbound");
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
  height: ~"calc(100% - 70px)";
}
</style>