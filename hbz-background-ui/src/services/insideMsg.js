import { request, config } from 'utils'

const { api: { insideMsg } } = config

export async function send (params) {
  return request(insideMsg.send, { data: params })
}
export async function save (params) {
  return request(insideMsg.save, { data: params })
}
export async function update (params) {
  return request(insideMsg.update, { data: params })
}
