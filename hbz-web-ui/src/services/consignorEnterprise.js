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

export async function getOrderDetail (param) {
  const { apiType, id } = param
  return request(api[apiType].getOrderDetail,
    {
      body: JSON.stringify({ id }),
    })
}

export async function countPrice (param) {
  const { apiType, fields } = param
  return request(api[apiType].countPrice,
    {
      showMsg: false,
      body: JSON.stringify({ ...fields }),
    })
}

export async function addressQuery () {
  return request(api.addressQuery, { body: {} })
}

export async function orderConfirm (param) {
  const { apiType, fields } = param
  return request(api[apiType].orderConfirm,
    {
      showMsg: false,
      body: JSON.stringify({ ...fields }),
    })
}

export async function getPayType (param) {
  return request(api.enums, { data: param })
}
