import { request } from 'utils'
import { api } from '../routes/system/utils'


export async function queryPostOrgTree () {
  return request(api.queryPostOrgTree)
}

export async function queryOrgUserTree () {
  return request(api.queryOrgUserTree, {})
}

export async function savePostUsers (params) {
  return request(api.savePostUsers, { data: params })
}

export async function queryPostUsers (params) {
  return request(api.queryPostUsers, { data: params })
}
