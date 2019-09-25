// import './polyfill'
// import '@/theme/index'
import '@/style/index'
// import Cookies from 'js-cookie'
import lodash from 'lodash'
import Vue from 'vue'
import router from '@/router'
import App from '@/App.vue'
import store from '@/store'
import api from '@/api'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import config from '@/config'

let _ = lodash

Vue.use(iView)

global.__app = new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})

Vue.prototype.$api = api
Vue.prototype.$config = config
