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
import device from '@/device.js';

Vue.use(iView);
Vue.use(VueAwesomeSwiper);
if (config.env == 'development') {
    global.__app = new Vue({
        el: '#app',
        router: router,
        store: store,
        render: h => h(App)
    });
}else {
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
Vue.prototype.$cookies = Cookies;
Vue.prototype.$api = api;
Vue.prototype.$config = config;
Vue.prototype.$device = device;
Vue.prototype.$rem = rem;