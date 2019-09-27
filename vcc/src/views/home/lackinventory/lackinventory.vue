<template>
  <div class="lack-inventory">
    <Row class="lack-inventory-title">
      <p>缺少库存疫苗</p>
    </Row>
    <Row class="lack-inventory-main"
         :gutter="10">
      <Col v-for="el in lists"
           span="6">
      <lackcard :title="el.title"
                :value="el.value"
                :unit="el.unit"></lackcard>
      </Col>
    </Row>
  </div>
</template>

<script>
import lackcard from "_c/main/lackcard"
export default {
  name: 'lackInventory',
  components: {
    lackcard
  },
  props: {
    lists: {
      type: Array,
      default () {
        return []
      }
    }
  },
  data () {
    return {

    }
  },
  mounted () {
  },
  methods: {
  },
}
</script>

<style lang="less">
@import "~@/style/color.less";
.lack-inventory {
}
.lack-inventory-title {
  margin: 25px 21px;
  font-size: 16px;
  font-weight: bold;
  color: @black;
}
.lack-inventory-main {
  padding: 1px 17px;
}
</style>