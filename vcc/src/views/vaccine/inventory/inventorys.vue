<template>
  <div class="card">
    <div class="inventorys-header">
      <p>提示：点击疫苗所在抽屉，查看库存详情</p>
    </div>
    <div class="inventorys-main">
      <vaccine-table :type="'Router'">
      </vaccine-table>
    </div>
  </div>
</template>

<script>
import vaccineTable from "_c/main/vaccineTable";
export default {
  name: "inventorys",
  components: {
    vaccineTable
  },
  data() {
    return {};
  }
};
</script>

<style lang="less">
.inventorys-header p {
  font-size: 15px;
  font-weight: bold;
  color: rgba(62, 73, 85, 1);
}
.inventorys-header {
  padding-top: 21px;
  padding-left: 34px;
}
.inventorys-main {
  height: ~"calc(100% - 40px)";
  padding: 40px;
}
</style>