<template>
  <div class="layout">
    <menu-list class="menu"
               :lists="menulist"
               :menuOpen='menuOpen'
               @clickitem='menuItemClick'
               @click="menuClick()"></menu-list>
    <div class="menu-shade"
         v-show="menuOpen"
         @click="menuClose()"></div>
    <div class="header">
      <Row type="flex">
        <div class="routerlink">
          <routerlink></routerlink>
        </div>
        <div class="position">
          <position></position>
        </div>
        <div class="userinfo"
             @click="test()">
          <userinfo></userinfo>
        </div>
        <div class="datetime">
          <datetime></datetime>
        </div>
      </Row>
    </div>
    <div class="main">
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
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
      menulist: [],
      menuOpen: false,
      socket: '',
    }
  },
  created () {
    this.getmenulist()
  },
  mounted () {
    // this.websocket()
  },
  methods: {
    test () {
      console.log('123')
      this.$router.push('/login')
    },
    menuItemClick (item) {
      this.menuClose()
    },
    menuClick () {
      this.$nextTick(() => {
        this.menuOpen = !this.menuOpen
      })
    },
    menuClose () {
      this.menuOpen = false
    },
    getmenulist () {
      let menulist = vcc.map(el => {
        let path = { path: el.path, name: el.name, ...el.meta }
        return path
      })
      this.menulist = menulist
    },
    websocket () {
      this.socket = io()
    }
  },
}
</script>

<style lang="less" scoped>
@import "~@/style/index.less";
</style>