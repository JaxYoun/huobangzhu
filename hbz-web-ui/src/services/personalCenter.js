import { request, config } from 'utils'

const { api } = config
const url = api.personalCenter

export async function getUser (param) {
  return request(url.getUserInfo, { data: param })
}
export async function getRegistry (param) {
  return request(url.registry, { data: param })
}
export async function registrySearch (param) {
  return request(url.search, { data: param })
}
export async function enterpriseConsignorSubmit (param) {
  return request(url.enterpriseConsignorSubmit, { data: param })
}
export async function transEnterpriseSubmit (param) {
  return request(url.transEnterpriseSubmit, { data: param })
}
export async function imageChange (param) {
  return request(url.imageChange, { data: param })
}

