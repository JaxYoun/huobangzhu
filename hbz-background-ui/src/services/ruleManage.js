import { request, config } from 'utils'

const { api: { ruleManage } } = config

export async function addRule (params) {
  return request(ruleManage.addRule, { data: params })
}
export async function updateRule (params) {
  return request(ruleManage.updateRule, { data: params })
}
