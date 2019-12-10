import './polyfill';
import lodash from 'lodash';
global._ = lodash;
import Vue from "vue";
import router from "@/router";
import App from "@/App.vue";
import store from "@/store";
import '@/theme/index';
import Cookies from 'js-cookie';
import api from '@/api';
import iView from 'iview';
import VueAwesomeSwiper from 'vue-awesome-swiper';
import config from '@/config';
import rem from '@/rem';
import FastClick from 'fastclick'

FastClick.attach(document.body);
Vue.use(iView);
Vue.use(VueAwesomeSwiper);
//if (config.env == 'development') {
    //生产环境显示屏应用仅使用下面的配置，if else 需要注释
    global.__app = new Vue({
        el: '#app',
        router: router,
        store: store,
        render: h => h(App)
    });
// }else {
//     // 安卓环境使用
//     window.$d.onReady = function() {
//         console.log('$d.ready');
//         global.__app = new Vue({
//             el: '#app',
//             router: router,
//             store: store,
//             render: h => h(App)
//         });
//     };
// }
Vue.prototype.$cookies = Cookies;
Vue.prototype.$api = api;
Vue.prototype.$config = config;
Vue.prototype.$rem = rem;