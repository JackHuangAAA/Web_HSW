<template>
  <div @click="vCardClick()">
    <div class="vCard-checkbox"
         v-if="checked">
      <Checkbox v-model="Selection"></Checkbox>
    </div>
    <Row type="flex"
         class="vCard"
         :class="{'cur-po':NotEdit}"
         align="middle">
      <Col span="12"
           v-if="type != 'edit'">
      <div>
        <Row>
          <p class="vCard-name">狂犬疫苗</p>
        </Row>
        <Row>
          <p class="vCard-count">
            100支
          </p>
        </Row>
      </div>
      </Col>
      <!-- edit模块 -->
      <Col v-if="type == 'edit' && value"
           :span="wspan"
           v-for="vc in value">
      <div class="vCard-edit">
        <p class="vCard-name editfont">{{vc.name}}</p>
        <div class="vCard-edit-d">
          <div class="vCard-edit-img"
               @click="Vaccine('del')"
               :class="{'cur-po':!NotEdit}">
            <img :src="delpng">
          </div>
        </div>
      </div>
      </Col>
      <Col :span="wspan"
           v-if="value.length<max && type=='edit'">
      <div class="vCard-edit editfont">
        <Select v-model="model1"
                size='large'
                placeholder='请选择疫苗名称'
                style="width:150px;">
          <Option v-for="item in vacclists"
                  :value="item.code"
                  :key="item.code">{{ item.name }}</Option>
        </Select>
        <div class="vCard-edit-d">
          <div class="vCard-edit-img"
               @click="Vaccine('add')"
               :class="{'cur-po':!NotEdit}">
            <img :src="addpng">
          </div>
        </div>
      </div>
      </Col>
      <!-- edit模块 -->
    </Row>
    <div>
      <Modal v-model="vCardModal"
             width="548"
             :styles="{top: '30%'}">
        <div style="text-align:center;margin:30px 0px;">
          <Row type="flex"
               align="middle">
            <Col type="flex"
                 justify="end"
                 span="4">
            <p style="font-size:16px;font-weight:bold;color:rgba(50,58,68,1);margin:auto;">
              狂犬疫苗:
            </p>
            </Col>
            <Col span="20">
            <Input v-model="input"></Input>
            </Col>
          </Row>
        </div>
        <div slot="footer"
             class="footerButton">
          <Button size="large"
                  @click="cancel()"
                  style="background:rgba(153,153,153,1);color:rgba(255,255,255,1);">取消</Button>
          <Button type="primary"
                  size="large"
                  @click="handleSubmit()">确定</Button>
        </div>
      </Modal>
    </div>
  </div>
</template>

<script>
import addpng from '_a/add.png'
import delpng from '_a/del.png'
export default {
  name: 'vaccineCard',
  props: {
    id: {//抽屉ID
      type: String
    },
    type: {
      type: String,
      default: 'Router'
    },
    vacclists: {//疫苗下拉列表
      type: Array,
      default: () => {
        return []
      }
    },
    values: {
      type: Array,
      default: () => {
        return []
      }
    },
  },
  data () {
    return {
      addpng, delpng,
      Selection: false,
      vCardModal: false,
      input: '',
      model1: '',
      handle: ['Router', 'edit', 'add', 'check'],
      count: 0,
      max: 2,
    }
  },
  computed: {
    NotEdit () {
      return this.type != 'edit'
    },
    value () {
      if (this.values) return this.values
      else return []
    },
    wspan () {
      let length = this.value.length
      length = Math.max(length, 1)
      return 24 / length
    },
    routerto () {
      return this.type === 'Router'
    },
    additem () {
      return this.type === "add"
    },
    checked () {
      return this.type === "check"
    },

  },
  created () {
    this.init()
  },
  mounted () {
  },
  watch: {
  },
  methods: {
    init () {
      // console.log(this.id)
      // console.log(this.value)
    },
    Vaccine (handle) {
    },
    vCardClick () {
      this[this.type]()
    },
    handlesubmit () {
      this.vCardModal = true
    },
    Router () {
      console.log('router')
    },
    edit () {
    },
    add () {
      console.log('add')
      this.vCardModal = true
    },
    cancel () {
      this.vCardModal = false
    },
    check () {
      console.log('check')
      this.Selection = !this.Selection
    }
  }
}
</script>

<style lang="less">
@import "./vaccineCard.less";
</style>