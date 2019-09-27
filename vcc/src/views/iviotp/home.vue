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
    <div class="rightbox"></div>
  </div>
</template>

<script>

import testli from './data/testlackitem.js'

import homecard from './data/homecard'
import itemCard from '_c/main/itemcard'
import lackinventory from '_c/main/lackinventory'
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
</style>