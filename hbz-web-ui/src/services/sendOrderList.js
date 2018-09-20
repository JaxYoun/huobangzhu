import { request, config } from 'utils'

const { api: { sendOrderList } } = config

export async function confirmSign (param) {
  let { orderType, orderId } = param
  return request(sendOrderList[`${orderType}Sign`],
    {
      showMsg: true,
      body: JSON.stringify({ id: orderId }),
    })
}
export async function doFslAgree (param) {
  return request(sendOrderList.doFslAgree, { data: param })
}
export async function doLtlAgree (param) {
  return request(sendOrderList.doLtlAgree, { data: param })
}
