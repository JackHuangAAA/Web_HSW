<template>
  <div class="date">{{nowdate}}</div>
</template>

<script>
import moment from "moment";
moment.lang("zh-cn");
export default {
  name: "datetime",
  data() {
    return {
      nowdate: "null"
    };
  },
  computed: {},
  created() {
    this.dateint();
  },
  methods: {
    dateint() {
      setInterval(() => {
        let nowdate = moment().format("YYYY-MM-DD HH:mm:ss dddd");
        this.nowdate = nowdate;
      }, 500);
    },
  }
};
</script>

<style lang="less">
@import "./datetime.less";
</style>