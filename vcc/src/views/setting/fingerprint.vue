<template>
  <div class="fingerprint card">
    <showTable></showTable>
  </div>
</template>

<script>
import showTable from '_c/main/showTable'
export default {
  name: "fingerprint",
  components:{
    showTable
  },
  data() {
    return {};
  }
};
</script>

<style lang="less">
.fingerprint{
  padding:44px;
}
</style>