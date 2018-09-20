import { request, config } from 'utils'

const { api: { accountManage } } = config

export async function enable (param) {
  return request(accountManage.enable, { data: param })
}
export async function disable (param) {
  return request(accountManage.disable, { data: param })
}
export async function queryRole (param) {
  return request(accountManage.queryRole, { data: param })
}
export async function enterpriseOrg (param) {
  return request(accountManage.enterpriseOrg, { data: param })
}
export async function userAdd (param) {
  return request(accountManage.userAdd, { data: param })
}
export async function userUpdate (param) {
  return request(accountManage.userUpdate, { data: param })
}
export async function userDelete (param) {
  return request(accountManage.userDelete, { data: param })
}
