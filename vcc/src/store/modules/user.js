export default {
  state: {
    user: null,
    username: '李晓文',
    position: '2号接种台',
    routerTitle: '主页',
    location: '西湖区',
    currentMenu: {} //当前菜单
  },

  getters: {
    location: state => {
      return state.location
    },
    user: state => {
      return state.user
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
    ['CURRENT_MENU']: (state, action) => {
      state.currentMenu = action
    },
    ['SAVE_USER_INFO']: (state, userinfo) => {
      state.username = userinfo.name
    }
  },

  actions: {
    saveUser({ commit }, user) {
      commit('SAVE_USER', user)
    },
    saveUserInfo({ commit }, userinfo) {
      commit('SAVE_USER_INFO', userinfo)
    },
    setCurrentMenu({ commit }, currentMenu) {
      commit('CURRENT_MENU', currentMenu)
    }
  }
}
