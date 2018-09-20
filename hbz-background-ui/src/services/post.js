import { request } from 'utils'
import { api } from '../routes/system/utils'

export async function queryPostList (param) {
  return request(api.queryPostOrgTree, { data: param })
}

export async function queryRoleList (param) {
  return request(api.roleList, { data: param })
}

export async function onDelete (param) {
  return request(api.postDel, { data: param })
}

export async function getPost (param) {
  return request(api.getPost, { data: param })
}

export async function saveRoleLimit (param) {
  return request(api.saveRoleLimit, { data: param })
}

export async function save (param) {
  return request(api.postSave, { data: param })
}

export async function queryRolesByPostId (param) {
  return request(api.queryRolesByPostId, { data: param })
}