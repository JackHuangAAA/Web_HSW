<template>
  <div class="inventoryDetails bsmain card">
    <div class="inventoryDetails-title">
      <p>
        详情页面
      </p>
    </div>
    <div class="inventoryDetails-main">
      <showTable :columns="columns"
                 :data="data"></showTable>
    </div>
    <div class="TableButton">
      <Button type="primary"
              @click="clickButton()">返回上一页</Button>
    </div>
  </div>
</template>

<script>
import columns from "./data/columns";
import showTable from "_c/main/showTable";
export default {
  name: "inventoryDetails",
  components: {
    showTable
  },
  data() {
    return {
      data: [],
      columns
    };
  },
  computed: {
    id() {
      return this.$route.params.id;
    },
    deviceid() {
      return this.$store.state.deviceid;
    }
  },
  mounted() {
    this.init();
  },
  watch:{
    id(value){
      this.init()
    }
  },
  methods: {
    init() {
      this.data = []
      this.queryDrawerByCondition();
    },
    async queryDrawerByCondition() {
      let res = await this.$api.get("/drawer/queryDrawerByCondition", {
        id: this.id,
        device: this.deviceid
      });
      res = res.data.rs[0];
      this.fmtrs2data(res);
    },
    fmtrs2data(res) {
      let postion = `(${res.x},${res.y})`;
      let data = res.vaccine.map(el => ({
        ...el,
        postion: postion,
        use: el.total - el.surplus
      }));
      this.data = data;
    },
    clickButton() {
      this.$router.go(-1);
    }
  }
};
</script>

<style lang="less">
.inventoryDetails-title {
  font-size: 18px;
  font-weight: bold;
  color: rgba(62, 73, 85, 1);
}
</style>