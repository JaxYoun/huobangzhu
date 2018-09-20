import { request, config } from 'utils'

const { api: { storageManage } } = config

export async function saveStorage (param) {
  return request(storageManage.save, { data: param })
}
export async function updateStorage (param) {
  return request(storageManage.update, { data: param })
}
export async function queryStroage (param) {
  return request(storageManage.query, { data: param })
}
