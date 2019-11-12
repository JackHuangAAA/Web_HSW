import './polyfill'
import lodash from 'lodash'
global._ = lodash
import Vue from "vue"
import router from "@/router"
import App from "@/App.vue"
import store from "@/store"
import '@/theme/index'
import '@/style/index'
import Cookies from 'js-cookie'
import api from '@/api'
import iView from 'iview'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import config from '@/config'
import FastClick from 'fastclick'

FastClick.attach(document.body);
global._ = lodash
Vue.use(iView);
Vue.use(VueAwesomeSwiper);

global.__app = new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App)
});

Vue.prototype.$cookies = Cookies;
Vue.prototype.$api = api;
Vue.prototype.$config = config;
