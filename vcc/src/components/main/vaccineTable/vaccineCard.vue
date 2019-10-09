<template>
  <div @click="vCardClick()">
    <div class="vCard-checkbox"
         v-if="checked">
      <Checkbox v-model="Selection"></Checkbox>
    </div>
    <Row type="flex"
         class="vCard"
         align="middle">
      <Col span="12">
      <div v-if="type!='edit'">
        <Row>
          <p class="vCard-name">狂犬疫苗</p>
        </Row>
        <Row>
          <p class="vCard-count">
            100支
          </p>
        </Row>
      </div>
      <div v-else
           class="vCard-edit">
        <Select v-model="model1"
                style="width:200px">
          <Option v-for="item in vacclists"
                  :value="item.code"
                  :key="item.code">{{ item.name }}</Option>
        </Select>
        <div class="vCard-edit-d">
          <img :src="addpng">
        </div>
        <div class="vCard-edit-d">
          <img :src="delpng">
        </div>
      </div>
      </Col>
      <Col span="12">
      <Row>
        <p class="vCard-name">百白破疫苗</p>
      </Row>
      <Row>
        <p class="vCard-count">
          100支
        </p>
      </Row>
      </Col>
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
    type: {
      type: String,
      default: 'Router'
    },
    vacclists: {
      type: Array,
      default: () => {
        return []
      }
    },
    x: {
      type: Number
    },
    y: {
      type: Number
    }
  },
  data () {
    return {
      addpng, delpng,
      Selection: false,
      vCardModal: false,
      input: '',
      model1: '',
    }
  },
  computed: {
    routerto () {
      return this.type === 'Router'
    },
    additem () {
      return this.type === "add"
    },
    checked () {
      return this.type === "check"
    }
  },
  mounted () {
  },
  watch: {
    vacclists (value) {
      console.log(value)
    }
  },
  methods: {
    vCardClick () {
      this[this.type]()
    },
    handlesubmit () {
      this.vCardModal = true
    },
    Router () {
      console.log('router')
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