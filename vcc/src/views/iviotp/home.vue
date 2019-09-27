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
    </div>
    <div class="rightbox"></div>
  </div>
</template>

<script>
import homecard from './data/homecard'
import itemCard from '_c/main/itemcard'
export default {
  components: {
    itemCard
  },
  data () {
    return {
      homecard: homecard
    }
  },
}
</script>

<style lang="less">
.homebox {
  width: 100%;
  height: 100%;
}
.rightbox {
  position: absolute;
  width: 320px;
  height: 100%;
  right: 0px;
  // background-color: red;
}
.leftbox {
  position: absolute;
  width: ~"calc(100% - 334px)";
  height: 100%;
  left: 0px;
}
</style>