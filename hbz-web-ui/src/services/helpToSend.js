import { request, config } from 'utils'

const { api } = config

export async function create (param) {
  return request(api.helpToSend.create, { data: param })
}
export async function confirmProxyDelivery (param) {
  return request(api.helpToSend.confirmProxyDelivery, { data: param })
}
export async function computePrice (param) {
  return request(api.helpToSend.computePrice, { data: param })
}
export async function confirmOrder (param) {
  return request(api.helpToSend.confirmOrder, { data: param })
}
