import { request } from 'utils'

export async function Linkage (params) {
  return request('/manager/Linkage', { data: params })
}
export async function payOrderFls (params) {
  return request('/manager/payInformation', { data: params })
}
export async function hbzExpressPiecesDetails (params) {
  return request('/manager/hbzExpressPiecesDetails', { data: params })
}
export async function details (params) {
  return request('/manager/selectDetails', { data: params })
}
export async function addHbzExOrder (params) {
  return request('/manager/saveHbzExpressPieces', { data: params })
}
export async function deleteData (params) {
  return request('/manager/deleteLogisticsDetail', { data: params })
}
export async function addLogisticsDetail (params) {
  return request('/manager/addLogisticsDetail', { data: params })
}
export async function enums (params) {
  return request('/manager/enums', { data: params })
}