<template>
  <div class="inoculate card">
    <inoculate-box :peopleColumns='peopleColumns'
                   :people='people'
                   :vaccineColumns='vaccineColumns'
                   :vaccine='vaccine'
                   :type='type'></inoculate-box>
  </div>
</template>

<script>
import vaccineColumns from "./data/vaccine-columns.js";
import peopleColumns from "./data/people-columns.js";
import people from "./data/people.js";
import vaccine from "./data/vaccine.js";
import inoculateBox from "./inoculateBox";
import { IncomingMessage } from "http";
export default {
  name: "inoculate",
  components: {
    inoculateBox
  },
  data() {
    return {
      peopleColumns: peopleColumns,
      people: people,
      vaccineColumns: vaccineColumns,
      vaccine: vaccine,
      type: 0
    };
  },
  created() {
    this.init();
  },
  methods: {
    async init() {
      this.getpeople();
      this.getvacc();
    },
    async getpeople() {
      let res = await this.$api.post("/zcy/reciveVaccination");
      let people = res.data;
      this.$nextTick(() => {
        this.people = people;
        return;
      });
    },
    async getvacc() {
      let res = await this.$api.get(`/zcy/queryVaccineKinds`);
      let vaccs = res.data;
      this.cont(vaccs);
    },
    cont(vaccs) {
      let type = 2;
      for (let va of vaccs) {
        if (va.name === this.people.vaccineName) {
          this.vaccine = va;
          type = 1;
        }
      }
      this.type = type;
    }
  }
};
</script>

<style lang="less">
@import "./inoculate.less";
</style>