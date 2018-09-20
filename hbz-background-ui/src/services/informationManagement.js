import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function addNew (params) {
  return request('/manager/addNews', { data: params })
}
export async function updateNew (params) {
  return request('/manager/updateNews', { data: params })
}
export async function updateNewsStatus (params) {
  return request('/manager/updateNewsStatus', { data: params })
}
export async function getNewsDetail (params) {
  return request('/manager/getNewsDetail', { data: params })
}