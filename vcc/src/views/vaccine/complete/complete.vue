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
    <div class="complete-info">
      <Row>
        <Col span="8">
        {{title}}支数:
        </Col>
        <Col span="8">
        取出人员:{{username}}
        </Col>
        <Col span="8">
        取出时间:{{time}}
        </Col>
      </Row>
    </div>
    <div class="complete-main">
      <show-table></show-table>
    </div>
  </div>
</template>

<script>
import showTable from '_c/main/showTable'
import gohome from "_a/gohome.png";
import succeed from "_a/succeed.png";
import moment from "moment";
export default {
  name: "complete",
  components:{
    showTable
  },
  data() {
    return {
      succeed,
      gohome,
      nowtime: moment()
    };
  },
  computed: {
    time() {
      let time = moment().format("YYYY-MM-DD HH:mm:ss");
      return time;
    },
    username() {
      if (this.userinfo != null) return this.userinfo.name;
      else return "";
    },
    type() {
      return this.$route.params.type;
    },
    title() {
      return this.type === "1" ? "入库" : "出库";
    }
  },
  created: {
    
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
.complete-info {
  padding:40px 0px;
  position: relative;
  left: 24%;
  width: 70%;
  font-size: 17px;
  font-weight: 400;
}
</style>