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
      console.log(this.$store.state.device)
      return this.$store.state.device.alias
    }
  },
  created() {},
  methods: {}
};
</script>

<style lang="less" scoped>
@import "./position.less";
</style>