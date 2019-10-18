<template>
  <div class="Steps"> 
    <div class="Steps-item"
         v-for="(list,index) in lists">
      <div class="Steps-index"
           :class="{'Steps-index-active':index<=active-1}">{{index+1}}</div>
      <div class="Steps-step"
           :class="{'Steps-active':index<=active-1}">{{list.title}}</div>
      <div class="steps-x"
           :class="{'Steps-index-active':index<=active-1}"
           v-if="index!==0"></div>
      <div style="position:relative">
        <div class="step-line" :class="{'step-line-active':index<=active-2}" v-if="index<lists.length-1"></div>
      </div>
      
    </div>
  </div>
  
</template>

<script>
export default {
  name: 'Steps',
  props: {
    lists: {
      type: Array,
      default () {
        return []
      }
    },
    active: {
      type: Number,
      default: 0
    }
  },
  data () {
    return {

    }
  },
  mounted () {
    console.log(this.active)
  }
}
</script>

<style lang="less">
.Steps {
  margin-top: -10px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  height: 70px;
}
.Steps-item {
  width: 100px;
  height: 54px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.Steps-index {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  background: rgba(201, 201, 201, 1);
  border-radius: 50%;
  font-size: 16px;
  font-weight: 400;
  color: rgba(236, 240, 248, 1);
  z-index: 2;
}
.Steps-index-active {
  background: rgba(42, 122, 255, 1) !important;
}
.Steps-active {
  color: rgba(42, 122, 255, 1) !important;
}
.step-line{
  width:76px;
  height:4px;
  background:rgba(201,201,201,1);
  border-radius:2px;
  position: absolute;
  top: -40px;
  left: 12px;
}
.step-line-active{
  background: rgba(42, 122, 255, 1) !important;
}
.Steps-step {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  font-weight: 400;
}
.steps-x {
  top: -37px;
  right: 64px;
  z-index: -10;
  position: relative;
  width: 128px;
  height: 4px;
  background: rgba(201, 201, 201, 1);
  border-radius: 2px;
}
</style>