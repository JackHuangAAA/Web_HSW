/**
 * Created by Administrator on 2019/10/11.
 */
import Vuex from "vuex"
import Vue from "vue"

 const store = {
    state: {
        user: null,
        device: null, //当前设备
    },

    getters: {
        user: state => {
            return state.user;
        },
        device: state => {
            return state.device;
        },
    },

    mutations: {
        ['SAVE_USER']: (state, action) => {
            state.user = action;
        },
        ['SAVE_DEVICE']: (state, action) => {
            state.device = action;
        },
    },

    actions: {
        saveUser({commit}, user){
            commit('SAVE_USER', user);
        },
        saveDevice({ commit }, device) {
            commit('SAVE_DEVICE', device);
        },
    }
}

Vue.use(Vuex)
let Store = new Vuex.Store(store)

export default Store