import Cookies from 'js-cookie'

const state = { // 用于存储数据
  sidebar: {
    opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
    withoutAnimation: false,
    hide: false
  },
  device: 'desktop',
  size: Cookies.get('size') || 'medium'
}

const mutations = { // 用于修改state中的数据
  TOGGLE_SIDEBAR: state => { // 侧边栏开关
    if (state.sidebar.hide) { // 如果侧边栏隐藏，则不允许切换
      return false;
    }
    state.sidebar.opened = !state.sidebar.opened // 取反
    state.sidebar.withoutAnimation = false // 有动画
    // 如果侧边栏打开，则设置sidebarStatus为1，否则为0
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  // 关闭侧边栏
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)// 设置sidebarStatus为0
    state.sidebar.opened = false//  关闭侧边栏
    state.sidebar.withoutAnimation = withoutAnimation// 无动画
  },
  TOGGLE_DEVICE: (state, device) => {// 切换设备
    state.device = device
  },
  SET_SIZE: (state, size) => { // 设置大小
    state.size = size
    Cookies.set('size', size)
  },
  SET_SIDEBAR_HIDE: (state, status) => {// 设置侧边栏隐藏
    state.sidebar.hide = status
  }
}

const actions = {// 用于响应组件中的用户操作
  toggleSideBar({ commit }) {// 切换侧边栏
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {// 关闭侧边栏
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) { // 切换设备
    commit('TOGGLE_DEVICE', device)
  },
  setSize({ commit }, size) {// 设置大小
    commit('SET_SIZE', size)
  },
  toggleSideBarHide({ commit }, status) {// 切换侧边栏隐藏
    commit('SET_SIDEBAR_HIDE', status)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
