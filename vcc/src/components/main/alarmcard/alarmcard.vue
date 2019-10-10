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
@import "~@/style/color";
.ivt {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(102, 102, 102, 1);
  margin: 21px 0px 12px 0px;
}
.ivh {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(62, 73, 85, 1);
  margin: 0px 0px 0px 0px;
}
.alarm-card {
  width: 100%;
  height: 100px;
  margin: 8px 0px;
  background-color: @gray;
}
.alarm-card-index p {
  font-size: 30px;
  font-weight: bold;
  color: @white;
  margin: auto;
}
.alarm-card-index {
  width: 24px;
  height: 100px;
  background-color: #b1d9ff;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>