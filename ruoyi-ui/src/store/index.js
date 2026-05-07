// 为什么在这里引入
import Vue from 'vue' // 为了使用 Vue.use 安装插件
import Vuex from 'vuex' // 引入 Vuex（为了使用 Vuex 的功能） Vuex.Store
import app from './modules/app'
import dict from './modules/dict'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import settings from './modules/settings'
import getters from './getters'

// Vuex 插件 use
Vue.use(Vuex) // 创建一个 store 实例

const store = new Vuex.Store({
  modules: {
    app,
    dict,
    user,
    tagsView,
    permission,
    settings
  },
  getters
})

export default store
