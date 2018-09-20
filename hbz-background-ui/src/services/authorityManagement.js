import { request } from 'utils'
export async function addAuthority (params) {
  return request('/authority/add', { data: params, showMsg: true })
}
export async function updateRecord (params) {
  return request('/authority/merge', { data: params, showMsg: true })
}
export async function deleteAuthority (params) {
  return request('/authority/delete', { data: params })
}
export async function makeAuth (params) {
  return request('/authority/makeUrls', { data: params })
}
export async function queryAuthority (params) {
  return request('/authority/query', { data: params })
}
export async function queryMenus (params) {
  return request('/authority/query', { data: params })
}
export async function queryUrl (params) {
  return request('/URL/queryPage', { data: params })
}
