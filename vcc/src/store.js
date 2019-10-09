import Vuex from 'vuex'
import Vue from 'vue'
import { queryDeviceByCondition, queryVaccineKinds } from '@/libs/axios.js'
import { getid, Storages } from '@/libs/util.js'
const store = {
  state: {
    user: null,
    device: null, //当前设备
    username: '',
    position: '2号接种台',
    routerTitle: '主页',
    location: '西湖区',
    vacc: null
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
    }
  },

  mutations: {
    ['SAVE_USER']: (state, action) => {
      state.user = action
    },
    ['SAVE_DEVICE']: (state, action) => {
      state.location = action.address.countyName
      state.position = action.alias
      state.device = action
    },
    ['SAVE_USER_INFO']: (state, userinfo) => {
      state.username = userinfo.name
    },
    ChangeRoute(state, title) {
      state.routerTitle = title
    },
    SaveVaccineKinds(state, lists) {
      state.vacc = lists
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
    },
    async getVaccineKinds({ state, commit }) {
      let res = await queryVaccineKinds()
      commit('SaveVaccineKinds', res.data)
      return res.data
    },
    async getDevice({ commit, dispatch }) {
      let id = await getid()
      let res = await queryDeviceByCondition(id)
      dispatch('saveDevice', res.data[0])
      return res.data[0]
    }
  }
}

Vue.use(Vuex)
let Store = new Vuex.Store(store)

export default Store
