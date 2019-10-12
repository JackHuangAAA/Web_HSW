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
      data: [],
      test: [
        {
          finger: "finger1"
        },
        {
          finger: "finger2"
        }
      ]
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
      console.log(this.user);
    }
  }
};
</script>

<style lang="less">
.fingerprint {
  padding: 44px;
}
</style>