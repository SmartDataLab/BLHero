
// 导航守卫

import router from './router'
import store from './store'

router.beforeEach(async(to, from, next) => {
  if (store.state.user.token) {
    if (!store.state.router.routes.length && to.query.type !== 'error') {
      await store.dispatch('router/GenerateRoutes')
      next(to.path)
    } else {
      // 已登录状态
      if (to.path === '/login') {
        next('/')
      } else {
        next()
      }
    }
  } else {
    // 未登录状态
    if (to.path === '/login' || to.path === '/404') {
      next()
    } else {
      next('/login')
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  // NProgress.done()
})
