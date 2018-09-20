import { request } from 'utils'
import { api } from '../routes/system/utils'

export async function addCodeTable (params) {
  return request(api.addCodeTable,  { data: params })
}
export async function editCodeTable (params) {
  return request(api.editCodeTable,  { data: params })
}
export async function deleteCodeTable (params) {
  return request(api.deleteCodeTable, { data: params })
}
export async function updateCodeTable (params) {
  return request(api.updateCodeTable,  { data: params })
}