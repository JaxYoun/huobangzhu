import { request, config, jsonToUrlencoded } from 'utils'

const { api } = config
const { userLogin, user, switchPost } = api

export async function login (params) {
  let bodyData = jsonToUrlencoded(params)
  return request(userLogin, {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
    body: bodyData,
    showMsg: false,
  }).then((res) => {
    let { success, message } = res
    return {
      success,
      message,
    }
  })
}
export async function userPost () {
  return request(user)
}
export async function selectPost (params) {
  return request(switchPost, {
    body: JSON.stringify({ currentPostId: params.postId }),
    showMsg: false,
  })
}
