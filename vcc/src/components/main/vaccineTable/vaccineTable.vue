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
             class="vTableContent-index"> 第1行 </Col>
        <Col span="11">
        <vaccineCard :type='type'
                     :vacclists='vacclists'></vaccineCard>
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
      t2b, l2r,
      vacclists: null,
      drawers: null,
    }
  },
  computed: {
    storevacc () {
      return this.$store.state.vacc
    },
    storedrawer () {
      return this.$store.state.drawer
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    init () {
      this.getVacc()
      this.getDrawer()
    },
    async getVacc () {
      if (this.storevacc === null) {
        this.vacclists = await this.$store.dispatch('getVaccineKinds')
      } else {
        this.vacclists = this.storevacc
      }
    },
    async getDrawer () {
      console.log(this.storedrawer)
      if (this.storedrawer === null) {
        let drawer = await this.$store.dispatch('getDrawer')
        this.drawers = drawer
      } else {
        this.drawers = this.storedrawer
      }
    },
  }
}
</script>

<style lang="less">
@import "./vaccineTable.less";
</style>