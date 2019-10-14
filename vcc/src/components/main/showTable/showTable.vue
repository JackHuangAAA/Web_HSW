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
const deleteButton = (vm, h, currentRow) => {
  return h(
    "Button",
    {
      style: {
        margin: "0 5px"
      },
      props: {
        type: "Dashed",
        placement: "top"
      },
      on: {
        click: () => {
          vm.delete(currentRow);
        }
      }
    },
    '刪除'
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
    return {
      columnslist: []
    };
  },
  computed: {},
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.columnslist = this.tablerender(this.columns);
      console.log(this.columnslist);
    },
    tableclass(row, index) {
      return "table-row";
    },
    tablerender(columns) {
      columns.forEach(item => {
        if (item.handle) {
          item.render = (h, param) => {
            let children = [];
            item.handle.forEach(handle => {
              if (handle === "delete") {
                children.push(deleteButton(this, h, param.row));
              }
            });
            return h("div", children);
          };
        }
      });
      return columns;
    },
    delete(currentRow) {
      console.log(currentRow)
    }
  }
};
</script>

<style lang="less">
@import "./showTable.less";
</style>