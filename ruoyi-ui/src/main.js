import Vue from 'vue' // 引入 Vue

import Cookies from 'js-cookie' // 引入 Cookies

import Element from 'element-ui'
import './assets/styles/element-variables.scss' // 引入 element-ui 样式

/**
 * 这两个通常控制了：页面基础样式、主题颜色、滚动条、按钮、布局规范、UI 定制样式
 */
import '@/assets/styles/index.scss' // global css// 引入全局样式
import '@/assets/styles/ruoyi.scss' // ruoyi css // 引入 ruoyi 样式
import App from './App' // 引入 App.vue
import store from './store' // 引入 store
import router from './router' // 引入 router
import directive from './directive' // directive
import plugins from './plugins' // plugins
import { download } from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from "@/utils/ruoyi";
// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 字典数据组件
import DictData from '@/components/DictData'

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)

Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
DictData.install()

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */



/**
 * import Element from 'element-ui'
 * import './assets/styles/element-variables.scss'
 * })
 * 加载 Element-UI 组件库，设置组件默认大小来自 Cookie（可让用户自定义字号）。
 */
Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app', // 挂载点把vue应用挂载到HTML页面中的#app元素上
  router, //挂载路由系统（用 vue-router）
  store, //挂载状态管理系统（用 vuex）
  render: h => h(App) // 渲染App.vue组件
})
