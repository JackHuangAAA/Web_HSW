<template>
  <div class="layout">
    <menu-list class="menu"
               :lists="menulist"></menu-list>
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
import vcc from '@/router/vcc'
export default {
  name: 'Main',
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
      let menulist = vcc.map(el => {
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
</style>