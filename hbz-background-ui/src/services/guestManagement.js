import { request } from 'utils'
export async function disableGuest (params) {
  return request('/user/disable', { data: params })
}
export async function addGuest (params) {
  return request('/user/add', { data: params })
}
export async function updateGuest (params) {
  return request('/user/update', { data: params })
}
export async function enableGuest (params) {
  return request('/user/enable', { data: params })
}

export async function makeRolesGuest (params) {
  return request('/user/makeRoles', { data: params })
}
export async function getRegisterInfo (params) {
  return request('/user/registry/get', { data: params })
}
export async function queryRole (params) {
  return request('/role/query', { data: params })
}
