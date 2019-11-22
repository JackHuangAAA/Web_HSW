/**
 * Created by Administrator on 2019/10/11.
 */
import Vuex from "vuex"
import Vue from "vue"

 const store = {
    state: {
        user: null,
        device: null, //当前设备
        customer:null,
        vaccine:null,
    },

    getters: {
        user: state => {
            return state.user;
        },
        device: state => {
            return state.device;
        },
        customer: state => {
            return state.customer;
        },
        vaccine: state => {
            return state.vaccine;
        },
    },

    mutations: {
        ['SAVE_USER']: (state, action) => {
            state.user = action;
        },
        ['SAVE_DEVICE']: (state, action) => {
            state.device = action;
        },
        ['SAVE_CUSTOMER']: (state, action) => {
            state.customer = action;
        },
        ['SAVE_VACCINE']: (state, action) => {
            state.vaccine = action;
        },
    },

    actions: {
        saveUser({commit}, user){
            commit('SAVE_USER', user);
        },
        saveDevice({ commit }, device) {
            commit('SAVE_DEVICE', device);
        },
        saveCustomer({ commit }, customer) {
            commit('SAVE_CUSTOMER', customer);
        },
        saveVaccine({ commit }, vaccine) {
            commit('SAVE_VACCINE', vaccine);
        },
    }
}

Vue.use(Vuex)
let Store = new Vuex.Store(store)

export default Store