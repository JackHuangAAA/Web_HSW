<template>
  <div class="lack-card">
    <div class="lack-card-title lack-card-item">{{title}}</div>
    <div class="lack-card-count lack-card-item">{{value}}{{unit}}</div>
  </div>
</template>

<script>
export default {
  name: "lackCard",
  props: {
    title: {
      type: String,
      default: "未获取"
    },
    value: {
      type: Number,
      default: 0
    },
    unit: {
      type: String,
      default: "支"
    }
  },
  data() {
    return {};
  }
};
</script>

<style lang="less">
@import "~@/style/color.less";
.lack-card {
  width: 100%;
  height: 82px;
  background-color: @gray;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}
.lack-card-title {
  position: absolute;
  left: 25px;
  margin: auto 0px;
}
.lack-card-count {
  position: absolute;
  right: 18px;
  margin: auto 0px;
}

.lack-card-item {
  font-size: 18px;
  font-weight: 400;
  color: @black;
}
</style>