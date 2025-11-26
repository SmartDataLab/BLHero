<template>
  <div :class="classObj">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />

    <div class="myMenu">
      <!-- 一级路由 -->
      <div v-for="(item,j) in routeMeta" :key="j" class="menuIten" :class="{ active: index === j }" @click="activeMata(j)">{{ item.meta.title }}</div>

      <!-- 用户名和退出按钮 -->
      <navbar class="navbar" />
    </div>

    <!-- 二级路由 -->
    <sidebar :children="routeMeta[index].children" :base-path="routeMeta[index].path" class="sidebar-container" />

    <div class="main-container">
      <div :class="{'fixed-header':fixedHeader}" />
      <!-- 小标签 -->
      <TagsView />
      <app-main />
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'
import TagsView from './components/TagsView/index.vue'
import ResizeMixin from './mixin/ResizeHandler'

export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    TagsView
  },
  mixins: [ResizeMixin],
  data() {
    return {
      index: 0
    }
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },

    // 路由
    routeMeta() {
      const routerArr = this.$store.state.router.routes.filter((item) => {
        return item.meta
      })
      return routerArr
    },

    fixedHeader() {
      return this.$store.state.settings.fixedHeader
    },
    classObj() {
      return {
        // hideSidebar: !this.sidebar.opened,
        // openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },
    activeMata(j) {
      this.index = j
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  @import "~@/styles/variables.scss";

  // 一级路由
  .myMenu{
    z-index:3000;
    position: relative;
    background-color: #23262f;
    display: flex;
    height: 65px;
    padding-left: 210px;

    .menuIten{
      color: #fff;
      font-size: 15px;
      line-height: 65px;
      padding-left: 30px;
      padding-right: 30px;
   }

     // 用户名和退出按钮
    .navbar{
     position: absolute;
     right: 5px;
    }
  }

  .active{
      color: #409eff !important;
      border-bottom: 3px solid #409eff;
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$sideBarWidth});
    transition: width 0.28s;
  }
</style>
