import './polyfill'
// import '@/theme/index'
import '@/style/index';
// import Cookies from 'js-cookie'
import lodash from 'lodash';
import enums from '@/libs/enum.js';
import Vue from 'vue';
import router from '@/router';
import App from '@/App.vue';
import store from '@/store';
import api from '@/api';
import iView from 'iview';
import 'iview/dist/styles/iview.css';
import config from '@/config';
import device from '@/api/device.js';
import { Storages } from '@/libs/util.js';
import moment from "moment";
global._ = lodash;
global._static = enums;
global.storage = Storages;

moment.lang("zh-cn");
global.moment = moment

Vue.use(iView);

//导入socket.io模块
// import io from 'socket.io-client';
// global.io = io;

let processmod = false;
if (process.env.NODE_ENV !== 'production') {
  processmod = true;
}
Vue.devtools = processmod; //开发环境true 生产环境 fasle
Vue.config.productionTip = processmod;

Vue.prototype.$api = api;
Vue.prototype.$config = config;
Vue.prototype.$device = device;
if (config.env == 'development') {
  global.__app = new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
  });
} else {
  // 安卓环境使用
  window.$d.onReady = function() {
    console.log('$d.ready');
    global.__app = new Vue({
      el: '#app',
      router: router,
      store: store,
      render: h => h(App)
    });
  };
}
