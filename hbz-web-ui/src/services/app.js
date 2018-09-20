import { request, config } from 'utils'

const { api } = config
const { userRoleMenu, userLogout, userLogin, token } = api

export async function login (params) {
  return request({
    url: userLogin,
    data: params,
  })
}

export async function publicTypeval (param) {
  return request(api.typeval, { data: param })
}

export async function logout () {
  return request(userLogout, { showMsg: false })
}

export async function query () {
  return request(userRoleMenu, {
    showMsg: false,
  }).then((json) => {
    const { success, data = {} } = json
    const {
      EnterpriseConsignorMenuList = [],
      EnterpriseDriverMenuList = [],
      EnterprisePersonalCenterMenuList = [],
      EnterpriseStorageCenterMenuList = [],
      user = {},
    } = data
    console.log('EnterpriseStorageCenterMenuList', EnterpriseStorageCenterMenuList)
    const userMenu = {
      success,
      user,
      menus: {
        EnterpriseConsignor: EnterpriseConsignorMenuList,
        EnterpriseDriver: EnterpriseDriverMenuList,
        EnterprisePersonalCenter: EnterprisePersonalCenterMenuList,
        EnterpriseStorageCenter: EnterpriseStorageCenterMenuList,
      },
    }
    return userMenu
  })
}

export async function enums (enumname) {
  return request(api.enums,
    {
      showMsg: false,
      body: JSON.stringify({ enumname }),
    })
}

export async function queryUserList (params) {
  return request(api.queryUserList,
    {
      showMsg: false,
      body: JSON.stringify(params),
    })
}
// export async function query () {
//   return request(user, { showMsg: false }).then((json) => {
//     let response = {}
//     let userInfo = {}
//     if (Number(json.code) === 200 && json.data.menuIds) {
//       userInfo.userName = json.data.userName
//       userInfo.id = json.data.id
//       userInfo.orgId = json.data.orgId
//       userInfo.menuList = json.data.menuIds
//       userInfo.currentPostId = json.data.currentPostId
//       userInfo.posts = json.data.posts
//       userInfo.permissions = {
//         visit: [],
//       }
//       json.data.menuIds = json.data.menuIds ? json.data.menuIds : []
//       for (let item of json.data.menuIds.values()) {
//         userInfo.permissions.visit.push(item.id)
//       }
//       response = {
//         success: true,
//         user: userInfo,
//       }
//     }
//     return response
//   })
// }

export async function getToken () {
  return request(token, { showMsg: false })
}
