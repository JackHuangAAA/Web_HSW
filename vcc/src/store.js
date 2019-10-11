import Vuex from 'vuex';
import Vue from 'vue';
import {
  queryDeviceByCondition,
  queryVaccineKinds,
  queryDrawerByCondition
} from '@/libs/axios.js';
import { getcode, Storages } from '@/libs/util.js';
const store = {
  state: {
    user: null,
    device: null, //当前设备
    deviceid: null,
    userinfo: null,
    position: '2号接种台',
    routerTitle: '主页',
    location: '西湖区',
    vacc: null, //通过zcy获取的疫苗列表
    drawer: null //缓存的抽屉柜子
  },

  getters: {
    user: state => {
      return state.user;
    },
    device: state => {
      return state.device;
    },
    location: state => {
      return state.location;
    },
    code: state => {
      return state.user;
    },
    routerTitle: state => {
      return state.routerTitle;
    },
    userinfo: state => {
      return state.userinfo;
    },
    position: state => {
      return state.position;
    }
  },

  mutations: {
    ['SAVE_USER']: (state, action) => {
      state.user = action;
    },
    ['SAVE_DEVICE']: (state, action) => {
      state.location = action.address.countyName;
      state.position = action.alias;
      state.deviceid = action._id;
      state.device = action;
    },
    SaveUserInfo(state, userinfo) {
      state.userinfo = userinfo;
    },
    ChangeRoute(state, title) {
      state.routerTitle = title;
    },
    SaveVaccineKinds(state, lists) {
      state.vacc = lists;
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
    saveUserInfo({ commit }, userinfo) {
      Storages.SaveStorage('userinfo',userinfo)
      commit('SaveUserInfo', userinfo);
    },
    ChangeRoute({ commit }, routertitle) {
      commit('ChangeRoute', routertitle);
    },
    async getUserInfo({ state,commit, getters }) {
      if (state.userinfo === null) {
        let userinfo = Storages.GetStorage('userinfo');
        userinfo = JSON.parse(userinfo)
        commit('SaveUserInfo',userinfo)
        return userinfo;
      } else {
        return getters.userinfo;
      }
    },
    async getDrawer({ state, dispatch }) {
      if (state.drawer === null) {
        let res = await dispatch('updateDrawe');
        return res.data;
      } else {
        return state.drawer;
      }
    },
    async getVaccineKinds({ state, dispatch }) {
      if (state.vacc === null) {
        let res = await dispatch('updateVaccineKinds');
        return res.data;
      } else {
        return state.vacc;
      }
    },
    async updateDrawe({ state, commit }) {
      let deviceid = state.deviceid;
      let res = await queryDrawerByCondition(deviceid);
      commit('SaveDrawer', res.data);
      return res;
    },
    async updateVaccineKinds({ state, commit }) {
      let res = await queryVaccineKinds();
      commit('SaveVaccineKinds', res.data);
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
