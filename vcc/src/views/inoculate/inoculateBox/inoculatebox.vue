<template>
  <div>

    <Row>
      <Row>
        <Col span="12">
        <Row>
          <div class="inoculate-box-title">
            <p>接种人信息</p>
          </div>
        </Row>
        <Row>
          <div class="inoculate-box">
            <Col span="12"
                 v-for="column in peopleColumns"
                 v-if="column.hide!=true">
            <div v-if="column.key=='code'"
                 class="inoculate-box-peopleinfo">
              <div class="inoculate-box-id">{{people['code']}}</div>
              <div class="inoculate-box-name">{{people['name']}}</div>
            </div>
            <div class="inculate-box-item"
                 v-else>
              <inoculateboxitem :ekey="column.key"
                                :title="column.title"
                                :unit="column.unit"
                                :value="people[column.key]"
                                :focus="column.focus"></inoculateboxitem>
            </div>
            </Col>
          </div>
        </Row>
        </Col>
        <Col span="12">
        <Row>
          <div class="inoculate-box-title">
            <p>疫苗信息</p>
          </div>
        </Row>
        <Row>
          <div class="inoculate-box">
            <Col span="12"
                 v-for="column in vaccineColumns"
                 v-if="column.hide!=true">
            <div class="inculate-box-item">
              <inoculateboxitem :ekey="column.key"
                                :title="column.title"
                                :unit="column.unit"
                                :value="vaccine[column.key]"
                                :focus="column.focus"></inoculateboxitem>
            </div>
            </Col>
          </div>
        </Row>
        </Col>
      </Row>
    </Row>
    <Row>
      <div class="inculate-box-button">
        <div class="inculate-box-icon">
          <Spin>
            <Icon type="ios-loading"
                  size=50
                  class="inculate-box-spin-icon-load"></Icon>
          </Spin>
        </div>
        <div class="inculate-box-checkedinfo">疫苗接种信息匹配中</div>
      </div>
    </Row>
  </div>
</template>

<script>
import inoculateboxitem from "./inoculateboxitem.vue";
export default {
  name: "inoculate-Box",
  components: {
    inoculateboxitem
  },
  props: {
    type: {
      tyep: Number,
      default: 0
    },
    peopleColumns: {
      type: Array,
      default() {
        return [];
      }
    },
    people: {
      type: Object,
      default() {
        return {};
      }
    },
    vaccineColumns: {
      type: Array,
      default() {
        return [];
      }
    },
    vaccine: {
      type: Object,
      default() {
        return {};
      }
    }
  },
  data() {
    return {};
  },
  computed: {
    typeinfo() {
      let type = this.type;
      let str = '匹配中'
    }
  },
  mounted() {}
};
</script>

<style lang="less">
@import "./inoculateBox.less";
.inculate-box-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  width: 50px;
}
.inculate-box-checkedinfo {
  margin-left: 40px;
  font-size: 30px;
  font-weight: 400;
  color: rgba(62, 73, 85, 1);
}
.inculate-box-spin-icon-load {
  animation: ani-spin 1s linear infinite;
}
@keyframes ani-spin {
  from {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>