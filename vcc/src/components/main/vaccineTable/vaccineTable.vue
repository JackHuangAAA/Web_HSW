<template>
  <div class="vTable">
    <div class="vTable-t-b">
      <p> 上 </p>
      <img :src="t2b">
      <p style="margin-top:12px;"> 下 </p>
    </div>
    <div class="vTable-l-r">
      <p> 左 </p>
      <img :src="l2r"
           style="margin-left:40px;">
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
          抽屉二
        </p>
        </Col>
      </Row>
      <Row :gutter="12"
           v-for="lineRow,index of DrawerTree"
           style="margin:14px">
        <Col span="2"
             class="vTableContent-index"> 第{{index}}行 </Col>
        <Col span="11"
             v-for="item of lineRow">
        <vaccineCard :type='type'
                     :vacclists='vacclists'
                     :values='item.vaccine'
                     :id='item._id'
                     :max='2'></vaccineCard>
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
      max: 2,//指定疫苗最大数
      // DrawerTree: {//处理数据为树状结构方便遍历抽屉格式
      //   yNumb: {
      //     xNumb: {
      //       vaccine: {}
      //     }
      //   }
      // }
      DrawerTree: {}
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
  watch: {
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
      if (this.storedrawer === null) {
        let drawer = await this.$store.dispatch('getDrawer')
        this.drawers = drawer
      } else {
        this.drawers = this.storedrawer
      }
      this.drawerinit(this.drawers.rs)
    },
    drawerinit (values) {
      let tree = {}
      for (let el of values) {
        if (tree[el.y] === undefined) tree[el.y] = {}
        if (tree[el.y][el.x] === undefined) tree[el.y][el.x] = []
        tree[el.y][el.x] = el
      }
      this.DrawerTree = tree
    }
  }
}
</script>

<style lang="less">
@import "./vaccineTable.less";
</style>