<template>
  <div class="inoculate-box-item">
    <Row>
      <p class="inoculate-box-item-title">
        {{title}}
      </p>
    </Row>
    <Row>
      <p class="inoculate-box-item-value"
         :class="valuefocus">
        {{value}}{{unit}}
      </p>
    </Row>
  </div>
</template>

<script>
export default {
  name: "inoculateBoxItem",
  props: {
    ekey: {
      type: String
    },
    title: {
      type: String
    },
    value: {
      type: String
    },
    unit: {
      type: String
    },
    focus: {
      type: String
    }
  },
  data() {
    return {};
  },
  computed: {
    valuefocus() {
      let tmp = {};
      let css = "ibiv-" + this.focus;
      tmp[css] = true;
      return css;
    }
  }
};
</script>
  
<style lang="less" scoped>
@import "./inoculateboxitem.less";
</style>