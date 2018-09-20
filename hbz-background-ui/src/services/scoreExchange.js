import { request } from 'utils'
export async function allTree (params) {
  return request('/wareType/all', { data: params })
}
export async function queryBrand (params) {
  return request('/brand/query', { data: params })
}
export async function modify (params) {
  return request('/product/modify', { data: params })
}
export async function destroy (params) {
  return request('/product/destroy', { data: params })
}
export async function trans (params) {
  return request('/orderScore/invoice', { data: params })
}
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function querytrans (params) {
  return request('/invoice/query', { data: params })
}
export async function create (params) {
  return request('/recommendProduct/create', { data: params })
}
export async function updateRecommonGoods (params) {
  return request('/recommendProduct/update', { data: params })
}
