import { request, config } from 'utils'

const { api: { helpToBuy } } = config

export async function create (param) {
  return request(helpToBuy.create, { data: param })
}
export async function confirmProxyBuy (param) {
  return request(helpToBuy.confirmProxyBuy, { data: param })
}
export async function confirmOrder (param) {
  return request(helpToBuy.confirmOrder, { data: param })
}
