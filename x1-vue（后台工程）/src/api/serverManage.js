import request from '@/utils/request'

// 服务器管理 模块

// -----------------获取数据------------------//

// 服务器管理
export function gameServer(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameServer/data.auth',
    method: 'post',
    data: formData
  })
}

// 渠道与服务器关系
export function gameChannelServer(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameChannelServer/data.auth',
    method: 'post',
    data: formData
  })
}

// 渠道管理
export function gameChannel(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameChannel/data.auth',
    method: 'post',
    data: formData
  })
}

// 渠道大区管理
export function gameRegion(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameRegion/data.auth',
    method: 'post',
    data: formData
  })
}

// 开服管理
export function serverOpen(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/serverOpen/data.auth',
    method: 'post',
    data: formData
  })
}

// 渠道权限管理
export function userChannel(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/userChannel/data.auth',
    method: 'post',
    data: formData
  })
}

// 游戏公告管理
export function gameBulletin(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/bulletin/data.auth',
    method: 'post',
    data: formData
  })
}

// 平台管理
export function gamePlatform(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamePlatform/data.auth',
    method: 'post',
    data: formData
  })
}

// 服务器维护管理
export function serverMaintain(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameServerMaintain/data.auth',
    method: 'post',
    data: formData
  })
}

// 客户端版本管理
export function clientVersion(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/clientVersion/data.auth',
    method: 'post',
    data: formData
  })
}

// ----------------可新增、可编辑-------------------//

// 服务器管理
export function saveGameServer(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameServer/save.authw',
    method: 'post',
    data: formData
  })
}

// 渠道管理
export function saveGameChannel(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameChannel/save.authw',
    method: 'post',
    data: formData
  })
}

// 渠道大区管理
export function saveGameRegion(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameRegion/save.authw',
    method: 'post',
    data: formData
  })
}

// 游戏公告管理
export function saveGameBulletin(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/bulletin/save.authw',
    method: 'post',
    data: formData
  })
}

// 平台管理
export function savaGamePlatform(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gamePlatform/save.authw',
    method: 'post',
    data: formData
  })
}

// 客户端版本管理
export function savaClientVersion(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/clientVersion/save.authw',
    method: 'post',
    data: formData
  })
}

// -----------------新增----------------------//

// 渠道与服务器关系
export function addGameChannelServer(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameChannelServer/save.authw',
    method: 'post',
    data: formData
  })
}

// ------------------编辑---------------------//

// 渠道与服务器关系
export function editGameChannelServer(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameChannelServer/saveToRegion.authw',
    method: 'post',
    data: formData
  })
}

// 渠道权限管理
export function editUserChannel(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/userChannel/save.authw',
    method: 'post',
    data: formData
  })
}

// -------------------删除--------------------//

// 渠道大区管理
export function delGameRegion(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameRegion/delete.authw',
    method: 'post',
    data: formData
  })
}

// 渠道管理
export function delGameChannel(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameChannel/delete.authw',
    method: 'post',
    data: formData
  })
}

// 游戏公告管理
export function delGameBulletin(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/bulletin/delete.authw',
    method: 'post',
    data: formData
  })
}

// 客户端版本管理
export function delClientVersion(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/clientVersion/delete.authw',
    method: 'post',
    data: formData
  })
}

// -------------------------------------------//

// 设置服务器预期开服时间
export function setOpenTime(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/serverOpen/editOpenTime.authw',
    method: 'post',
    data: formData
  })
}

// 立刻开服
export function nowOpenServer(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/serverOpen/openNow.authw',
    method: 'post',
    data: formData
  })
}

// 可授权得渠道数据下拉菜单
export function accreditOptions(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/userChannel/options.auth',
    method: 'post',
    data: formData
  })
}

// 公告下拉菜单
export function bulletinOptions() {
  return request({
    url: '/bulletin/options.do',
    method: 'post'
  })
}

// 平台管理下拉菜单
export function platformOptions() {
  return request({
    url: '/gamePlatform/options.do',
    method: 'post'
  })
}

// 进行服务器维护
export function conductMaintain(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameServerMaintain/maintain.authw',
    method: 'post',
    data: formData
  })
}

// 重新发送进行服务器维护
export function resendMaintain(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameServerMaintain/resendMaintain.authw',
    method: 'post',
    data: formData
  })
}

// 结束服务器维护
export function endMaintain(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameServerMaintain/endMaintain.authw',
    method: 'post',
    data: formData
  })
}

// 重新发送结束服务器维护
export function resendEndMaintain(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameServerMaintain/resendEndMaintain.authw',
    method: 'post',
    data: formData
  })
}
