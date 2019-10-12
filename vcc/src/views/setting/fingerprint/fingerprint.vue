<template>
  <div class="fingerprint card">
    <showTable :columns="columns"
               :data="data"></showTable>
  </div>
</template>

<script>
import columns from "./data/columns.js";
import showTable from "_c/main/showTable";
export default {
  name: "fingerprint",
  components: {
    showTable
  },
  data() {
    return {
      data: []
    };
  }
};
</script>

<style lang="less">
.fingerprint {
  padding: 44px;
}
</style>