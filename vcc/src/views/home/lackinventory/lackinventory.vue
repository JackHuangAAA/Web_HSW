<template>
  <div class="lack-inventory">
    <Row class="lack-inventory-title">
      <p>缺少库存疫苗</p>
      <div class="lack-number">
        2种
      </div>
      <div class="lack-notice">
        <div class="orange"></div>
        <div>报警</div>
      </div>
      <div class="lack-notice">
        <div class="yellow"></div>
        <div>预警</div>
      </div>
    </Row>
    <Row class="lack-inventory-main"
         :gutter="10">
      <Col v-for="el in lists"
           span="12">
      <lackcard :title="el.name"
                :value="el.surplus" :type="el"></lackcard>
      </Col>
    </Row>
  </div>
</template>

<script>
import lackcard from "_c/main/lackcard";
export default {
  name: "lackInventory",
  components: {
    lackcard
  },
  props: {
    lists: {
      type: Array,
      default() {
        return [];
      }
    }
  },
  data() {
    return {};
  },
  mounted() {},
  methods: {}
};
</script>

<style lang="less">
@import "./lackinventory.less";
.lack-inventory {
  position: relative;
  height: auto;
}
.lack-inventory-icon{
  background: url("~@/assets/icon/Stock.png") no-repeat center;
  width: 15px;
  height: 14px;
  margin-right: 11px;
}
.lack-inventory-title {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin: 27px 19px 41px 59px;
  font-size: 16px;
  font-weight: bold;
  color: @black;
}
.lack-number{
  color: #0066FF;
  font-size: 18px;
  margin: 0px 38px 0px 19px;
}
.lack-notice{
  color: #4F5F6F;
  font-size: 13px;
  display: flex;
  align-items: center;
  margin-right: 34px;
}
.lack-notice div:nth-child(1){
  width: 10px;
  height: 10px;
  margin-right: 12px;
}
.orange{
  background: #FF5D24;
}
.yellow{
  background: #FCBC39;
}
.lack-inventory-main {
  width: ~'calc(100% - 130px)';
  // position: absolute;
  left: 65px;
  // top: 73px;
}
</style>