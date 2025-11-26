import request from '@/utils/request'

// 充值相关 模块

// ---------------获取数据-----------------//

// 充值回调查询
export function recharge(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/recharge/data.auth',
    method: 'post',
    data: formData
  })
}

// 充值排名
export function rechargeRank(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/rechargeRank/data.auth',
    method: 'post',
    data: formData
  })
}

// 充值商品项目统计
export function rechargeStats(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/rechargeProductCount/data.auth',
    method: 'post',
    data: formData
  })
}

// 虚拟充值(内部充值)
export function rechargeVirtual(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/rechargeVirtual/data.auth',
    method: 'post',
    data: formData
  })
}

// ------------------新增------------------//

// 发放-虚拟充值(内部充值)
export function AddRechargeVirtual(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/rechargeVirtual/add.authw',
    method: 'post',
    data: formData
  })
}

// ------------------其他------------------//

// 人工补单
export function manualRecharge(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/recharge/manual.authw',
    method: 'post',
    data: formData
  })
}
