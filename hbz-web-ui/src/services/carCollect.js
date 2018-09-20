import { request, config } from 'utils'

const { api } = config

export async function orderFslQuery (param) {
  return request(api.carCollect.orderQuery, { data: param })
}
