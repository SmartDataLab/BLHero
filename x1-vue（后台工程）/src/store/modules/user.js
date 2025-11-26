import { login } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

export default {
  namespaced: true,
  state: {
    token: getToken()
  },

  mutations: {
    set_token(state, token) {
      state.token = token
      setToken(token)
    },
    remove_token(state) {
      state.token = null
      removeToken()
    }
  },

  actions: {

    // 登录
    async Newlogin(store, data) {
      const res = await login(data)
      store.commit('set_token', res.data.data.token)
    },

    // 退出登录
    logout(store) {
      store.commit('remove_token')
    }

  }
}

