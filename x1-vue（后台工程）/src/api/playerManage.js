import request from '@/utils/request'

// 玩家管理 模块

// ---------------获取数据-----------------//

// 玩家信息管理
export function playerInfo(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/player/data.auth',
    method: 'post',
    data: formData
  })
}

// 白名单管理
export function whiteList(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/whiteList/data.auth',
    method: 'post',
    data: formData
  })
}

// 金手指名单
export function godFinger(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/godFinger/data.auth',
    method: 'post',
    data: formData
  })
}

// 封禁账号
export function forbidList(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/forbidList/data.auth',
    method: 'post',
    data: formData
  })
}

// ------------------新增------------------//

// 白名单管理
export function AddwhiteList(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/whiteList/add.authw',
    method: 'post',
    data: formData
  })
}

// 封禁账号
export function addForbidList(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/forbidList/add.authw',
    method: 'post',
    data: formData
  })
}

// -------------可新增、可编辑-------------//

// 金手指名单
export function saveGodFinger(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/godFinger/save.authw',
    method: 'post',
    data: formData
  })
}

// ------------------删除------------------//

//  白名单管理
export function deletewhiteList(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/whiteList/delete.authw',
    method: 'post',
    data: formData
  })
}

//  金手指名单
export function deleteGodFinger(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/godFinger/delete.authw',
    method: 'post',
    data: formData
  })
}

//  封禁账号
export function deleteForbidListr(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/forbidList/delete.authw',
    method: 'post',
    data: formData
  })
}

// ---------------------------------------//

// 玩家数据详情（玩家账号信息）
export function playerDetail(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/player/detail.auth',
    method: 'post',
    data: formData
  })
}

// 玩家英雄数据（伙伴信息）
export function playerHero(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/player/hero.auth',
    method: 'post',
    data: formData
  })
}

// 在线情况
export function onlineSituation(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/player/detail.auth',
    method: 'post',
    data: formData
  })
}

// 玩家背包
export function playerBag(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/player/bag.auth',
    method: 'post',
    data: formData
  })
}

// 封禁玩家
export function blockPlayer(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerCurrOnline/forbid.auth',
    method: 'post',
    data: formData
  })
}

// 踢玩家下线
export function KickoutPlayer(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerCurrOnline/forcedOffLine.auth',
    method: 'post',
    data: formData
  })
}
