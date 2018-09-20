import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function getDatas (params) {
  return request('/manager/enums', { data: params })
}
export async function getCarDatas (params) {
  return request('/manager/selecAlltVehicleNumber', { data: params })
}
export async function addBatchStartVehicle (params) {
  return request('/manager/addBatchStartVehicle', { data: params })
}
export async function driverConfirmedPage (params) {
  return request('/manager/driverConfirmedPage', { data: params })
}
export async function selectAllStartVehicleInformation (params) {
  return request('/manager/selectAllStartVehicleInformation', { data: params })
}
export async function updateTransitState (params) {
  return request('/manager/updateTransitState', { data: params })
}
export async function updateShippingStatus (params) {
  return request('/manager/updateShippingStatus', { data: params })
}
export async function midwayLoading (params) {
  return request('/manager/midwayLoading', { data: params })
}
export async function addNewBatchStartVehicle (params) {
  return request('/manager/addNewBatchStartVehicle', { data: params })
}
