<template>
  <div class="fingerprint card">
    <showTable :columns="columns"
               :data="data"></showTable>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import columns from "./data/columns.js";
import showTable from "_c/main/showTable";
export default {
  name: "fingerprint",
  components: {
    showTable
  },
  data() {
    return {
      columns,
      data: []
    };
  },
  computed: {
    ...mapGetters({
      user: "user",
      device: "device"
    })
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.coverfp();
    },
    coverfp() {
      let data = [];
      for (let f of this.user.finger) {
        let t = {}
        t.finger = f
        data.push(t)
      }
      this.data = data
    }
  }
};
</script>

<style lang="less">
.fingerprint {
  padding: 44px;
}
</style>