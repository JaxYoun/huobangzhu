import { request, config } from 'utils'

const { api: { driverHelpToSend } } = config

export async function nearQuery (param) {
  return request(driverHelpToSend.nearQuery, { data: param })
}
export async function sendCarry (param) {
  return request(driverHelpToSend.sendCarry, { data: param })
}
