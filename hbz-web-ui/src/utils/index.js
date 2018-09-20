
import React from 'react'
import { Tree } from 'antd'
import lodash from 'lodash'
import classnames from 'classnames'

import config from './config'
import request from './request'
import api from './api'
import { color } from './theme'
import COOKIE from './Cookie'
import address from './address'
import address01 from './address01'

const apiPrefix = config.apiPrefix
const TreeNode = Tree.TreeNode
// 连字符转驼峰
String.prototype.hyphenToHump = function () {
  return this.replace(/-(\w)/g, (...args) => {
    return args[1].toUpperCase()
  })
}

// 驼峰转连字符
String.prototype.humpToHyphen = function () {
  return this.replace(/([A-Z])/g, '-$1').toLowerCase()
}

// 日期格式化
Date.prototype.format = function (format) {
  const o = {
    'M+': this.getMonth() + 1,
    'd+': this.getDate(),
    'h+': this.getHours(),
    'H+': this.getHours(),
    'm+': this.getMinutes(),
    's+': this.getSeconds(),
    'q+': Math.floor((this.getMonth() + 3) / 3),
    S: this.getMilliseconds(),
  }
  if (/(y+)/.test(format)) {
    format = format.replace(RegExp.$1, `${this.getFullYear()}`.substr(4 - RegExp.$1.length))
  }
  for (let k in o) {
    if (new RegExp(`(${k})`).test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : (`00${o[k]}`).substr(`${o[k]}`.length))
    }
  }
  return format
}


/**
 * @param   {String}
 * @return  {String}
 */

const queryURL = (name) => {
  let reg = new RegExp(`(^|&)${name}=([^&]*)(&|$)`, 'i')
  let r = window.location.search.substr(1).match(reg)
  if (r != null) return decodeURI(r[2])
  return null
}

/**
 * 数组内查询
 * @param   {array}      array
 * @param   {String}    id
 * @param   {String}    keyAlias
 * @return  {Array}
 */
const queryArray = (array, key, keyAlias = 'key') => {
  if (!(array instanceof Array)) {
    return null
  }
  const item = array.filter(_ => _[keyAlias] === key)
  if (item.length) {
    return item[0]
  }
  return null
}

/**
 * 数组格式转树状结构
 * @param   {array}     array
 * @param   {String}    id
 * @param   {String}    pid
 * @param   {String}    children
 * @return  {Array}
 */
const arrayToTree = (array, id = 'id', pid = 'pid', children = 'children') => {
  let data = lodash.cloneDeep(array)
  let result = []
  let hash = {}
  data.forEach((item, index) => {
    hash[data[index][id]] = data[index]
  })

  data.forEach((item) => {
    let hashVP = hash[item[pid]]
    if (hashVP) {
      !hashVP[children] && (hashVP[children] = [])
      hashVP[children].push(item)
    } else {
      result.push(item)
    }
  })
  return result
}


/**
 * 树形节点生成
 * @param {Array} treeData
 * @param {String} key
 * @param {String} pId
 * @param {String} name
 */
const arrayToNodes = (treeData, name = 'name') => treeData.map((item) => {
  if (item.children && item.children.length) {
    return <TreeNode key={item.id} title={item[name]} {...item} >{arrayToNodes(item.children, name)}</TreeNode>
  }
  return <TreeNode key={item.id} title={item[name]} {...item} />
})


/**
 * json 转换成 urlencoded
 * @ param {object} data
*/
const jsonToUrlencoded = (data) => {
  return Object.keys(data).map((key) => {
    return `${encodeURIComponent(key)}=${encodeURIComponent(data[key])}`
  }).join('&')
}


/**
 *  替换对象属性名称
 * @ param {array} data
*/
const changeKeyNames = (arry, namesMap) => {
  let arryData = []
  for (let obj of arry.values()) {
    for (let [key, value] of Object.entries(namesMap)) {
      obj[key] = obj[value]
    }
    arryData.push(obj)
  }
  return arryData
}


/** 表单正则校验规则
 *
*/
const regex = {
  phone: '^1[34578]\\d{9}$',
  loginname: /^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){3,14}$/,
  urlPrefix: '^(http://|https://)',
  ip: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
  number: '^[0-9]+(.[0-9]{1,2})?$',
  telephone: '^((0\\d{2,3}-\\d{7,8})|(1[3584]\\d{9}))$',
  email: '^[a-zA-Z0-9_\\.-]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$',
}


/**
 *  下载文件
 */
const fileDownLoad = (fileUrl) => {
  let elemIF = document.getElementById('fileDownLoad')
  if (!elemIF) {
    elemIF = document.createElement('iframe')
    elemIF.id = 'fileDownLoad'
    elemIF.src = fileUrl
    elemIF.style.display = 'none'
    document.body.appendChild(elemIF)
  } else {
    elemIF.src = ''
    elemIF.src = fileUrl
  }
}

/**
 * 用户页面权限
 * @param {String | Number} limitCode 页面权限码
 * @param {String} type 权限类型
 */
const isLimited = (limitCode, type) => {
  limitCode = Number(limitCode)
  let result
  switch (type) {
    case 'create':
      result = limitCode > 0 && limitCode < 6
      break
    case 'update':
      result = limitCode > 0 && limitCode < 4
      break
    case 'execute':
      result = limitCode === 1 || limitCode === 4 || limitCode === 2 || limitCode === 6
      break
    case 'delete':
      result = limitCode === 1
      break
    default:
      result = false
      break
  }
  return result
}
/**
 *  获取查询条件上的差数
 */
const getOrderId = (location, name) => {
  let param
  let reg = new RegExp(`(^|&)${name}=([^&]*)(&|$)`)
  let r = location.search.substr(1).match(reg)
  if (r != null) {
    param = unescape(r[2])
  } else {
    param = null
  }
  return param
}

module.exports = {
  config,
  apiPrefix,
  request,
  color,
  classnames,
  queryURL,
  queryArray,
  arrayToTree,
  arrayToNodes,
  jsonToUrlencoded,
  changeKeyNames,
  regex,
  fileDownLoad,
  isLimited,
  COOKIE,
  address,
  address01,
  getOrderId,
}

