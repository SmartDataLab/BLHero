import router, { constantRoutes } from '@/router'
import { getRouters } from '@/api/routersMenu'
import Layout from '@/layout'

const state = {
  routes: [],
  addRoutes: [],
  navbarIndex: localStorage.getItem('navbarIndex') || 0
}
const mutations = {
  SET_ROUTES: (state, routes) => {
    state.routes = state.routes.filter(item => item.path !== '*')
    state.addRoutes = routes
    // 拼接公共路由和动态路由
    state.routes = constantRoutes.concat(routes)
    router.addRoutes(routes)
  },
  SET_NAVBAR_INDEX(state, index) {
    state.navbarIndex = index
    localStorage.setItem('navbarIndex', index)
  }
}

const actions = {

  // 生成路由
  GenerateRoutes({ commit }) {
    return new Promise((resolve) => {
      // 请求路由数据
      getRouters().then((res) => {
        const accessedRoutes = res.data.data.systemMenus
        // console.log(24, accessedRoutes)
        accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
        commit('SET_ROUTES', handleRoutes(accessedRoutes))
        resolve(accessedRoutes)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

// 处理路由
function handleRoutes(routeArr) {
  return routeArr.map(route => {
    if (route.children) {
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component) {
        route.component = loadLayout(route.component) // 整体使用才是懒加载
      }
      route.children = handleRoutes(route.children)
    } else {
      route.component = loadView(route.component) // (resolve) => require([`@/views${name}/${route.name}`], resolve)
    }
    return route
  })
}

export const loadLayout = (view) => {
  // 路由懒加载  === import 同  require 同义  都是路由懒加载
  return (resolve) => require([`@/${view}`], resolve) // 整体使用才是懒加载
}
export const loadView = (view) => {
  // 路由懒加载  === import 同  require 同义  都是路由懒加载
  return (resolve) => require([`@/views/${view}`], resolve) // 整体使用才是懒加载
}

