import router from '@/router'
import store from '@/store'
import axios from 'axios'
import { Message } from 'element-ui'
export const baseApiUrl = process.env.VUE_APP_BASE_API

const service = axios.create({
  baseURL: baseApiUrl, // url = base url + request url
  timeout: 5000 // request timeout
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 设置token
    if (store.state.user.token) {
      config.headers['token'] = store.state.user.token
    }

    // 设置请求头
    if (config.method.toUpperCase() === 'post') {
      config.headers['Content-Type'] = 'multipart/form-data'
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    if (typeof response.data !== 'object') {
      store.dispatch('user/logout')
      router.replace('/login?type=error') // 正常应该跳转错误页面 500
      // router.replace('/500?type=error')
    }

    if (response.data.code !== 0) {
      Message.error(response.data.message)
    }

    return response
  },
  err => {
    // 给出提示
    let message = err.message
    if (message === 'Network Error') {
      message = '后端接口连接异常'
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时'
    } else if (message.includes('Request failed with status code')) {
      message = '系统接口' + message.substr(message.length - 3) + '异常'
    }
    Message.error(message)
    return Promise.reject(err)
  }
)

export default service
