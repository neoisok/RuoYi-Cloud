import Vue from 'vue'
import store from '@/store'
import DataDict from '@/utils/dict'
import { getDicts as getDicts } from '@/api/system/dict/data'

function searchDictByKey(dict, key) {
  if (key == null && key == "") {
    return null
  }
  try {
    for (let i = 0; i < dict.length; i++) {
      if (dict[i].key == key) {
        return dict[i].value
      }
    }
  } catch (e) {
    return null
  } 
}

function install() {
  Vue.use(DataDict, {
    metas: {
      '*': {
        labelField: 'dictLabel',
        valueField: 'dictValue',
        //ES6语法，当属性的值是一个函数时，可以省略 : function，直接写函数名和参数。
        request(dictMeta) {
          const storeDict = searchDictByKey(store.getters.dict, dictMeta.type)
          if (storeDict) {
            return new Promise(resolve => { resolve(storeDict) })
          } else {
            return new Promise((resolve, reject) => {
              getDicts(dictMeta.type).then(res => {
                store.dispatch('dict/setDict', { key: dictMeta.type, value: res.data })
                resolve(res.data)
              }).catch(error => {
                reject(error)
              })
            })
          }
        },
      },
    },
  })
}

export default {
  install,
}