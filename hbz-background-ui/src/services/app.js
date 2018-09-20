import { request, config } from 'utils'

const { api } = config
const { user, userLogout, userLogin } = api

export async function login (params) {
  return request({
    url: userLogin,
    data: params,
  })
}

export async function logout () {
  return request(userLogout, { showMsg: false })
}

export async function query () {
  return request(user, { showMsg: false }).then((json) => {
    let response = {}
    let userInfo = {}
    if (Number(json.code) === 200 && json.data.menuIds) {
      userInfo.userName = json.data.userName
      userInfo.id = json.data.id
      userInfo.orgId = json.data.orgId
      userInfo.menuList = json.data.menuIds
      userInfo.currentPostId = json.data.currentPostId
      userInfo.posts = json.data.posts
      userInfo.permissions = {
        visit: [],
      }
      json.data.menuIds = json.data.menuIds ? json.data.menuIds : []
      for (let item of json.data.menuIds.values()) {
        userInfo.permissions.visit.push(item.id)
      }
      response = {
        success: true,
        user: userInfo,
      }
    }
    return response
  })
}

export async function typeval (type) {
  return request(api.typeval,
    {
      showMsg: false,
      body: JSON.stringify({ type }),
    })
}
