/**
 * Created by Administrator on 2017/10/11.
 */
import Vuex from "vuex"
import Vue from "vue"

 const store = {
    state: {
        user: null,
        // currentMenu: {}//当前菜单
        isactive:0
    },

    getters: {
        user: state => {
            return state.user;
        },
        // currentMenu: state => {
        //     return state.currentMenu;
        // }
        isactive:state=>{
            return state.isactive
        }
    },

    mutations: {
        ['SAVE_USER']: (state, action) => {
            state.user = action;
        },
        // ['CURRENT_MENU']: (state, action) => {
        //     state.currentMenu = action;
        // }
        ['IS_ACTIVE']:(state,action)=>{
            state.isactive=action
        }
    },

    actions: {
        saveUser({commit}, user){
            commit('SAVE_USER', user);
        },
        // setCurrentMenu({commit}, currentMenu){
        //     commit('CURRENT_MENU', currentMenu);
        // }
        setActive({commit},action){
            commit('IS_ACTIVE',action)
        }
    }
}

Vue.use(Vuex)
let Store = new Vuex.Store(store)

export default Store