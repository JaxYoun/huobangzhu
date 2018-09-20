import axios from 'axios'
import lodash from 'lodash'
import pathToRegexp from 'path-to-regexp'
import { message } from 'antd'
import { browserHistory } from 'react-router'

import { apiPrefix } from './config'
import { clearCookie, keepCookie, getCookie } from './Cookie'


const fetch = (options) => {
  let {
    method = 'post',
    headers = { 'Content-Type': 'application/json' },
    url,
    data,
  } = options
  // axios.defaults.headers.post['Content-Type'] = headers['Content-Type']
  // axios.defaults.headers.post['X-AUTH-TOKEN'] = headers['X-AUTH-TOKEN']
  const X_AUTH_TOKEN = getCookie('X-AUTH-TOKEN')
  const X_AUTH_USER = getCookie('X-AUTH-USER')
  if (X_AUTH_TOKEN) {
    headers['X-AUTH-TOKEN'] = X_AUTH_TOKEN
  }
  if (X_AUTH_USER) {
    headers['X-AUTH-USER'] = X_AUTH_USER
  }
  axios.defaults.headers.post = { ...axios.defaults.headers.post, ...headers }

  axios.defaults.withCredentials = true

  const cloneData = lodash.cloneDeep(data)

  try {
    let domin = ''
    if (url.match(/[a-zA-z]+:\/\/[^/]*/)) {
      domin = url.match(/[a-zA-z]+:\/\/[^/]*/)[0]
      url = url.slice(domin.length)
    }
    const match = pathToRegexp.parse(url)
    url = pathToRegexp.compile(url)(data)
    for (let item of match) {
      if (item instanceof Object && item.name in cloneData) {
        delete cloneData[item.name]
      }
    }
    url = domin + url
  } catch (e) {
    message.error(e.message)
  }

  switch (method.toLowerCase()) {
    case 'get':
      return axios.get(url, {
        params: cloneData,
      })
    case 'delete':
      return axios.delete(url, {
        data: cloneData,
      })
    case 'post':
      return axios.post(url, cloneData)
    case 'put':
      return axios.put(url, cloneData)
    case 'patch':
      return axios.patch(url, cloneData)
    default:
      return axios(options)
  }
}

export default function request (option, options) {
  let requestOption = {}
  if (!options) {
    requestOption = {
      url: apiPrefix + (option.url || option),
    }
  } else {
    options.data = options.data || options.body
    requestOption = {
      url: apiPrefix + option,
      ...options,
    }
  }
  return fetch(requestOption).then((response) => {
    const { statusText, status } = response
    const { showMsg } = requestOption // 是否显示message
    let data = response.data
    let iscodeOk // api返回的code
    if (!data) {
      iscodeOk = true  // 登录接口成功无返回数据，特殊处理
    } else {
      iscodeOk = Number(data.code) === 200
    }
    // api未返回data  默认显示message；showMsg 强制决定是否显示message
    const showMessage = () => {
      iscodeOk ? message.success(data.msg || '操作成功!') : message.error(data.msg || '操作失败了!请重试')
    }
    if (showMsg === undefined) {
      data.data !== 0 && !data.data && showMessage()
    } else if (showMsg) {
      showMessage()
    }
    keepCookie()
    return {
      success: iscodeOk,
      message: statusText,
      statusCode: status,
      ...data,
    }
  }).catch((error) => {
    const { showMsg } = requestOption // 是否显示message
    const { response } = error
    let msg
    let statusCode
    if (response && response instanceof Object) {
      const { data, statusText } = response
      statusCode = response.status
      msg = data.msg || data.message || statusText || '出错了，请重试！'
      switch (msg) {
        case 'Authentication failed':
          msg = '登录失败！请确认登录账号及密码是否正确'
          break
        case 'Access Denied':
          msg = '登录状态失效！请重新登录'
          break
        case 'Gateway Timeout':
          msg = '请求超时，请稍后重试'
          break
        case 'Internal Server Error':
          msg = '服务器错误！请稍后重试'
          break
        case 'Network Error':
          msg = '网络连接失败！请稍后重试'
          break
        case 'Not Found':
          msg = '服务器连接失败！请稍后重试'
          break
        default:
          break
      }
      if (statusCode === 401) {
        setTimeout(() => {
          sessionStorage.isvisited ? message.info(msg) : ''
          sessionStorage.isvisited = ''
          console.log(window.location)
          browserHistory.push(`${window.location.pathname}?login=true`)
        }, 1500)
      } else {
        showMsg && message.error(msg)
      }
    } else {
      statusCode = 600
      msg = error.message || '网络连接失败！请稍后重试'
      showMsg && message.error(msg)
    }
    return { success: false, statusCode, message: msg }
  })
}

