<template>
  <div>
    <Row v-for="el in lists"
         class="alarm-card">
      <Col v-for="column in columns"
           :span="column.width">
      <div v-if="column.key==='ID'"
           class="alarm-card-index">
        <p>{{el['id']}}</p>
      </div>
      <div v-else>
        <div class="ivt">
          <p>{{column.title}}</p>
        </div>
        <div v-if="column.static"
             class="ivh">
          <p>{{content(el[column.key],column.map)}}</p>
        </div>
        <div v-else-if="column.type==='datetime'"
             class="ivh">
          <p>{{datetime(el[column.key])}}</p>
        </div>
        <div v-else
             class="ivh">
          <p>{{el[column.key]}}</p>
        </div>
      </div>
      </Col>
    </Row>
  </div>
</template>

<script>
import { dbDateFmt } from "@/libs/util.js";
export default {
  name: "alarmcard",
  props: {
    lists: {
      type: Array,
      default() {
        return [];
      }
    },
    columns: {
      type: Array,
      deafult() {
        return [];
      }
    }
  },
  data() {
    return {};
  },
  computed: {
    datetime() {
      return createDate => {
        return dbDateFmt(createDate);
      };
    },
    content() {
      return (param, map) => {
        console.log(_static[map][param].title);
        return _static[map][param].title;
      };
    }
  },
  mounted() {}
};
</script>

<style lang="less">
@import "./alarmcard.less";
</style>