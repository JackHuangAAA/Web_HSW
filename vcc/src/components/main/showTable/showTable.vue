<template>
  <div>
    <Table :columns="columns1"
           :data="data1"
           :row-class-name='tableclass'>
      <template slot-scope="{ row, index }"
                slot="name">
        <span>{{ row.name }}</span>
      </template>
    </Table>
  </div>
</template>

<script>
import column from "./testdata/column.js";
import testdata from "./testdata/column.js";
export default {
  name: "showTable",
  data() {
    return {
      columns1: [
        {
          title: "Name",
          slot: "name"
        },
        {
          title: "Age",
          key: "age"
        },
        {
          title: "Address",
          key: "address"
        },
        {
          title: "Action",
          slot: "action",
          width: 150,
          align: "center"
        }
      ],
      data1: [
        {
          name: "John Brown",
          age: 18,
          address: "New York No. 1 Lake Park",
          date: "2016-10-03"
        },
        {
          name: "Jim Green",
          age: 24,
          address: "London No. 1 Lake Park",
          date: "2016-10-01"
        },
        {
          name: "Joe Black",
          age: 30,
          address: "Sydney No. 1 Lake Park",
          date: "2016-10-02"
        },
        {
          name: "Jon Snow",
          age: 26,
          address: "Ottawa No. 2 Lake Park",
          date: "2016-10-04"
        }
      ]
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      console.log(testdata);
    },
    tableclass(row, index) {
      return "table-row";
    }
  }
};
</script>

<style lang="less">
@import "./showTable.vue";
</style>