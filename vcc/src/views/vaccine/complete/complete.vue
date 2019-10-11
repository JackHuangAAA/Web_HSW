<template>
  <div class="complete card bsmain">
    <div class="complete-title">
      <img :src="succeed">
      <p>疫苗{{title}}完成</p>
    </div>
    <div class="complete-backbutton"
         @click="gobackhome()">
      <img :src="gohome">
    </div>
  </div>
</template>

<script>
import gohome from "_a/gohome.png";
import succeed from "_a/succeed.png";
export default {
  name: "complete",
  data() {
    return {
      succeed,
      gohome
    };
  },
  computed: {
    type() {
      return this.$route.params.type;
    },
    title() {
      return this.type === "1" ? "入库" : "出库";
    }
  },
  created:{
    
  },
  methods: {
    gobackhome() {
      this.$router.push({ name: "home" });
    }
  }
};
</script>

<style lang="less">
.complete {
  position: relative;
}
.complete-title {
  height: 46px;
  width: 100%;
  display: flex;
  justify-content: center;
  p {
    margin-left: 20px;
    font-size: 30px;
    font-weight: 400;
  }
}
.complete-backbutton {
  cursor: pointer;
  position: absolute;
  top: 30px;
  right: 20px;
}
</style>