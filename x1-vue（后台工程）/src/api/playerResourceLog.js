import request from '@/utils/request'

// 玩家流水查询 模块

// ---------------获取数据-----------------//

// 金币日志
export function goldLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/goldLogs.auth',
    method: 'post',
    data: formData
  })
}

// 已删邮件日志
export function mailLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/mailLogs.auth',
    method: 'post',
    data: formData
  })
}

// 矿日志
export function mineLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/mineLogs.auth',
    method: 'post',
    data: formData
  })
}

// 肉日志
export function meatLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/meatLogs.auth',
    method: 'post',
    data: formData
  })
}

// 钻石日志
export function diamondLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/diamondLogs.auth',
    method: 'post',
    data: formData
  })
}

// 道具日志
export function itemLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/itemLogs.auth',
    method: 'post',
    data: formData
  })
}

// 角色经验日志
export function playerExpLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/playerExpLogs.auth',
    method: 'post',
    data: formData
  })
}

// 英雄日志
export function heroLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/heroLogs.auth',
    method: 'post',
    data: formData
  })
}

// 装备日志
export function equipLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/equipLogs.auth',
    method: 'post',
    data: formData
  })
}

// 木日志
export function woodLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/woodLogs.auth',
    method: 'post',
    data: formData
  })
}

// 英雄战力日志
export function heroFightingLogs(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamelog/heroFightingLogs.auth',
    method: 'post',
    data: formData
  })
}
