<template>
  <div class="item-card card">
    <div class="item-number">
      <slot></slot>
    </div>
    <div class="item-title">{{title}}</div>
    <div class="item-icon-frame">
      <img :src="reicon(icon)"
           class="item-icon">
    </div>
  </div>
</template>

<script>
export default {
  name: "itemCard",
  props: {
    title: {
      type: String,
      default: ""
    },
    icon: {
      type: String,
      default: ""
    }
  },
  data() {
    return {};
  },
  methods: {
    reicon(icon) {
      return `/static/img/icon/home/${icon}`;
    }
  }
};
</script>

<style lang="less">
@import "./itemcard.less";
</style>