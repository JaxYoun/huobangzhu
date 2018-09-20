import { request, config } from 'utils'

const { api: { driverHelpToBuy } } = config

export async function nearQuery (param) {
  return request(driverHelpToBuy.nearQuery, { data: param })
}
export async function getDetail (param) {
  return request(driverHelpToBuy.getDetail, { data: param })
}
export async function buyCarry (param) {
  return request(driverHelpToBuy.buyCarry, { data: param })
}

