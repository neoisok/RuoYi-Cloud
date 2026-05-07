import Vue from 'vue'
import { mergeRecursive } from "@/utils/ruoyi";
import DictMeta from './DictMeta'
import DictData from './DictData'

const DEFAULT_DICT_OPTIONS = {
  types: [],
}

/**
 * @classdesc 字典
 * @property {Object} label 标签对象，内部属性名为字典类型名称
 * @property {Object} dict 字段数组，内部属性名为字典类型名称
 * @property {Array.<DictMeta>} _dictMetas 字典元数据数组
 */
export default class Dict {
  constructor() {
    this.owner = null
    this.label = {}
    this.type = {}
  }

  init(options) {
    if (options instanceof Array) {
      options = { types: options }
    }
    console.trace('谁调用了 init？')  // 打印调用栈
    console.log('合并前 DEFAULT_DICT_OPTIONS:', JSON.stringify(DEFAULT_DICT_OPTIONS))
    const opts = mergeRecursive(DEFAULT_DICT_OPTIONS, options)
    console.log('合并后 DEFAULT_DICT_OPTIONS:', JSON.stringify(DEFAULT_DICT_OPTIONS))
    console.log('opts === DEFAULT_DICT_OPTIONS:', opts === DEFAULT_DICT_OPTIONS)
    if (opts.types === undefined) {
      throw new Error('need dict types')
    }
    const ps = []
    // 把字典类型字符串数组，转换成字典元数据对象数组，.map() 遍历数组的每个元素 对每个字符串 t，调用 DictMeta.parse(t)
    // DictMeta.parse() 会把这个字符串解析成一个完整的配置对象
    this._dictMetas = opts.types.map(t => DictMeta.parse(t))
    this._dictMetas.forEach(dictMeta => {
      const type = dictMeta.type // 取出当前字典类型的名称，后面会用作对象的属性名。
      Vue.set(this.label, type, {}) //Vue.set 进行响应式的创建 创建一个空对象，用来存储"值 → 标签"的快速查找映射。
      Vue.set(this.type, type, []) // 创建一个空数组，用来存储字典数据列表。
      if (dictMeta.lazy) { //某些字典数据量大或不常用，等真正需要时再手动调用 reloadDict 加载。
        return
      }
      ps.push(loadDict(this, dictMeta))
    })
    /*
    加载字典1 ──┐
    加载字典2 ──┼→ 都完成 → 完成
    加载字典3 ──┘
    */
    return Promise.all(ps)
  }

  /**
   * 重新加载字典
   * @param {String} type 字典类型
   */
  reloadDict(type) {
    const dictMeta = this._dictMetas.find(e => e.type === type)
    if (dictMeta === undefined) {
      return Promise.reject(`the dict meta of ${type} was not found`)
    }
    return loadDict(this, dictMeta)
  }
}

/**
 * 加载字典
 * @param {Dict} dict 字典
 * @param {DictMeta} dictMeta 字典元数据
 * @returns {Promise}
 */
function loadDict(dict, dictMeta) {
  // debugger;
  return dictMeta.request(dictMeta)
    .then(response => {
      const type = dictMeta.type
      let dicts = dictMeta.responseConverter(response, dictMeta)
      if (!(dicts instanceof Array)) {
        console.error('the return of responseConverter must be Array.<DictData>')
        dicts = []
      } else if (dicts.filter(d => d instanceof DictData).length !== dicts.length) {
        console.error('the type of elements in dicts must be DictData')
        dicts = []
      }
      dict.type[type].splice(0, Number.MAX_SAFE_INTEGER, ...dicts)
      dicts.forEach(d => {
        Vue.set(dict.label[type], d.value, d.label)
      })
      return dicts
    })
}
