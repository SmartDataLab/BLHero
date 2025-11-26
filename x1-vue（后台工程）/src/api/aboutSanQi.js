import request from '@/utils/request'

// 37互娱相关

// ---------------获取数据-----------------//

// 37互娱白名单
export function sanQiHuYuWhiteIp() {
  return request({
    url: '/sanQiHuYuWhiteIp/data.auth',
    method: 'post'
  })
}

// 37互娱发放礼包配置
export function sanQiHuYuGiftCfg(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/sanQiHuYuGiftCfg/data.auth',
    method: 'post',
    data: formData
  })
}

// 37互娱渠道参数设置
export function sanQiHuYuSetting(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/sanQiHuYuSetting/data.auth',
    method: 'post',
    data: formData
  })
}

// -------------可新增、可编辑-------------//

// 37互娱白名单
export function saveSanQiHuYuWhiteIp(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/sanQiHuYuWhiteIp/save.authw',
    method: 'post',
    data: formData
  })
}

// 37互娱渠道参数设置
export function savaSanQiHuYuSetting(id) {
  const formData = new FormData()
  formData.append('platformId', id)
  return request({
    url: '/sanQiHuYuSetting/save.authw',
    method: 'post',
    data: formData
  })
}

// 37互娱发放礼包配置
export function saveSanQiHuYuGiftCfg(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/sanQiHuYuGiftCfg/save.auth',
    method: 'post',
    data: formData
  })
}

// ------------------删除------------------//

// 37互娱白名单
export function deleteSanQiWhiteIp(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/sanQiHuYuWhiteIp/delete.authw',
    method: 'post',
    data: formData
  })
}

// 37互娱发放礼包配置
export function deleteSanQiHuYuGiftCfg(id) {
  const formData = new FormData()
  formData.append('id', id)
  return request({
    url: '/sanQiHuYuGiftCfg/delete.authw',
    method: 'post',
    data: formData
  })
}
