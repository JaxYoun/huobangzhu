import { request, config } from 'utils'

const { api } = config

export async function transSize (param) {
  return request(api.carPublish.transSize, { data: param })
}
export async function createDriverLine (param) {
  return request(api.carPublish.createDriverLine, { data: param })
}
