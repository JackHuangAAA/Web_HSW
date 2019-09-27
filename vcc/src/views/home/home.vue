<template>
  <div class="homebox">
    <div class="leftbox">
      <Row :gutter="13">
        <Col span="8"
             v-for="card in homecard">
        <item-card :title="card.title"
                   :icon="card.icon">{{card.value}}</item-card>
        </Col>
      </Row>
      <Row class="lackinventory card">
        <Col span="24">
        <lackinventory :lists="lists"></lackinventory>
        </Col>
      </Row>
    </div>
    <div class="rightbox">
      <div class="thermometer"></div>
      <div class="alarminfo"></div>
      <div class="vaccine-set vaccine-in button">
        <p>疫苗入库</p>
        <img src="/static/img/vaccine-in.png">
      </div>
      <div class="vaccine-set vaccine-out button">
        <p>疫苗出库</p>
        <img src="/static/img/vaccine-out.png">
      </div>
    </div>
  </div>
</template>

<script>

import testli from './data/testlackitem.js'

import homecard from './data/homecard'
import itemCard from '_c/main/itemcard'
import lackinventory from './lackinventory'
export default {
  components: {
    itemCard,
    lackinventory
  },
  data () {
    return {
      homecard: homecard,
      lists: testli
    }
  },
}
</script>

<style lang="less">
.lackinventory {
  height: ~"calc(100% - 250px)";
  min-height: 318px;
  overflow: hidden;
}
.homebox {
  width: 100%;
  height: 100%;
}
.rightbox {
  position: absolute;
  width: 320px;
  height: 100%;
  right: 0px;
}
.leftbox {
  position: absolute;
  width: ~"calc(100% - 334px)";
  height: 100%;
  left: 0px;
}
.thermometer {
  position: relative;
  height: 134px;
  background: blue;
}
.alarminfo {
  margin-top: 13px;
  position: relative;
  background: aqua;
  width: 100%;
  height: ~"calc(100% - 337px)";
}
.vaccine-set {
  display: flex;
  padding: 0px;
  margin: 0px;
  border-radius: 10px;
  position: absolute;
  z-index: 1;
  width: 100%;
  height: 64px;
  max-height: 64px;
  cursor: pointer;
  overflow: hidden;
}
.vaccine-set p {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
  position: absolute;
  font-size: 18px;
  font-weight: 400;
  color: rgba(255, 255, 255, 1);
  text-shadow: 0px 2px 5px rgba(0, 0, 0, 0.4);
}
.vaccine-set img {
  z-index: 0;
  position: relative;
  top: -5px;
  left: -10px;
  height: 80px;
  min-width: 340px;
}
.vaccine-in {
  bottom: 84px;
  margin-bottom: 13px;
}
.vaccine-out {
  bottom: 20px;
}
</style>