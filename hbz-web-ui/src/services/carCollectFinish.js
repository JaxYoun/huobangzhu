import { request, config } from 'utils'

const { api } = config

export async function getDetail (param) {
  return request(api.fslOrder.get, { data: param })
}

export async function tenderGet (param) {
  return request(api.fslOrder.tender, { data: param })
}

export async function consignorGet (param) {
  return request(api.fslOrder.consignor, { data: param })
}
