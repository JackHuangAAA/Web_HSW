<template>
  <div class="vTable">
    <div class="vTable-t-b">
      <p> 上 </p>
      <img :src="t2b">
      <p style="margin-top:12px;"> 下 </p>
    </div>
    <div class="vTable-l-r">
      <p> 左 </p>
      <img :src="l2r">
      <p style="margin-left:12px;"> 右 </p>
    </div>
    <div class="vTableContent">
      <Row>
        <Col span="11"
             push="2">
        <p class="vTable-cloumns">
          抽屉一
        </p>
        </Col>
        <Col span="11"
             push="2">
        <p class="vTable-cloumns">
          抽屉2
        </p>
        </Col>
      </Row>
      <Row :gutter="12">
        <Col span="2"
             class="vTableContent-index"> 第一行 </Col>
        <Col span="11">
        <vaccineCard :type='type'></vaccineCard>
        </Col>
        <Col span="11">
        <vaccineCard :type='type'></vaccineCard>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
import vaccineCard from './vaccineCard.vue'
import t2b from '_a/t-b.png'
import l2r from '_a/l-r.png'
export default {
  name: 'vaccineTable',
  components: {
    vaccineCard
  },
  props: {
    type: {
      type: String,
      default: 'Router'
    }
  },
  data () {
    return {
      t2b, l2r
    }
  }
}
</script>

<style lang="less">
.vTableContent-index {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(62, 73, 85, 1);
}
.vTable {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.vTable-t-b,
.vTable-l-r {
  position: absolute;
  p {
    font-size: 16px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: rgba(62, 73, 85, 1);
  }
}
.vTable-t-b {
  display: flex;
  flex-direction: column;
  left: 108px;
}
.vTable-l-r {
  display: flex;
  flex-direction: row;
  top: 88px;
}
.vTableContent {
  padding: 100px 150px;
  width: 100%;
  height: 100%;
}
.vTable-cloumns {
  margin-top: -20px;
  margin-bottom: 10px;
  text-align: center;
  font-size: 14px;
  font-weight: bold;
  color: rgba(62, 73, 85, 1);
}
</style>