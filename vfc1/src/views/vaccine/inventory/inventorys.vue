<template>
  <div class="inventorys-bg">
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
import vaccineTable from '_c/main/vaccineTable'
export default {
  name: 'inventorys',
  components: {
    vaccineTable
  },
  data () {
    return {

    }
  }
}
</script>

<style lang="less">
.inventorys-bg{
  background: #fff;
}
.inventorys-header p {
  font-size: 14px;
  font-weight: bold;
  color: #3E4955;
}
.inventorys-header {
  padding-top: 25px;
  padding-left: 31px;
}
.inventorys-main {
  height: ~"calc(100% - 60px)";
  padding-left: 31px;
}
</style>