import { request, config } from 'utils'

const { api: { receiveOrderList } } = config

export async function getReceiveList (param) {
  return request(receiveOrderList.getReceiveList, { data: param })
}
export async function getLogistics (param) {
  return request(receiveOrderList.getLogistics, { data: param })
}
export async function takeUrl (param) {
  const { apiType, orderId } = param
  return request(receiveOrderList[`${apiType}Take`],
    {
      showMsg: true,
      body: JSON.stringify({ id: orderId }),
    })
}
export async function okUrl (param) {
  const { apiType, orderId } = param
  return request(receiveOrderList[`${apiType}Ok`],
    {
      showMsg: true,
      body: JSON.stringify({ id: orderId }),
    })
}
export async function refuseUrl (param) {
  const { orderId } = param
  return request(receiveOrderList.refuseUrl,
    {
      showMsg: true,
      body: JSON.stringify({ id: orderId }),
    })
}
export async function drivingAgreeUrl (param) {
  const { apiType, orderId } = param
  return request(receiveOrderList[`${apiType}DrivingAgree`],
    {
      showMsg: true,
      body: JSON.stringify({ id: orderId }),
    })
}
export async function refuseAgreeUrl (param) {
  const { apiType, orderId } = param
  return request(receiveOrderList[`${apiType}RefuseAgree`],
    {
      showMsg: true,
      body: JSON.stringify({ id: orderId }),
    })
}
