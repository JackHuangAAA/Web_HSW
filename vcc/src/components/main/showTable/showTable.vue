<template>
  <div class="showTable">
    <Table :columns="columns"
           :data="data"
           :row-class-name="tableclass">
      <template slot-scope="{ row, index }"
                slot="ac">
      </template>
    </Table>
  </div>
</template>

<script>
const showfp = (vm, h, currentRow) => {
  console.log(currentRow)
  return h(
    "div"
  )
};
const deleteButton = (vm, h, currentRow) => {
  return h(
    "Button",
    {
      style: {
        margin: "0 5px"
      },
      props: {
        type: "error",
        placement: "top"
      }
    },
    vm.delete(currentRow)
  );
};
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
  computed: {},
  mounted() {
    this.init();
  },
  methods: {
    init() {},
    tableclass(row, index) {
      return "table-row";
    },
    tablerender(columns) {
      if (item.handle) {
        item.render = (h, param) => {
          let children = [];
          item.handle.forEach(handle => {
            if (handle === "fp") {
              children.push(showfp(this, h, param.row));
            } else if (handle === "delete") {
              children.push(deleteButton(this, h, param.row));
            }
          });
          return h("div", children);
        };
      }
      return columns;
    }
  }
};
</script>

<style lang="less">
@import "./showTable.less";
</style>