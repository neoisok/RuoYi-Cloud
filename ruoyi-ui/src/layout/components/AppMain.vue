<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <!-- keep-alive 缓存 缓存组件，切换页面时保留页面状态（如滚动位置、表单数据）-->
      <keep-alive :include="cachedViews">
        <router-view v-if="!$route.meta.link" :key="key" />
      </keep-alive>
    </transition>
    <!-- <iframe-toggle /> -->
  </section>
</template>

<script>
import iframeToggle from "./IframeToggle/index"

export default {
  name: 'AppMain',
  components: { iframeToggle },
  computed: {
    // 返回需要缓存的组件名称列表
    cachedViews() {
      console.log("计算属性 cachedViews()调用。")
      console.log(this.$store.state.tagsView.cachedViews)
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      console.log("计算属性 key()调用。")
      console.log(this.$route.path)
      return this.$route.path
    }
  },
  watch: {
    // 监听路由变化
    $route() {
      this.addIframe()
    }
  },
  mounted() {
    console.log("mounted()mounted()mounted()")
    this.addIframe()
  },
  methods: {
    addIframe() {
      const {name} = this.$route
      console.log("addIframeaddIframeaddIframe")
      console.log(this.$route.meta.link)
      if (name && this.$route.meta.link) {
        console.log("")
        this.$store.dispatch('tagsView/addIframeView', this.$route)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 6px;
  }
}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>
