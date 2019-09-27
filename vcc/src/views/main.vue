<template>
  <div class="layout">
    <div class="menu">
      <menu-list :lists="menulist"></menu-list>
    </div>
    <div class="header">
      <Row type="flex">
        <div class="routerlink">
          <routerlink></routerlink>
        </div>
        <div class="position">
          <position></position>
        </div>
        <div class="userinfo">
          <userinfo></userinfo>
        </div>
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
import userinfo from '_c/userinfo'
import position from '_c/position'
import routerlink from '_c/routerlink'
import menuList from '_c/menulist'
import iviotp from '@/router/iviotp'
export default {
  name: 'main',
  components: {
    datetime,
    userinfo,
    position,
    routerlink,
    menuList
  },
  data () {
    return {
      menulist: []
    }
  },
  created () {
    this.getmenulist()
  },
  methods: {
    getmenulist () {
      let menulist = iviotp.map(el => {
        let path = { path: el.path, name: el.name, ...el.meta }
        return path
      })
      this.menulist = menulist
    }
  },
}
</script>

<style lang="less" scoped>
@import "~@/style/index.less";
.layout {
  width: 100%;
  height: 100%;
  background: @gray;
  position: fixed;
  color: @black;
  min-width: @minhtmlwidth;
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
  width: ~"calc(100% - 41px)";
  height: ~"calc(100% - 118px)";
  margin: 20px 18px 0px 20px;
}
.menu {
  position: absolute;
  width: 180px;
  // height: 100%;
  z-index: 200;
  // background-color: @blue;
}
.routerlink {
  margin-left: 30px+180px;
  height: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: @black;
}
.title p {
  margin: auto;
  font-size: 18px;
  font-weight: bold;
  color: @black;
}
.position {
  margin: auto;
  color: @blue;
}
.userinfo {
  margin: auto 26px auto 0px;
}
.datetime {
  margin: auto 21px auto 0px;
  color: @black;
}
</style>