import request from '@/utils/request'

// 特殊操作 模块

// ---------------获取数据-----------------//

// 热更代码
export function fixCode(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/fixCode/data.auth',
    method: 'post',
    data: formData
  })
}

// 热更代码结果
export function fixCodeResult(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/fixCodeResult/data.auth',
    method: 'post',
    data: formData
  })
}

// 热更配置
export function fixDesign(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/fixDesign/data.auth',
    method: 'post',
    data: formData
  })
}

// 热更配置结果
export function fixDesignResult(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/fixDesignResult/data.auth',
    method: 'post',
    data: formData
  })
}

// 流水事件
export function waterEvent(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameCause/data.auth',
    method: 'post',
    data: formData
  })
}

// 道具配置
export function propsConfig(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/itemCfg/data.auth',
    method: 'post',
    data: formData
  })
}

// 兑换码生成
export function giftCode(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/giftCode/data.auth',
    method: 'post',
    data: formData
  })
}

// 通用兑换码日志
export function giftCodeShareLog(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/giftCodeShareLog/data.auth',
    method: 'post',
    data: formData
  })
}

// 兑换码礼包配置
export function giftCodeCfg(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/giftCodeCfg/data.auth',
    method: 'post',
    data: formData
  })
}

// 客户端日志
export function clientLog(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/clientLog/data.auth',
    method: 'post',
    data: formData
  })
}

// 充值商品配置
export function rechargeProductCfg(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/rechargeProductCfg/data.auth',
    method: 'post',
    data: formData
  })
}

// ------------- 新增 -----------------//

// 礼包码
export function saveGiftCode(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/giftCode/save.authw',
    method: 'post',
    data: formData
  })
}

// --------------------------------------

// 更新流水事件
export function updateWaterEvent() {
  return request({
    url: '/gameCause/refresh.auth',
    method: 'post'
  })
}

// 流水事件下拉数据
export function waterEventOpt() {
  return request({
    url: '/gameCause/options.do',
    method: 'post'
  })
}

// 更新游戏道具数据
export function updateGameProps() {
  return request({
    url: '/itemCfg/refresh.auth',
    method: 'post'
  })
}

// 道具下拉菜单数据
export function propsOpt() {
  return request({
    url: '/itemCfg/options.do',
    method: 'post'
  })
}

// 重发热更配置
export function resendFixDesign(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/fixDesign/resend.auth',
    method: 'post',
    data: formData
  })
}

// 重发热更代码
export function resendFixCode(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/fixCode/resend.auth',
    method: 'post',
    data: formData
  })
}

// 兑换码配置下拉菜单数据
export function codeCfgOpt() {
  return request({
    url: '/giftCodeCfg/options.do',
    method: 'post'
  })
}

// 更新兑换码配置数据
export function updateCodeCfg() {
  return request({
    url: '/giftCodeCfg/refresh.auth',
    method: 'post'
  })
}

// 更新充值商品配置数据
export function updateRechargeProductCfg(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/rechargeProductCfg/refresh.auth',
    method: 'post',
    data: formData
  })
}
