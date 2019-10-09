import Vue from 'vue'
import VueRouter from 'vue-router'
import Routers from './routers'
import store from '@/store'
import config from '@/config'
import api from '@/api'

Vue.use(VueRouter)

// 路由配置
const RouterConfig = {
  mode: 'hash',
  routes: Routers
}

const router = new VueRouter(RouterConfig)

let routers = []
_.forEach(RouterConfig.routes, v => {
  routers.push(v)
  if (v.children != null) {
    routers = _.concat(routers, v.children)
  }
})

let getRouterConfig = function(path) {
  let config = _.find(routers, { path: path })
  return config
}

router.beforeEach((to, from, next) => {
  if (to.meta.title != null) store.dispatch('ChangeRoute', to.meta.title)
  let menu = getRouterConfig(to.path)
  if (menu != null) {
    let p = Promise.resolve()
    if (store.getters.user == null && to.path != '/login') {
      //当前用户信息不存在
      p = api.get('/user/current').then(result => {
        store.dispatch('saveUser', result.data)
        return Promise.resolve()
      })
    }
    if (menu.access != null) {
      //如果有权限
      p.then(() => {
        //校验权限
        if (config.env == 'development') {
          console.log(
            `check permission`,
            menu.access,
            store.getters.user.role.name
          )
        }
        if (_.indexOf(menu.access, store.getters.user.role.name) >= 0) {
          next()
        } else {
          next({ path: '/50x', query: { message: '没有权限访问' } })
        }
      })
    } else {
      next()
    }
  } else {
    //找不到页面自动匹配最后404
    next()
  }
})

export default router
