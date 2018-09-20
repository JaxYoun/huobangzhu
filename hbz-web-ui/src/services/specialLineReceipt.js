import { request, config } from 'utils'

const { api: { specialLineReceipt } } = config

export async function getFlsDetial (param) {
  return request(specialLineReceipt.getFlsDetial, { data: param })
}
export async function getLtlDetial (param) {
  return request(specialLineReceipt.getLtlDetial, { data: param })
}
export async function takeFlsOrder (param) {
  return request(specialLineReceipt.takeFlsOrder, { data: param })
}
export async function takeLtlOrder (param) {
  return request(specialLineReceipt.takeLtlOrder, { data: param })
}
