import { request } from 'utils'
import { api } from '../routes/system/utils'

export async function query () {
  return request(api.queryMenuList)
}

export async function menuDelete (params) {
  return request(api.menuDelete, { data: params })
}

export async function menuSave (params) {
  return request(api.menuSave, { data: params })
}
