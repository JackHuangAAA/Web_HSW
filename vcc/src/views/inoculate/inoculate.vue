<template>
  <div class="inoculate card">
    <inoculate-box :peopleColumns='peopleColumns'
                   :people='people'
                   :vaccineColumns='vaccineColumns'
                   :vaccine='vaccine'></inoculate-box>
  </div>
</template>

<script>
import vaccineColumns from './data/vaccine-columns.js'
import peopleColumns from './data/people-columns.js'
import people from './data/people.js'
import vaccine from './data/vaccine.js'
import inoculateBox from './inoculateBox'
import { IncomingMessage } from 'http'
export default {
  name: 'inoculate',
  components: {
    inoculateBox
  },
  data () {
    return {
      peopleColumns: peopleColumns,
      people: people,
      vaccineColumns: vaccineColumns,
      vaccine: vaccine
    }
  }
}
</script>

<style lang="less">
@import "~@/style/main/inoculate.less";
</style>