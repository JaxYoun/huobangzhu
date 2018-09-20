import { request } from 'utils'
export async function enums (params) {
  return request('/manager/enums', { data: params })
}
export async function deleteClass (params) {
  return request('/wareType/delete', { data: params })
}
export async function allTree (params) {
  return request('/wareType/all', { data: params })
}
export async function newClass (params) {
  return request('/wareType/new', { data: params })
}
export async function Linkage (params) {
  return request('/manager/Linkage', { data: params })
}
export async function queryBrand (params) {
  return request('/brand/query', { data: params })
}
export async function changeClass (params) {
  return request('/wareType/merge', { data: params })
}
export async function newGoods (params) {
  return request('/wareInfo/new', { data: params })
}
export async function changeGooods (params) {
  return request('/wareInfo/update', { data: params })
}
export async function deleteGoods (params) {
  return request('/wareInfo/delete', { data: params })
}
export async function newBrand (params) {
  return request('/brand/create', { data: params })
}
export async function deleteBrand (params) {
  return request('/brand/delete', { data: params })
}
export async function updateBrand (params) {
  return request('/brand/update', { data: params })
}
export async function upGoods (params) {
  return request('/product/create', { data: params })
}
