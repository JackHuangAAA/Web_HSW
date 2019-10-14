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
      <!-- 无数据显示 -->
      <Col v-if="isNull && type != 'edit'"
           span="24">
      <p class="vCard-name">暂无存放疫苗</p>
      </Col>
      <!-- 无数据显示 -->
      <Col :span="span"
           v-else-if="type != 'edit'"
           v-for="vc in value">
      <div>
        <Row>
          <p class="vCard-name">{{vc.name}}</p>
        </Row>
        <Row>
          <p class="vCard-count">
            {{surplus(vc)}}
          </p>
        </Row>
      </div>
      </Col>
      <!-- edit模块 -->
      <Col v-else-if="type=='edit'"
           :span="wspan"
           v-for="vc in value">
      <div class="vCard-edit">
        <p class="vCard-name editfont">{{vc.name}}</p>
        <div class="vCard-edit-d">
          <div class="vCard-edit-img"
               @click="Vaccine('del',vc)"
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
               align="middle"
               v-if="type != 'edit' && value"
               v-for="vc in value">
            <Col type="flex"
                 justify="end"
                 style="padding:10px;"
                 span="7">
            <p class="vCard-modal-p">
              {{vc.name}}:
            </p>
            </Col>
            <Col span="15">
            <Input v-model="input[vc.name]"></Input>
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
                  @click="handleSubmit(value)">确定</Button>
        </div>
      </Modal>
    </div>
  </div>
</template>

<script>
import addpng from "_a/add.png";
import delpng from "_a/del.png";
export default {
  name: "vaccineCard",
  props: {
    id: {
      //抽屉ID
      type: String
    },
    type: {
      type: String,
      default: "base"
    },
    vacclists: {
      //疫苗下拉列表
      type: Array,
      default: () => {
        return [];
      }
    },
    values: {
      type: Array,
      default: () => {
        return [];
      }
    }
  },
  data() {
    return {
      addpng,
      delpng,
      Selection: true,
      vCardModal: false,
      input: {},
      model1: "",
      handle: ["base", "edit", "add", "check"],
      count: 0,
      max: 2 //最大显示数量，vcc为2
    };
  },
  computed: {
    isNull() {
      let check = false;
      if (this.values === null || this.values === undefined) check = true;
      return check;
    },
    NotEdit() {
      return this.type != "edit";
    },
    value() {
      if (!this.isNull) return this.values;
      else return [];
    },
    wspan() {
      let length = this.value.length + 1;
      length = Math.min(length, this.max);
      return 24 / length;
    },
    span() {
      let length = this.value.length;
      length = length < 1 ? 1 : length;
      length = Math.min(length, this.max);
      return 24 / length;
    },
    baseto() {
      return this.type === "base";
    },
    additem() {
      return this.type === "add";
    },
    checked() {
      return this.type === "check";
    },
    surplus() {
      return vc => {
        if (this.type == "add") {
          return vc.total === undefined ? "" : vc.total + "支";
        } else {
          return vc.surplus === undefined ? 0 + "支" : vc.surplus + "支";
        }
      };
    }
  },
  created() {
    this.init();
  },
  mounted() {},
  watch: {},
  methods: {
    init() {
      // console.log(this.id)
      // console.log(this.value)
    },
    Vaccine(handle, vc) {
      switch (handle) {
        case "add":
          this.$emit("vaccine-add", this.model1, this.id);
          break;
        case "del":
          this.$emit("vaccine-del", vc, this.id);
          break;
        default:
          break;
      }
    },
    vCardClick() {
      this[this.type]();
    },
    handleSubmit(vcs) {
      this.$emit("handleSubmit", this.input, this.id, vcs);
      this.vCardModal = false;
    },
    base() {
      this.$emit("click", this.id, this.value);
    },
    edit() {},
    add() {
      if (this.value.length > 0) {
        this.vCardModal = true;
      } else {
        this.$Message.info("请在设置中划分该区域疫苗");
      }
    },
    cancel() {
      this.vCardModal = false;
    },
    check() {
      // this.Selection = !this.Selection;
    }
  }
};
</script>

<style lang="less">
@import "./vaccineCard.less";
</style>