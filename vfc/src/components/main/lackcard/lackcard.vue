<template>
  <div class="lack-card" :class="reBg(state)">
    <div class="lack-card-title lack-card-item">
      <div class="lack-card-column" :class="reColor(state)">
        <div>title</div>
        <div>number{{unit}}</div>
      </div>
    </div>
    <div class="lack-card-count lack-card-item">
      <div class="lack-card-column">
        <div>title</div>
        <div>number{{unit}}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'lackCard',
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
      default: '支'
    },
    state:{
      type:Number,
      default:''
    }
  },
  data () {
    return {
      reBg(state){
        if(state==1){
          return `lack-type-bg1`
        }else if(state==2){
          return `lack-type-bg2`
        }else if(state==3){
          return `lack-type-bg3`
        }
      },
      reColor(state){
        if(state==1){
          return ``
        }else if(state==2){
          return `lack-card-title-color2`
        }else if(state==3){
          return `lack-card-title-color3`
        }
      }
    }
  }
}
</script>

<style lang="less">
@import "~@/style/color.less";
.lack-card {
  width: 100%;
  height: 120px;
  box-shadow:0px 0px 6px 1px rgba(0, 0, 0, 0.19);
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 11px;
}
.lack-type-bg1{
  background:rgba(255,255,255,1);
}
.lack-type-bg2{
  background:rgba(255,247,233,1);
}
.lack-type-bg3{
  background:rgba(254,226,223,1);
}
.lack-card-title {
  position: absolute;
  left: 28px;
  margin: auto 0px;
}
.lack-card-title-color2{
  color: #F9BC45;
}
.lack-card-title-color3{
  color: #FA5A30;
}
.lack-card-count {
  position: absolute;
  right: 57px;
  margin: auto 0px;
}
.lack-card-column{
  line-height: 1;
}
.lack-card-item {
  font-size: 18px;
  font-weight: 400;
  color: @black;
}
</style>