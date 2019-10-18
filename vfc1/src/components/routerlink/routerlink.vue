<template>
  <div class="routerlinkType">
    {{routerTitle}}
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'routerlink',
  data () {
    return {

    }
  },
  computed: {
    ...mapGetters(['routerTitle'])
  },
  created () {
  },
  methods: {

  },
}
</script>

<style lang="less" scoped>
.routerlinkType {
  margin: auto;
  font-size: 18px;
  font-family: Microsoft YaHei;
  font-weight: bold;
}
</style>