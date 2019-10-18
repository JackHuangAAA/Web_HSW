<template>
  <div class="positionType">
    {{position}}
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'position',
  data () {
    return {

    }
  },
  computed: {
    ...mapGetters(['position'])
  },
  created () {
  },
  methods: {

  },
}
</script>

<style lang="less" scoped>
.positionType {
  // letter-spacing: 7px;
  font-size: 18px;
  font-family: Microsoft YaHei;
  font-weight: bold;
}
</style>