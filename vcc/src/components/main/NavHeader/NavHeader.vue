<template>
  <div class="nav-header">
    <div class="nav-back"
         @click="goback()">
      <img :src="back">
    </div>
    <div class="nav-slot">
      <slot></slot>
    </div>
    <div class="nav-steps">
      <Steps :lists='lists'
             :active='active'></Steps>
    </div>
  </div>
</template>

<script>
import Steps from "_c/main/Steps";
import back from "_a/back.png";
export default {
  name: "NavHeader",
  components: {
    Steps
  },
  props: {
    lists: {
      type: Array,
      default() {
        return [];
      }
    },
    active: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      back
    };
  },
  methods: {
    goback() {
      this.$router.go(-1);
    }
  }
};
</script>

<style lang="less">
.nav-header {
  justify-content: center;
  margin-top: -10px;
  display: flex;
  height: 70px;
  width: 100%;
  padding: 5px;
}
.nav-back {
  position: absolute;
  left: 0px;
  cursor: pointer;
}
.nav-slot {
  margin: auto;
  font-size: 30px;
  font-weight: 400;
  color: rgba(62, 73, 85, 1);
}
.nav-steps {
  position: absolute;
  right: 0px;
}
</style>