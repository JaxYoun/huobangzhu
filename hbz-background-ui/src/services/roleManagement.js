import { request } from 'utils'
export async function disableRole (params) {
  return request('/role/disable', { data: params })
}
export async function addRole (params) {
  return request('/role/add', { data: params, showMsg: true })
}
export async function updateRecord (params) {
  return request('/role/update', { data: params, showMsg: true })
}
export async function deleteRole (params) {
  return request('/role/delete', { data: params })
}
export async function makeAuthorities (params) {
  return request('/role/makeAuthorities', { data: params })
}
export async function makeMenus (params) {
  return request('/role/makeMenus', { data: params })
}
export async function queryAuthority (params) {
  return request('/authority/query', { data: params })
}
export async function queryMenus (params) {
  return request('/menu/query', { data: params })
}
export async function getRoleCode (params) {
  return request('/manager/enums', { data: params })
}
