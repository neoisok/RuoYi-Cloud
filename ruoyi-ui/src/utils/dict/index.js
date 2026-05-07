import Dict from './Dict'
import { mergeOptions } from './DictOptions'

export default function (Vue, options) {
  mergeOptions(options)
  // 全局混入每个组件，会以影响到所有组件
  Vue.mixin({
    data() {
      // 条件1：组件没有选项 条件2：组件没有 dicts 属性 条件3：dicts 的值是 null
      if (this.$options === undefined || this.$options.dicts === undefined || this.$options.dicts === null) {
        return {}
      }
      const dict = new Dict()
      dict.owner = this
      return {
        dict
      }
    },
    created() {
      // 打印日志
      // console.log('created 执行，组件名:', this.$options.name, '是否有 dicts:', !!this.$options.dicts)
      
      if (!(this.dict instanceof Dict)) {
        return
      }
      /*
      if (options.onCreated) {
        options.onCreated(this.dict)
      }
      */
      options.onCreated && options.onCreated(this.dict)
      this.dict.init(this.$options.dicts).then(() => {
        options.onReady && options.onReady(this.dict)
        // 等待 DOM 更新完成后，再执行里面的代码
        this.$nextTick(() => {
          this.$emit('dictReady', this.dict)
          if (this.$options.methods && this.$options.methods.onDictReady instanceof Function) {
            this.$options.methods.onDictReady.call(this, this.dict)
          }
        })
      })
    },
  })
}
