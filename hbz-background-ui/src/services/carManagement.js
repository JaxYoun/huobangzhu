import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function deleteCar (params) {
  return request('/manager/deleteVehicleInformation', { data: params })
}
export async function addCar (params) {
  return request('/manager/addVehicleInformation', { data: params })
}
export async function updateCar (params) {
  return request('/manager/updateVehicleInformation', { data: params })
}
