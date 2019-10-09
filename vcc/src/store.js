/**
 * Created by Administrator on 2017/10/11.
 */
import Vuex from 'vuex'
import Vue from 'vue'

const store = {
  state: {
    user: null,
    device: null, //当前设备
    username: '',
    position: '2号接种台',
    routerTitle: '主页',
    location: '西湖区'
  },

  getters: {
    user: state => {
      return state.user
    },
    device: state => {
      return state.device
    },
    location: state => {
      return state.location
    },
    code: state => {
      return state.user
    },
    routerTitle: state => {
      return state.routerTitle
    },
    username: state => {
      return state.username
    },
    position: state => {
      return state.position
    },
    currentMenu: state => {
      return state.currentMenu
    }
  },

  mutations: {
    ['SAVE_USER']: (state, action) => {
      state.user = action
    },
    ['SAVE_DEVICE']: (state, action) => {
      state.device = action
    },
    ['SAVE_USER_INFO']: (state, userinfo) => {
      state.username = userinfo.name
    },
    ChangeRoute(state, title) {
      state.routerTitle = title
    }
  },

  actions: {
    saveUser({ commit }, user) {
      commit('SAVE_USER', user)
    },
    saveDevice({ commit }, device) {
      commit('SAVE_DEVICE', device)
    },
    saveUserInfo({ commit }, userinfo) {
      commit('SAVE_USER_INFO', userinfo)
    },
    ChangeRoute({ commit }, routertitle) {
      commit('ChangeRoute', routertitle)
    }
  }
}

Vue.use(Vuex)
let Store = new Vuex.Store(store)

export default Store
