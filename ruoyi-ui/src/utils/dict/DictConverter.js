import DictOptions from './DictOptions'
import DictData from './DictData'

export default function(dict, dictMeta) {

  // DEFAULT_LABEL_FIELDS: ['label', 'name', 'title'],
  // DEFAULT_VALUE_FIELDS: ['value', 'id', 'uid', 'key'],
  /*
  function sum(a, b, c) {
  return a + b + c
  }
  const numbers = [1, 2, 3]
  sum(numbers[0], numbers[1], numbers[2])  // 6
  sum(...numbers)  // 6
  */

  const label = determineDictField(dict, dictMeta.labelField, ...DictOptions.DEFAULT_LABEL_FIELDS)
  const value = determineDictField(dict, dictMeta.valueField, ...DictOptions.DEFAULT_VALUE_FIELDS)
  return new DictData(dict[label], dict[value], dict)
}

/**
 * 确定字典字段
 * @param {DictData} dict
 * @param  {...String} fields dictLabel label. name title
 * 
 */
function determineDictField(dict, ...fields) {
  // 检查 dict 对象自身是否有属性 f（不检查原型链） 强制使用原型链上的原始方法，避免被覆盖
  // find 是数组的方法从头到位找一个符合条件的元素
  return fields.find(f => Object.prototype.hasOwnProperty.call(dict, f))
}
