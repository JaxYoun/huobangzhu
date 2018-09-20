import { request } from 'utils'
import { api } from '../routes/system/utils'

export async function queryRoleList (params) {
  return request(api.roleList, { data: params })
}
export async function queryMenuList (params) {
  return request(api.queryMenuList, { data: params })
}
export async function queryMenuListByRoleId (params) {
  return request(api.queryMenuListByRoleId, { data: params })
}
export async function saveMenuLimit (params) {
  return request(api.saveMenuLimit, { data: params })
}
export async function deleteRole (params) {
  return request(api.roleDel, { data: params })
}
export async function save (params) {
  return request(api.roleSave, { data: params })
}
