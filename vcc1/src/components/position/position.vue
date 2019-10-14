<template>
  <div class="positionType">
    {{position}}
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "position",
  data() {
    return {};
  },
  computed: {
    position(){
      return this.$store.state.device.cabinetNo
    }
  },
  created() {},
  methods: {}
};
</script>

<style lang="less" scoped>
@import "./position.less";
</style>