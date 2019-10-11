<template>
  <div>
    <Table :columns="column"
           :data="testdata"></Table>
  </div>
</template>

<script>
import column from "./testdata/column.js";
import testdata from "./testdata/column.js";
export default {
  name: "showTable",
  data() {
    return {
      column,
      testdata
    };
  }
};
</script>

<style lang="" scoped>
</style>