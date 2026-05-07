<template>
  <!-- 实现了根据链接类型（内部路由 vs. 外部链接）动态切换为 <router-link> 或 <a> 标签，并自动绑定对应的属性。 -->
  <!-- v-bind="对象"  当用于绑定 class 或 style attribute，v-bind 支持额外的值类型如数组或对象-->
  <component :is="type" v-bind="linkProps(to)">
    <slot />
  </component>
</template>

<script>
// @ 是指src 构建工具（Webpack、Vite 等）在项目初始化时自动设置
import { isExternal } from '@/utils/validate'

export default {
  props: {
    to: {
      type: [String, Object],
      required: true
    }
  },
  computed: {
    isExternal() {
      return isExternal(this.to)
    },
    type() {
      if (this.isExternal) {
        return 'a'
      }
      return 'router-link'
    }
  },
  methods: {
    linkProps(to) {
      if (this.isExternal) {
        return {
          href: to,
          target: '_blank',
          rel: 'noopener'
        }
      }
      return {
        to: to
      }
    }
  }
}
</script>
