import request from '@/utils/request'

/* ----------关于渠道---------*/

// 切换渠道
export function changeChannel(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameChannel/change.do',
    method: 'post',
    data: formData
  })
}

// 渠道-下拉菜单选项
export function channelOpt() {
  return request({
    url: '/gameChannel/options.do',
    method: 'post'
  })
}

// 渠道下当前的服务器
export function channelServerOpt(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameChannelServer/currRelation.auth',
    method: 'post',
    data: formData
  })
}

// 渠道大区下拉菜单
export function gameRegionOpt(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/gameRegion/options.do',
    method: 'post',
    data: formData
  })
}
