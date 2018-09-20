import { request } from 'utils'
export async function addUrl (params) {
  return request('/URL/add', { data: params, showMsg: true })
}
export async function updateRecord (params) {
  return request('/URL/update', { data: params, showMsg: true })
}
export async function deleteUrl (params) {
  return request('/URL/delete', { data: params })
}
export async function queryUrl (params) {
  return request('/URL/query', { data: params })
}
