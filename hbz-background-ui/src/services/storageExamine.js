import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function updateWarehouseAudit (params) {
  return request('/manager/updateWarehouseAudit', { data: params })
}
export async function queryWarehouseAuditDetail (params) {
  return request('/manager/queryWarehouseAuditDetail', { data: params })
}
