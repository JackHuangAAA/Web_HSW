<template>
  <div class="layout">
    <div class="header">
      <Row type="flex">
        <div class="menu"></div>
        <div class="title">
          <p>路径名字</p>
        </div>
        <div class="position">
          二号接种台
        </div>
        <div class="userinfo">李晓文</div>
        <div class="datetime">
          <datetime></datetime>
        </div>
      </Row>
    </div>
    <div class="main">
      <router-view></router-view>
    </div>

  </div>
</template>

<script>
import datetime from '_c/datetime'
export default {
  name: 'main',
  components: {
    datetime
  },
  data () {
    return {

    }
  }
}
</script>

<style lang="less" scoped>
@import "../style/color.less";
.layout {
  width: 100%;
  height: 100%;
  background: @bgcolor;
  position: relative;
  color: @black;
}
.header {
  width: 100%;
  height: 60px;
  background: rgba(255, 255, 255, 1);
  position: absolute;
  top: 0px;
  left: 0px;
}
.main {
  top: 60px;
  position: absolute;
  background: rgba(255, 255, 255, 1);
  width: ~"calc(100% - 41px)";
  height: ~"calc(100% - 118px)";
  margin: 20px 18px 0px 23px;
}
.menu {
  position: absolute;
  width: 180px;
  height: 60px;
  z-index: 200;
  background-color: @blue;
}
.title {
  margin-left: 30px+180px;
  height: 60px;
  display: flex;
  justify-content: center;
  align-content: center;
}
.title p {
  margin: auto;
  font-size: 18px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: @black;
}
.position {
  margin: auto;
  font-size: 20px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: @blue;
}
.userinfo {
  margin: auto 26px auto 0px;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
}
.datetime {
  margin: auto 21px auto 0px;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: @black;
}
</style>