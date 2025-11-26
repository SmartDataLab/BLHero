
import request from '@/utils/request'

// -----路由菜单----- //

export function getRouters() {
  return request({
    url: '/menu.do',
    method: 'post'
  })
}

