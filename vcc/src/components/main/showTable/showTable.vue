<template>
  <div class="showTable">
    <Table :columns="columns"
           :data="data"
           :row-class-name='tableclass'>
      <template slot-scope="{ row, index }"
                slot="name">
        <span>{{ row.name }}</span>
      </template>
    </Table>
  </div>
</template>

<script>
export default {
  name: "showTable",
  props: {
    columns: {
      type: Array,
      default: () => {
        return [];
      }
    },
    data: {
      type: Array,
      default: () => {
        return [];
      }
    }
  },
  data() {
    return {};
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      console.log(testdata);
    }
  }
};
</script>

<style lang="less">
@import "./showTable.less";
</style>