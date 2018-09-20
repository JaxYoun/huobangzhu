import { request, config } from 'utils'

const { api } = config

export async function queryAddress (param) {
  return request(api.accessories.queryAddress, { data: param })
}
export async function updateAddress (param) {
  return request(api.accessories.updateAddress, { data: param })
}
export async function geoConvert (param) {
  return request(api.geoConvert, { data: param })
}
export async function createAddress (param) {
  return request(api.accessories.createAddress, { data: param })
}
export async function deleteAddress (param) {
  return request(api.accessories.deleteAddress, { data: param })
}
export async function priceCompute (param) {
  return request(api.accessories.priceCompute, { data: param })
}
export async function exCreate (param) {
  return request(api.accessories.exCreate, { data: param })
}

