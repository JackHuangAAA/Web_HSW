// import './polyfill'
// import '@/theme/index'
import '@/style/index'
// import Cookies from 'js-cookie'
import lodash from 'lodash'
import Vue from 'vue'
import router from '@/router'
import App from '@/App.vue'
import store from '@/store'
// import api from '@/api'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import config from '@/config'
import util from '@/libs/util.js'
let _ = lodash

Vue.use(iView)

let processmod = false
if (process.env.NODE_ENV !== 'production') {
  processmod = true
}
Vue.devtools = processmod //开发环境true 生产环境 fasle
Vue.config.productionTip = processmod

// Vue.prototype.$api = api
// Vue.prototype.$config = config

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
