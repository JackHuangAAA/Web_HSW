<template>
  <div>
    <Row>
      <div class="vTable-title">选择疫苗存放位置</div>
    </Row>
    <div class="vTable">
      <div class="vTable-t-b">
        <p> 上 </p>
        <img :src="t2b">
        <p style="margin-top:12px;"> 下 </p>
      </div>
      <div class="vTable-l-r">
        <p> 左 </p>
        <img :src="l2r"
            style="margin-left:20px;">
        <p style="margin-left:12px;"> 右 </p>
      </div>
      <div class="vTableContent">
        <Row :gutter="12" style="margin-left:14px">
          <Col span="10" offset="2">
            <p class="vTable-cloumns">
              第一列
            </p>
          </Col>
          <Col span="10" offset="2">
            <p class="vTable-cloumns">
              第二列
            </p>
          </Col>
        </Row>
        <Row :gutter="12"
            v-for="lineRow,index of DrawerTree"
            style="margin:14px">
          <Col span="2"
              class="vTableContent-index"> 第{{index}}行 </Col>
          <Col span="22"
              v-for="item of lineRow">
          <vaccineCard :type='type'
                      @vaccine-add='vaccineadd'
                      @vaccine-del='vaccinedel'
                      :vacclists='vacclists'
                      :values='item.vaccine'
                      :id='item._id'
                      :max='2'></vaccineCard>
          </Col>
        </Row>
      </div>
    </div>
    <Row class="vCard-btnbox">
      <div class="vCard-cancel vCard-button" @click="()=>{this.$router.go(-1)}">取消</div>
      <div class="vCard-sure vCard-button" @click="routerto('scanList')">确认</div>
    </Row>
    <Row class="datetime">
      <datetime>
        <div class="datetime-slot">
          <img :src="scan"/>
          <div>提示：将疫苗条码对准识别口扫码</div>
        </div>
      </datetime>
    </Row>
  </div>
</template>

<script>
import vaccineCard from './vaccineCard.vue'
import datetime from '_c/datetime'
import t2b from '_a/t-b.png'
import l2r from '_a/l-r.png'
import scan from '_a/scan.png'
export default {
  name: 'vaccineTable',
  components: {
    vaccineCard,
    datetime,
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
      scan,
      vacclists: null,
      drawers: null,
      max: 2,//指定疫苗最大数
      DrawerTree: {
        //处理数据为树状结构方便遍历抽屉格式
        //   yNumb: {
        //     xNumb: {
        //       vaccine: {}
        //     }
        //   }
        // }
      }
    }
  },
  computed: {
  },
  watch: {
    '$store.state.drawer': function (value) {
      this.drawerinit(value.rs)
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    vaccineadd (code, drawerid) {
      let va = this.vacclists.filter(el => {
        return el.code === code
      })
      this.$emit('vaccine-add', va[0], drawerid)
    },
    vaccinedel (va, drawerid) {
      this.$emit('vaccine-del', va, drawerid)
    },
    init () {
      this.getVacc()
      this.getDrawer()
    },
    async getVacc () {
      this.vacclists = await this.$store.dispatch('getVaccineKinds')
    },
    async getDrawer () {
      this.drawers = await this.$store.dispatch('getDrawer')
      this.drawerinit(this.drawers.rs)
    },
    drawerinit (values) {
      let tree = {}
      for (let el of values) {
        if (tree[el.y] === undefined) tree[el.y] = {}
        if (tree[el.y][el.x] === undefined) tree[el.y][el.x] = []
        tree[el.y][el.x] = el
      }
      this.$nextTick(() => {
        this.DrawerTree = tree
      })
    },
    routerto(link){
      this.$router.push({name:link})
    },
  }
}
</script>

<style lang="less">
@import "./vaccineTable.less";
</style>