<template>
  <div class="card">
    <vaccine-table type="edit"></vaccine-table>
  </div>
</template>

<script>
import vaccineTable from '_c/main/vaccineTable'
export default {
  name: 'VaccineSetting',
  components: {
    vaccineTable
  },
  data () {
    return {

    }
  }
}
</script>

<style lang="less">
</style>