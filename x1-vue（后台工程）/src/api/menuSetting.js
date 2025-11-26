import request from '@/utils/request'

// 菜单设置 模块

// ---------------获取数据-----------------//

// 功能菜单
export function functionMenu(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/functionmenu/data.auth',
    method: 'post',
    data: formData
  })
}

// 系统菜单
export function systemMenu(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/systemmenu/data.auth',
    method: 'post',
    data: formData
  })
}

// 模块菜单
export function moduleMenu(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/modulemenu/data.auth',
    method: 'post',
    data: formData
  })
}

// -----------------编辑-------------------//

// 功能菜单
export function updateFunctionMenu(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/functionmenu/update.authw',
    method: 'post',
    data: formData
  })
}

// 系统菜单
export function updateSystemMenu(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/systemmenu/update.authw',
    method: 'post',
    data: formData
  })
}

// 模块菜单
export function updateModuleMenu(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/modulemenu/update.authw',
    method: 'post',
    data: formData
  })
}
