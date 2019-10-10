<template>
  <div class="inbound">
    <div class="bound-header">
      <nav-header :lists="indexlist"
                  :active="1">请选择疫苗入库的抽屉</nav-header>
    </div>
    <div class="bound-content card">
      <vaccine-table :type="`check`"
                     @click-button="outbound()">
      </vaccine-table>
    </div>
  </div>
</template>

<script>
import indexlist from "./indexlist.js";
import NavHeader from "_c/main/NavHeader";
import vaccineTable from "_c/main/vaccineTable";
export default {
  name: "outbound",
  components: {
    vaccineTable,
    NavHeader
  },
  data() {
    return {
      indexlist
    };
  },
  methods: {
    outbound() {
      console.log("outbound"  );
    }
  }
};
</script>

<style lang="less">
.inbound {
  height: 100%;
}
.bound-header {
  height: 70px;
}
.bound-content {
  height: ~"calc(100% - 70px)";
}
</style>