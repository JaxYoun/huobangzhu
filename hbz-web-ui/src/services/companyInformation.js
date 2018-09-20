import { request, config } from 'utils'

const { api: { companyInformation } } = config

export async function companyDetail (param) {
  return request(companyInformation.companyDetail, { data: param })
}
export async function updateCompanyInfo (param) {
  return request(companyInformation.updateCompanyInfo, { data: param })
}
