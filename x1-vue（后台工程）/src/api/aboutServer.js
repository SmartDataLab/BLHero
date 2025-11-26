import request from '@/utils/request'

/* ----------关于服务器---------*/

// 服务器-下拉菜单选项
// export function serverOpt(data) {
//   const formData = new FormData()
//   for (const key in data) {
//     formData.append(key, data[key])
//   }
//   return request({
//     url: '/gameServer/options.do',
//     method: 'post',
//     data: formData
//   })
// }

// 根据当前渠道获取服务器下拉菜单数据
export function multiServer() {
  return request({
    url: '/gameServer/optionsInCurrChannel.do',
    method: 'post'
  })
}
