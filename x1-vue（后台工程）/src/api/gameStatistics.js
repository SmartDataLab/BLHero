import request from '@/utils/request'

// 游戏统计数据 模块

// ---------------获取数据-----------------//

// 付费LTV
export function playerLtv(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerLtv/data.auth',
    method: 'post',
    data: formData
  })
}

// 玩家留存
export function playerRemain(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerRemain/data.auth',
    method: 'post',
    data: formData
  })
}

// 付费留存
export function playerPayRemain(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerPayRemain/data.auth',
    method: 'post',
    data: formData
  })
}

// 注册分时
export function playerTimeLog(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerTimeLog/data.auth',
    method: 'post',
    data: formData
  })
}

// 在线人数
export function playerOnlineLog(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerOnlineLog/data.auth',
    method: 'post',
    data: formData
  })
}

// 在线时长占比
export function onlineTimeRatio(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerScatterLog/data.auth',
    method: 'post',
    data: formData
  })
}

// 游戏汇总
export function gameSum(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/serverResume/data.auth',
    method: 'post',
    data: formData
  })
}

// 货币消费统计
export function consumeSum(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/consume/data.auth',
    method: 'post',
    data: formData
  })
}

// 登录数据打点
export function loginDot(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/loginDotData/data.auth',
    method: 'post',
    data: formData
  })
}

// 任务数据打点
export function taskDot(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/taskDotData/data.auth',
    method: 'post',
    data: formData
  })
}

// 引导数据打点
export function guideDot(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/guideDotData/data.auth',
    method: 'post',
    data: formData
  })
}

// 招募数据打点
export function recruitDot(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/recruitDotData/data.auth',
    method: 'post',
    data: formData
  })
}

// 招募刷新数据打点
export function recruitRefreshDot(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/recruitRefreshDotData/data.auth',
    method: 'post',
    data: formData
  })
}

// BOSS击杀数据打点
export function bossDot(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/bossDotData/data.auth',
    method: 'post',
    data: formData
  })
}

// 场景区域解锁数据打点
export function fogDot(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/fogDotData/data.auth',
    method: 'post',
    data: formData
  })
}

// ---------------其他-----------------//

// 手动触发昨天LTV数据统计
export function manualTriggerLTV(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerLtv/manual.auth',
    method: 'post',
    data: formData
  })
}
