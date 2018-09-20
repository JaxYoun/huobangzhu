import { request, config } from 'utils'

const { api: { storageInformation } } = config

export async function getStorage (param) {
  return request(storageInformation.getStorage, { data: param })
}
export async function getStorageDetail (param) {
  return request(storageInformation.getStorageDetail, { data: param })
}
export async function queryBail (param) {
  return request(storageInformation.queryBail, { data: param })
}
export async function creatPayOrder (param) {
  return request(storageInformation.creatPayOrder, { data: param })
}
export async function getBatchId (param) {
  return request(storageInformation.getBatchId, { data: param })
}
