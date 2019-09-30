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
            <div v-if="column.key=='id'"
                 class="inoculate-box-peopleinfo">
              <div class="inoculate-box-id">{{people['id']}}</div>
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

      </div>
    </Row>
  </div>
</template>

<script>
import inoculateboxitem from './inoculateboxitem.vue'
export default {
  name: 'inoculate-Box',
  components: {
    inoculateboxitem
  },
  props: {
    peopleColumns: {
      type: Array,
      default () {
        return []
      }
    },
    people: {
      type: Object,
      default () {
        return {}
      }
    },
    vaccineColumns: {
      type: Array,
      default () {
        return []
      }
    },
    vaccine: {
      type: Object,
      default () {
        return {}
      }
    }
  },
  data () {
    return {

    }
  },
  mounted () {
  },
}
</script>

<style lang="less">
@import "./inoculateBox.less";
</style>