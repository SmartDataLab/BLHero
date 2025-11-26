import request from '@/utils/request'

// -----------用户设置模块-----------//

// 用户管理
export function userManagement(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/user/data.auth',
    method: 'post',
    data: formData
  })
}

// 用户权限管理
export function userRight(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/userfunction/data.auth',
    method: 'post',
    data: formData
  })
}

// 用户操作日志
export function userLog(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/userlog/data.auth',
    method: 'post',
    data: formData
  })
}

// ------------------新增or编辑------------------//

// 用户管理
export function saveUser(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/user/save.authw',
    method: 'post',
    data: formData
  })
}

// ------------------删除------------------//

// 用户管理
export function deleteUser(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/user/delete.authw',
    method: 'post',
    data: formData
  })
}

// ---------------------------------------

// 权限菜单下拉数据
export function funcOptions(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/userfunction/options.auth',
    method: 'post',
    data: formData
  })
}

// 用户权限设置
export function saveUserRight(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/userfunction/save.authw',
    method: 'post',
    data: formData
  })
}
