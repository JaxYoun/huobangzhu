const Mock = require('mockjs')
const config = require('../utils/config')
const { apiPrefix } = config
const qs = require('qs')

const returnSuccess = (words, rdata) => { // 返回操作成功
  return {
    msg: `${words}成功`,
    code: '200',
    data: rdata,
  }
}

const returnError = (words, rdata) => { // 返回操作失败
  return {
    msg: `操作失败,${words}`,
    code: '400',
    data: rdata,
  }
}
const randomSuccess = (words, rdata) => {  // 随机返回操作结果
  let responsS = {
    msg: `${words}成功`,
    code: '200',
    data: rdata,
  }
  let responsE = {
    msg: `${words}失败`,
    code: '400',
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

//  模拟数据
const TableListData = Mock.mock({
  'actModelList|11-30': [
    { key: '@id(1)',
      'id|+1': 1,
      name: '@name',
      'version|200-500': 1,
      createTime: '@date("yyyy-MM-dd")',
    },
  ],
  'queryAllIndicatorWork|88': [
    { 'id|+1': 1,
      indicatorWorkNum: '@guid',
      indicatorWorkName: '@cword(4,10)',
      assessmentPointName: '@word(19,41)',
      assessmentIndexName: '@cword(19,210)',
      createdDate: '@date("yyyy-MM-dd")',
      assessmentTimeLimit: null,
      indicatorWorkEndStr: '@date("yyyy-MM-dd")',
      'selfAssessmentTotalScore|1-100': 1,
      'isPass|0-1': 1,
      'actId|10000-99990': 232,
      'procInstId|10000-99990': 121,
      exeStatus: () => {
        let num = Mock.mock('@pick([1, 2, 4])')
        return num
      },
      exeStatusStr: '@cword(4,10)',
      assessmentAdvice: '@cword(4,10)',
      checkDate: '@date("yyyy-MM-dd")',
      'assessmentScore|1-100': 1,
    },
  ],
  queryTabDetailData: {
    '01|33': [
      {
        wuYeBianMa: '@guid',
        wuYeGuanLiYuan: '@cname',
        luRuRen: '@cname',
        shiFouXuYaoQianDingHeTong: () => Mock.mock('@pick(["是", "否"])'),
        yongDiHuoQuFangShi: '@cword(4,10)',
        yuGuNianZuJin: '',
        lingChangZuYuanYin: '@cword(4,10)',
        shiFouYouXiao: () => Mock.mock('@pick(["是", "否"])'),
        beiZhu: '@cword(10,20)',
      },
    ],
  },
})

const { actModelList, queryAllIndicatorWork, queryTabDetailData } = TableListData

const divisionIndexData = {
  id: '作业ID',
  workFormulaStr: '作业扣分计算公式',
  selfAppraiseScore: 123,
  selfAssessmentDesc: '自评描述',
  assessmentPoint: '考核点',
  assessmentIndexName: '指标名',
  verificationAdvice: '考核建议',
  isPass: '是否通过',
  isStandard: '是否达标',
  checkDesc: '考核描述',
  checkResult: '考核结果',
  deviationAnalysis: '偏差分析',
  subdivisionIndexList: [{
    subdivisionIndexId: '细分指标Id',
    subdivisionIndexName: '细分指标名',
    timePoint: '时间点',
    subdivisionIndexDeduction: '细分指标扣分',
    attachFiles: [{
      page: 0,
      size: 20,
      id: 502481,
      fileName: '网站安全监测(1).png',
      filePath: '/test/20170822/bb83290b-744d-4f78-a899-193c42cd5c87.png',
      fileSize: 4983,
      isEncryption: 'N',
    }],
    formulaStr: '扣分=未定级备案的网络单元数×A；参数A=10',
    wordMeans: '参数代表意思',
    deductPoints: '细分指标扣分',
    fileId: '证明文件',
    isReview: '细分指标是否完成自评',
    subdivisionIndexReviewParams: [{
      paramName: '未定级备案的网络单元数',
      paramValue: 12,
    }],
  }],
}

module.exports = {
  [`POST ${apiPrefix}/user/app/telephone/check`] (req, res) {      //  货帮主 hbz HBZ 验证手机
    let { body } = req
    let jsond = {}
    if (body && Number(body.telephone) !== 18202322320) {
      jsond = returnSuccess('手机验证', null)
    } else {
      jsond = returnError('手机验证', null)
    }
    res.json(jsond)
  }, [`POST ${apiPrefix}/user/app/authCode/check`] (req, res) {      //  货帮主 hbz HBZ 验证码检查
    let { body } = req
    let jsond = {}
    if (body && body.authCode == 2222) {
      jsond = returnSuccess('验证码检查', null)
    } else {
      jsond = returnError('验证码检查', null)
    }
    res.json(jsond)
  }, [`POST ${apiPrefix}/user/app/reset`] (req, res) {      //  货帮主 hbz HBZ 修改密码
    let { body } = req
    let jsond = {}
    if (body && body.authCode && body.telephone && body.password) {
      jsond = returnSuccess('修改', null)
    } else {
      jsond = returnError('修改', null)
    }
    res.json(jsond)
  }, [`POST ${apiPrefix}/user/app/registry`] (req, res) {      //  货帮主 hbz HBZ 注册
    let { body } = req
    let jsond = {}
    if (body && body.authCode && body.telephone && body.password && body.nickName) {
      jsond = returnSuccess('注册', null)
    } else {
      jsond = returnError('注册', null)
    }
    res.json(jsond)
  }, [`POST ${apiPrefix}/session/token`] (req, res) {      //  货帮主 hbz HBZ 获取token
    let jsond = returnSuccess('已认证', 'tonken-tonken-tonken-tonken')
    res.json(jsond)
  },
}
