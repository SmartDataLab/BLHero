import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

import 'lib-flexible/flexible'
import * as echarts from 'echarts'

import 'default-passive-events'

// import VueSmoothScroll from 'vue2-smooth-scroll'// 隐藏所有滚动条

// 防抖
import { debounce } from 'lodash'
Vue.prototype.$debounce = debounce

import moment from 'moment'
Vue.prototype.$moment = moment

// 时间过滤器（获取到的该时间戳为毫秒）
Vue.filter('formatTime', function(timestamp) {
  return moment(timestamp).format('YYYY-MM-DD HH:mm:ss')
})
// 今日在线时长过滤器（获取到的该时间戳为秒）
Vue.filter('formatDuration', function(timestamp) {
  return moment.utc(timestamp * 1000).format('HH时mm分ss秒')
})

// 过滤器（获取到的该时间戳为秒）-天时分秒
// Vue.filter('formatNumDays', function(timestamp) {
//   const duration = moment.duration(timestamp, 'seconds')
//   const day = duration.asDays()
//   const hours = duration.hours()
//   const minutes = duration.minutes()
//   const seconds = duration.seconds()
//   return Math.floor(day) + '天' + hours + '时' + minutes + '分' + seconds + '秒'
// })

Vue.prototype.$echarts = echarts

// 全局修改默认配置，点击空白处不能关闭弹窗
ElementUI.Dialog.props.closeOnClickModal.default = false
// 全局修改默认配置，按下ESC不能关闭弹窗
ElementUI.Dialog.props.closeOnPressEscape.default = false

// Vue.use(VueSmoothScroll) // 隐藏所有滚动条

ElementUI.Dialog.props.lockScroll.default = false// 弹出框的时候滚动条隐藏和出现导致页面抖动问题

Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
