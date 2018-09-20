import { request } from 'utils'
import { api } from '../routes/system/utils'


export async function queryOrgList () {
  return request(api.queryOrgList)
}

export async function queryUserList (params) {
  return request(api.queryUserList, { data: params })
}

export async function orgSave (params) {
  return request(api.orgSave, { data: params })
}

export async function userSave (params) {
  return request(api.userSave, { data: params })
}

export async function deleteOrg (params) {
  return request(api.deleteOrg, { data: params })
}

export async function deleteUser (params) {
  return request(api.deleteUser, { data: params })
}

export async function activateUser (params) {
  return request(api.activateUser, { data: params })
}

export async function inactivateUser (params) {
  return request(api.inactivateUser, { data: params })
}
