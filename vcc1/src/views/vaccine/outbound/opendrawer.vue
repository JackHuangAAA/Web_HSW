<template>
  <div class="bound">
    <div class="bound-header">
      <nav-header :lists="indexlist"
                  :active="2">请将抽屉中疫苗全部取出</nav-header>
    </div>
    <div class="bound-content card">
      <div class="opendrawer">
        <div class="opendrawer-title">
          <img :src="succeed">
          <p>抽屉锁已打开，请将疫苗全部取出</p>
        </div>
        <div class="opendrawer-button">
          <Row>
            <Row type="flex"
                 align="center">
              <Button type="primary">完成</Button>
            </Row>
            <Row type="flex"
                 style="padding-top:20px;"
                 align="center">
              <p style="font-size:18px;font-weight:400;color:rgba(130,130,130,1);">疫苗取出完毕请点击完成按钮</p>
            </Row>
          </Row>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import indexlist from "./indexlist.js";
import succeed from "_a/succeed.png";
import NavHeader from "_c/main/NavHeader";
export default {
  name: "opendrawer",
  components: {
    NavHeader
  },
  data() {
    return {
      succeed,
      indexlist
    };
  }
};
</script>

<style lang="less">
.opendrawer-title {
  display: flex;
  align-items: center;
  font-size: 30px;
  font-weight: 400;
  justify-content: center;
  position: relative;
  top: 120px;
  p {
    padding-left: 20px;
  }
}
.opendrawer-button {
  position: relative;
  top: 330px;
  .ivu-btn {
    width: 175px;
    height: 52px;
    font-size: 18px;
    font-weight: 400;
  }
}
</style>