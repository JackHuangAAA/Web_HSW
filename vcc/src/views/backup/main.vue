<template>
  <div>
    test
  </div>
</template>

<style lang="less" scoped>
</style>

<script>
import { mapGetters, mapActions } from "vuex";
import enumTool from "@/enum";
import moment from "moment";
import io from "socket.io-client";

export default {
  data() {
    return {};
  },
  computed: {
    ...mapGetters({
      user: "user",
      currentMenu: "currentMenu"
    })
  },
  components: {},
  methods: {
    ...mapActions({
      saveUser: "saveUser",
      setCurrentMenu: "setCurrentMenu"
    })
  },
  mounted() {}
};
</script>

