import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function Linkage (params) {
  return request('/manager/Linkage', { data: params })
}
export async function getDatas (params) {
  return request('/manager/enums', { data: params })
}
export async function deleteMessage (params) {
  return request('/manager/deleteCargoInformation', { data: params })
}
export async function addMessage (params) {
  return request('/manager/addCargoInformation', { data: params })
}
export async function updateMessage (params) {
  return request('/manager/updateCargoInformation', { data: params })
}

