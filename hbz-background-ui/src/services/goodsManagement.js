import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function deleteGood (params) {
  return request('/manager/deletaCommodityInformation', { data: params })
}
export async function addGood (params) {
  return request('/manager/addCommodityInformation', { data: params })
}
export async function updateGood (params) {
  return request('/manager/updateCommodityInformation', { data: params })
}
