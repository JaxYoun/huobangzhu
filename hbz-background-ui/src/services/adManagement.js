import { request } from 'utils'
export async function getData (params) {
  return request('/manager/typeval', { data: params })
}
export async function getBannerDetail (params) {
  return request('/manager/banner/getBannerDetail', { data: params })
}
export async function enableOrDisableBanner (params) {
  return request('/manager/banner/enableOrDisableBanner', { data: params })
}
export async function addBanner (params) {
  return request('/manager/banner/addBanner', { data: params })
}
export async function updateBanners (params) {
  return request('/manager/banner/updateBanner', { data: params })
}
