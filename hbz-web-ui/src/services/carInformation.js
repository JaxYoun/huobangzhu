import { request, config } from 'utils'

const { api: { carInformation } } = config

export async function addCar (param) {
  return request(carInformation.addCar, { data: param })
}
export async function changeCar (param) {
  return request(carInformation.changeCar, { data: param })
}
