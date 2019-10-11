<template>
  <div class="showTable">
    <Table :columns="columns"
           :data="data"
           :row-class-name="tableclass"
           >
      <template slot-scope="{ row, index }"
                slot="ac">
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
  computed: {
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {},
    tableclass(row, index) {
      return "table-row";
    }
  }
};
</script>

<style lang="less">
@import "./showTable.less";
</style>