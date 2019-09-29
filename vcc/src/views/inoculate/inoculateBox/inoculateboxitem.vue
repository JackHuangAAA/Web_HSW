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
  name: 'inoculateBoxItem',
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
  data () {
    return {

    }
  },
  computed: {
    valuefocus () {
      let tmp = {}
      let css = 'ibiv-' + this.focus
      tmp[css] = true
      return css
    }
  },
}
</script>
  
<style lang="less" scoped>
.inoculate-box-item-title {
  font-size: 18px;
  font-weight: bold;
}
.inoculate-box-item-value {
  font-size: 20px;
  font-weight: bold;
}
.inoculate-box-item-focus {
  color: rgba(42, 122, 255, 1);
}
.ibiv-blue {
  color: #2a7aff !important;
}
.ibiv-yellow {
  color: #ff7800 !important;
}
</style>