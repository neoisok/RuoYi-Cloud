<script>
// 这是一个函数式组件，用于渲染菜单项中的图标和标题。它是 Element UI 菜单组件的辅助组件。
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  },
  // 函数式组件 - 无 this，用 contex 所有外部传入的信息都通过 context 参数传递
  render(h, context) {
    // h 是 createElement 的缩写，用于创建虚拟 DOM 节点
    const { icon, title } = context.props // // 从 context 中获取 props
    // 因为 render 函数需要返回一个虚拟节点或虚拟节点数组，而这个组件要渲染的内容不固定
    const vnodes = [] // 虚拟节点数组

    // 1. 有图标：添加图标组件
    if (icon) {
      <svg-icon icon-class={icon}/>

    // 编译后自动变成
    // h('svg-icon', { props: { iconClass: icon } })
      vnodes.push(<svg-icon icon-class={icon}/>)
    }

    // 2. 有标题：添加标题文本
    if (title) {
      if (title.length > 5) {
        // 标题超过5个字：添加 tooltip 效果
        // 这里是jsx 正常的 Vue 模板应该在 <template> 标签里，而不是在 JavaScript 里写 HTML。
        vnodes.push(<span slot='title' title={(title)}>{(title)}</span>)
      } else {
        vnodes.push(<span slot='title'>{(title)}</span>)
      }
    }
    return vnodes
  }
}
</script>
