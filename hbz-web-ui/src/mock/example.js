const Mock = require('mockjs')
const config = require('../utils/config')
const { apiPrefix } = config
const qs = require('qs')

const returnSuccess = (words, rdata) => { // 返回操作成功
  return {
    msg: `${words}成功`,
    code: 200,
    data: rdata,
  }
}

const returnError = (words, rdata) => { // 返回操作失败
  return {
    msg: `操作失败,${words}`,
    code: 400,
    data: rdata,
  }
}
const randomSuccess = (words, rdata) => {  // 随机返回操作结果
  let responsS = {
    msg: `${words}成功`,
    code: 200,
    data: rdata,
  }
  let responsE = {
    msg: `${words}失败`,
    code: 400,
  }
  let R = Math.random()
  let respons = R < 0.7 ? responsS : responsE
  return respons
}
/*
*data array  数据
*other object 筛选条件
*timeName string 数据里面代表时间的字段名
* [startTimeName, endTimeName]  string other里面代表时间的参数的字段名
*/


const handleFilter = (data, other, timeName = 'creatTime', startTimeName = 'startTime', endTimeName = 'endTime') => {  // 查询表数据
  let startTime = {}.hasOwnProperty.call(other, startTimeName) ? other[startTimeName] : false
  let endTime = {}.hasOwnProperty.call(other, endTimeName) ? other[endTimeName] : false
  startTime && endTime ? other[timeName] = [startTime, endTime] : true

  for (let key in other) {
    // 遍历请求参数
    if ({}.hasOwnProperty.call(other, key)) {
      data = data.filter((item) => { // 遍历数据
        if ({}.hasOwnProperty.call(item, key)) {
          if (key === timeName) {
            const start = new Date(other[key][0]).getTime()
            const end = new Date(other[key][1]).getTime()
            const now = new Date(item[key]).getTime()
            if (start && end) {
              return now >= start && now <= end
            }
            return true
          }
          return String(item[key]).trim().indexOf(decodeURI(other[key]).trim()) > -1
        }
        return true
      })
    }
  }
  return data
}

/*
* data  array 要返回的数据
* page  number  页码
* size  number  页容量
*
*/
const pageJson = (data, { page = 1, size = 20 }) => {
  page++
  return {
    data: {
      content: data.slice((page - 1) * size, page * size),
      totalPages: 7,
      totalElements: data.length,
      last: false,
      number: 0,
      size,
      sort: [{
        direction: 'DESC',
        property: 'id',
        ignoreCase: false,
        nullHandling: 'NATIVE',
        ascending: false,
        descending: true,
      }],
      first: true,
      numberOfElements: 2,
    },
  }
}
const TableListData = Mock.mock({
  'actDefineList|11-30': [
    { key: '@id(1)',
      'id|+1': 1,
      name: '@name',
      'version|200-500.3': 1,
      'resourceName': function () {
        let name = Mock.mock('@word')
        return (`${name}.xml`)
      },
      'deployId|+1': 100,
      'dgrmResourceName': function () {
        let name = Mock.mock('@word')
        return (`${name}.png`)
      },
      deployTime: '@date("yyyy-MM-dd")',
      'suspensionState|1-2': true,
      'suspensionStateName': function () {
        return this.suspensionState ? '激活' : '挂起'
      },
    },
  ],
  'NatnoticeList|55': [{
    key: '@id(1)',
    'id|+1': 1,
    logDate: '@date("yyyy-MM-dd hh:mm:ss")',
    destIP: '@ip',
    'destPort|80-50000': 1,
    outIP: '@ip',
    'outPort|80-50000': 1,
    srcIP: '@ip',
    'srcPort|80-50000': 1,
  }],
  'usermanage|55': [{
    key: '@id(1)',
    'userId|+1': 3,
    userName: '@cname',
    loginName: '@str(6,10)',
    telephone: '@integer(13000000000, 18999999999)',
    email: '@email',
    depName: '@cword(4,7)',
    lastLoginTime: '@date("yyyy-MM-dd hh:mm:ss")',
    'isdelete|0-1': 1,
  }],
})

let { NatnoticeList, usermanage } = TableListData

module.exports = {
  [`POST ${apiPrefix}/natnotice/queryList`] (req, res) {
    const { body } = req
    let { pageSize, page, ...other } = body
    // pageSize = pageSize || 10
    // page = page || 1
    let newData = NatnoticeList
    newData = handleFilter(newData, other, 'createTime', 'startTime', 'endTime')
    // 假装有删除
    let R = Math.random()
    R < 0.5 ? newData.slice(1) : false
    console.log(R)
    // newData[' page'] = page
    // newData[' pageSize'] = pageSize
    // res.status(200).json({
    //   data: newData.slice((page - 1) * pageSize, page * pageSize)
    // })
    res.status(200).json({ data: newData })
  }, [`POST ${apiPrefix}/natlog/listForPage`] (req, res) {
    const { body, query } = req
    let { ...other } = body
    let { size = 10, page = 1 } = query
    page++
    let newData = NatnoticeList
    newData = handleFilter(newData, other, 'logDate', 'startTime', 'endTime')
    // 假装有删除
    let R = Math.random()
    R < 0.5 ? newData.slice(1) : false
    newData[' page'] = page
    newData[' pageSize'] = size
    res.status(200).json({
      data: {
        content: newData.slice((page - 1) * size, page * size),
        "totalPages" : 7,
        "totalElements" : newData.length,
        "last" : false,
        "number" : 0,
        size,
        "sort" : [{
          "direction" : "DESC",
          "property" : "id",
          "ignoreCase" : false,
          "nullHandling" : "NATIVE",
          "ascending" : false,
          "descending" : true,
        } ],
        "first" : true,
        "numberOfElements" : 2
      },
    })
  }, [`POST ${apiPrefix}/natlog/exportExl`] (req, res) {
    console.log('假装发出了个导出excel请求', req.body)
  }, [`POST ${apiPrefix}/system/user/listForPage`] (req, res) {
    const { body, query } = req
    let { ...other } = body
    let { size = 10, page = 1 } = query
    page++
    let newData = usermanage
    newData = handleFilter(newData, other, 'logDate', 'startTime', 'endTime')
    res.status(200).json(pageJson(newData, { size, page }))
  }, [`POST ${apiPrefix}/userManage/deactivateUser`] (req, res) {
    let data = randomSuccess('操作')
    res.status(200).json(data)
  }, [`POST ${apiPrefix}/userManage/activateUser`] (req, res) {
    let data = randomSuccess('操作')
    res.status(200).json(data)
  }, [`POST ${apiPrefix}/natlog/user/updateActivated`] (req, res) {  // 启用停用用户
    let data = randomSuccess('操作')
    res.status(200).json(data)
  }, [`POST ${apiPrefix}/userManage/addUser`] (req, res) {
    let data = randomSuccess('添加')
    res.status(200).json(data)
  }, [`POST ${apiPrefix}/system/login/loginOut`] (req, res) {
    res.clearCookie('token')
    res.status(200).end()
  }, [`POST ${apiPrefix}/system/user/changeUserPassword`] (req, res) {
    let data = randomSuccess('修改')
    res.status(200).json(data)
  },

}
