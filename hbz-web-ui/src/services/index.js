import { request, config } from 'utils'

const { api } = config

export async function createOrder (param) {
  const { apiType, body } = param
  return request(api[apiType].create,
    {
      showMsg: true,
      body: JSON.stringify(body),
    })
}
