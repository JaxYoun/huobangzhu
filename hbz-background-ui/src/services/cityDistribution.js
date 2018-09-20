import { request } from 'utils'


export async function Linkage (params) {
  return request('/manager/Linkage', { data: params })
}
export async function payOrderFls (params) {
  return request('/manager/payInformation', { data: params })
}
export async function teakUserInformation (params) {
  return request('/manager/teakUserDetails', { data: params })
}
export async function enums (params) {
  return request('/manager/enums', { data: params })
}
export async function cehiclecCollections (params) {
  return request('/manager/cehiclecCollections', { data: params })
}
export async function hbzTakerInfoInformation (params) {
  return request('/manager/hbzTakerInfoInformation', { data: params })
}
export async function details (params) {
  return request('/manager/details', { data: params })
}
export async function platformDetails (params) {
  return request('/manager/platformDetails', { data: params })
}
export async function helpBuyOrderDetails (params) {
  return request('/manager/helpBuyOrderDetails', { data: params })
}
export async function HelpSendDetails (params) {
  return request('/manager/HelpSendDetails', { data: params })
}
export async function takeDetailsSingle (params) {
  return request('/manager/takeDetailsSingle', { data: params })
}
