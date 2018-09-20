import { request } from 'utils'
export async function queryMenus (params) {
  return request('/menu/query', { data: params })
}
export async function menuSave (params) {
  return request('/menu/add', { data: params, showMsg: true })
}

export async function menuDelete (params) {
  return request('/menu/delete', { data: params })
}
export async function menuMerge (params) {
  return request('/menu/merge', { data: params })
}
export async function getWebModel (params) {
  return request('/manager/enums', { data: params })
}
