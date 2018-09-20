import { request } from 'utils'
export async function getData (params) {
  return request('/manager/enums', { data: params })
}
export async function puts (params) {
  return request('/user/registry/put', { data: params })
}
export async function getDatas (params) {
  return request('/user/registry/get', { data: params })
}
export async function addPsyData (params) {
  return request('/user/registry/saveDeliveryBoy', { data: params })
}
export async function addQyhzData (params) {
  return request('/user/registry/saveEnterpriseConsignor', { data: params })
}
export async function addGrhzData (params) {
  return request('/user/registry/savePersonConsignor', { data: params })
}
export async function addGrsjData (params) {
  return request('/user/registry/savePersonDriver', { data: params })
}
export async function addYsqyData (params) {
  return request('/user/registry/saveTransEnterprise', { data: params })
}
