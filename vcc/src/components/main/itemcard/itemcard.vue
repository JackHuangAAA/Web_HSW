<template>
  <div class="item-card">
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
  name: 'itemCard',
  props: {
    title: {
      type: String,
      default: ''
    },
    icon: {
      type: String,
      default: ''
    }
  },
  data () {
    return {

    }
  },
  methods: {
    reicon (icon) {
      return `/static/img/icon/home/${icon}`
    }
  }
}
</script>

<style lang="less">
@import "~@/style/color";
.item-card {
  width: 100%;
  height: 110px;
  background-color: @white;
  position: relative;
  display: flex;
  justify-items: center;
  align-items: center;
  margin-bottom: 13px;
  border-radius: 10px;
}
.item-number {
  position: absolute;
  top: 16px;
  left: 30px;
  color: @blue;
  font-size: 36px;
  font-weight: bold;
}
.item-title {
  position: absolute;
  left: 27px;
  bottom: 16px;
  color: @black;
  font-size: 18px;
  font-weight: 400;
}
.item-icon-frame {
  position: absolute;
  right: 29px;
  height: 66px;
  width: 66px;
  border-radius: 50%;
  background-color: @gray;
}
.item-icon {
  height: 100%;
  width: 100%;
}
</style>