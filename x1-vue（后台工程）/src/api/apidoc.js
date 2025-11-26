
import request from '@/utils/request'

// -----------接口说明模块---------//

// 接口说明
export function apidoc(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/apidoc/data.auth',
    method: 'post',
    data: formData
  })
}
