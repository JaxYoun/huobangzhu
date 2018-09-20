import { request, config } from 'utils'

const { api: { appraiseOrder } } = config

export async function comment (param) {
  return request(appraiseOrder.comment, { data: param })
}
