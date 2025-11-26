import request from '@/utils/request'

// 系统邮件相关 模块

// ---------------获取数据-----------------//

// 玩家邮件
export function playerMails(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerMail/data.auth',
    method: 'post',
    data: formData
  })
}

// 系统邮件
export function mailDatas(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/mail/data.auth',
    method: 'post',
    data: formData
  })
}

// 系统邮件发送结果
export function mailResult(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/mail/mailResult.auth',
    method: 'post',
    data: formData
  })
}

// ------------------可新增、可编辑------------------//

// 系统邮件
export function saveMail(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/mail/save.authw',
    method: 'post',
    data: formData
  })
}

// ------------------删除------------------//

// 系统邮件
export function deleteMail(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/mail/delete.authw',
    method: 'post',
    data: formData
  })
}

// 玩家邮件
export function deletePlayerMail(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/playerMail/delete.authw',
    method: 'post',
    data: formData
  })
}

// ----------------------------------------//

// 系统邮件-审核通过
export function mailPass(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/mail/checkPass.authw',
    method: 'post',
    data: formData
  })
}

// 系统邮件-审核不通过
export function mailNotPass(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/mail/checkNotPass.authw',
    method: 'post',
    data: formData
  })
}

// 系统邮件-重新发送
export function mailResend(data) {
  const formData = new FormData()
  for (const key in data) {
    formData.append(key, data[key])
  }
  return request({
    url: '/mail/resend.auth',
    method: 'post',
    data: formData
  })
}
