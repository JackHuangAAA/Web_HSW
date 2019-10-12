<template>
  <div class="inoculate card">
    <inoculate-box :peopleColumns='peopleColumns'
                   :people='people'
                   :vaccineColumns='vaccineColumns'
                   :vaccine='vaccine'></inoculate-box>
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
      vaccine: vaccine
    };
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.getpeople();
    },
    async getpeople() {
      let res = await this.$api.post("/zcy/reciveVaccination");
      let people = res.data;
      console.log(people);
      this.$nextTick(() => {
        this.people = people;
      });
    }
  }
};
</script>

<style lang="less">
@import "./inoculate.less";
</style>