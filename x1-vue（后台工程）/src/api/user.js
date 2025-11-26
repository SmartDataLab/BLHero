
import request from '@/utils/request'

// -----用户相关----- //

// 登录
export function login(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/login.gate',
    method: 'post',
    data: formData
  })
}

// 修改密码
export function changePasssword(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/changePasssword.do',
    method: 'post',
    data: formData
  })
}
