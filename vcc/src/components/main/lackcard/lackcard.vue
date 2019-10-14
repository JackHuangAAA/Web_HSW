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
@import "./lackcard.less";
</style>