import Vuex from 'vuex';
import Vue from 'vue';
import {
  queryDeviceByCondition,
  queryDrawerByCondition
} from '@/libs/axios.js';
import { getcode } from '@/libs/util.js';
const store = {
  state: {
    user: null,
    device: null, //当前设备
    deviceid: null,
    routerTitle: '主页',
    drawer: null //缓存的抽屉柜子
  },

  getters: {
    user: state => {
      return state.user;
    },
    device: state => {
      return state.device;
    },
    code: state => {
      return state.user;
    },
    routerTitle: state => {
      return state.routerTitle;
    },
  },

  mutations: {
    ['SAVE_USER']: (state, action) => {
      state.user = action;
    },
    ['SAVE_DEVICE']: (state, action) => {
      state.deviceid = action._id;
      state.device = action;
    },
    ChangeRoute(state, title) {
      state.routerTitle = title;
    },
    SaveDrawer(state, drawer) {
      state.drawer = drawer;
    }
  },

  actions: {
    saveUser({ commit }, user) {
      commit('SAVE_USER', user);
    },
    saveDevice({ commit }, device) {
      commit('SAVE_DEVICE', device);
    },
    ChangeRoute({ commit }, routertitle) {
      commit('ChangeRoute', routertitle);
    },
    async updateDrawe({ state, commit }) {
      //用于刷新抽屉信息
      let deviceid = state.deviceid;
      let res = await queryDrawerByCondition(deviceid);
      commit('SaveDrawer', res.data);
      return res;
    },
    async getDevice({ dispatch }) {
      let devicecode = await getcode();
      let res = await queryDeviceByCondition(devicecode);
      dispatch('saveDevice', res.data[0]);
      return res.data[0];
    }
  }
};

Vue.use(Vuex);
let Store = new Vuex.Store(store);

export default Store;