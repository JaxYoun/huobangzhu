import { request, config } from 'utils'

const { api: { vehicleCollect } } = config

export async function queryAvailableTender (param) {
  return request(vehicleCollect.queryAvailableTender, { data: param })
}
export async function takerCreate (param) {
  return request(vehicleCollect.takerCreate, { data: param })
}
