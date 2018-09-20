import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function updateWarehouseManage (params) {
  return request('/manager/updateWarehouseManage', { data: params })
}
export async function queryWarehouseManageDetail (params) {
  return request('/manager/queryWarehouseManageDetail', { data: params })
}
export async function updateLifecycleOverdue (params) {
  return request('/manager/updateLifecycleOverdue', { data: params })
}
