import { mergeRecursive } from "@/utils/ruoyi";
import DictOptions from './DictOptions'

/**
 * @classdesc 字典元数据
 * @property {String} type 类型
 * @property {Function} request 请求
 * @property {String} label 标签字段
 * @property {String} value 值字段
 */
export default class DictMeta {
  constructor(options) {
    this.type = options.type // type
    this.request = options.request // request
    this.responseConverter = options.responseConverter // responseConverter
    this.labelField = options.labelField // labelField
    this.valueField = options.valueField // valueField
    this.lazy = options.lazy === true // lazy
  }
}


/**
 * 解析字典元数据
 * @param {Object} options
 * @returns {DictMeta}
 */
DictMeta.parse= function(options) {
  let opts = null
  if (typeof options === 'string') {
    // DictOptions.metas.xx	属性名是固定的，直接写
    // DictOptions.metas[options]	属性名是变量，用变量的值作为属性名
    opts = DictOptions.metas[options] || {}
    opts.type = options
  } else if (typeof options === 'object') {
    opts = options
  }
  opts = mergeRecursive(DictOptions.metas['*'], opts)
  return new DictMeta(opts)
}
