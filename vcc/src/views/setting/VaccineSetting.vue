<template>
  <div class="card">
    <vaccine-table @vaccine-add='vaccineadd'
                   @vaccine-del='vaccinedel'
                   type="edit"></vaccine-table>
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
  },
  methods: {
    vaccineadd (va, drawerid) {
      this.addvaccine(va, drawerid)
    },
    vaccinedel (va, drawerid) {
      console.log(va, drawerid)
      this.delvaccine(va, drawerid)
    },
    async addvaccine (vaccine, id) {
      // let res = await this.$api.post('/drawer/modifyDrawerById', { id, vaccine })
      this.checkVaccineOfDrawer(vaccine)
      this.$store.dispatch('updateDrawe')
    },
    async delvaccine (vaccine, id) {
      let res = await this.$api.post('/drawer/modifyDrawerByIdDec', { id, vaccineId: vaccine._id })
      this.$store.dispatch('updateDrawe')
    },
    async checkVaccineOfDrawer (vaccine) {
      let drawer = this.$store.state.drawer
      let deviceid = this.$store.state.deviceid
      console.log(deviceid)
      let res = await this.$api.get('/vaccine/queryVaccine', { device: deviceid })
      console.log(res)
    }
  }
}
</script>

<style lang="less">
</style>