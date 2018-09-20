import { request, config } from 'utils'

const { api } = config

export async function publicTypeval (param) {
  return request(api.typeval, { data: param })
}
export async function saveCollect (param) {
  return request(api.carCollect.create, { data: param })
}
