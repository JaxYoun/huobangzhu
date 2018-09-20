import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function deleteUser (params) {
  return request('/manager/delateUserInformation', { data: params })
}
export async function addUsers (params) {
  return request('/manager/addUserInformation', { data: params })
}
export async function updateUser (params) {
  return request('/manager/updateUserInformation', { data: params })
}
