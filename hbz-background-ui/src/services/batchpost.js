import { request } from 'utils'
import { api } from '../routes/system/utils'

export async function queryPostList (param) {
  return request(api.queryPostOrgTree, { data: param })
}

export async function queryRoleList (param) {
  return request(api.roleList, { data: param })
}

export async function onDelete (param) {
  return request(api.batchDeletePost, { data: param })
}

export async function getPost (param) {
  return request(api.getPost, { data: param })
}

export async function savePostAndRole (param) {
  return request(api.savePostAndRole, { data: param })
}

export async function save (param) {
  return request(api.batchSaveOrUpdatePost, { data: param })
}

export async function findRoleIdsByPost (param) {
  return request(api.findRoleIdsByPost, { data: param })
}
