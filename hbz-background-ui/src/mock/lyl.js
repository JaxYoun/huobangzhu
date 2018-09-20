const Mock = require('mockjs')
const config = require('../utils/config')
const { apiPrefix } = config
module.exports = {
  [`POST ${apiPrefix}/manager/hbzFslOrderpage`] (req, res) {
    let dataa = [{
      id: '1',
      orderNo: 'bh123321',
      org: '创意',
      createUser: 'lyl',
      createUsertelephone: '1484740573000',
      createUserTime: '2017-12-12',
      destLinker: 'lyl',
      originArea: '成都',
      linkMan: 'ht',
      linkTelephone: '18080982277',
      destArea: '重庆',
      commodityWeight: '0.8',
      commodityVolume: '0.2',
      amount: '100',
      orderTrans: 'NEW',
      settlementTypeValue: '在线支付',
      settlementType: 'ONLINE_PAYMENT',
      orderTransValue: '',
    }]
    let datas = {
      code: '200',
      msg: '操作成功！',
      data: { content: dataa },
    }
    res.json(datas)
  },
  [`POST ${apiPrefix}/manager/helpMeToBuyPage`] (req, res) {
    let dataa = [{
      id: '1',
      orderNo: 'ORD-BUY-20171130161157-000001',
      org: '创意',
      commodityName: '河蟹',
      commodityCount: 100,
      commodityAmount: 2.23,
      remuneration: 2.35,
      createUser: 'lyl',
      createUsertelephone: '1484740573000',
      createUserTime: '2017-12-12',
      timeLimitValue: '立即',
      startTime: '2017-11-30 16:11:57',
      teakUser: 'lyl',
      destArea: '成都',
      teakUserTelephone: '18080982277',
      commodityWeight: '0.8',
      commodityVolume: '0.2',
      amount: '100',
      orderTrans: 'NEW',
      orderTransValue: '新建',
    }]
    let datas = {
      code: '200',
      msg: '操作成功！',
      data: { content: dataa },
    }
    res.json(datas)
  },
  [`POST ${apiPrefix}/manager/hbzExpressPiecesPage`] (req, res) {
    let dataa = [{
      id: '1',
      orderNo: 'ORD-BUY-20171130161157-000001',
      org: '创意',
      commodityName: '河蟹',
      commodityCount: 100,
      commodityAmount: 2.23,
      remuneration: 2.35,
      createUser: 'lyl',
      createUsertelephone: '1484740573000',
      createUserTime: '2017-12-12',
      timeLimitValue: '立即',
      startTime: '2017-11-30 16:11:57',
      teakUser: 'lyl',
      destArea: '成都',
      teakUserTelephone: '18080982277',
      commodityWeight: '0.8',
      commodityVolume: '0.2',
      amount: '100',
      orderTrans: 'NEW',
      orderTransValue: '新建',
    }]
    let datas = {
      code: '200',
      msg: '操作成功！',
      data: { content: dataa },
    }
    res.json(datas)
  },
  [`POST ${apiPrefix}/manager/hbzExpressPiecesDetails`] (req, res) {
    let data = {
      id: '1',
      orderNo: 'ORD-BUY-20171130161157-000001',
      org: '创意',
      commodityName: '河蟹',
      commodityCount: 100,
      commodityAmount: 2.23,
      remuneration: 2.35,
      createUser: 'lyl',
      createUsertelephone: '1484740573000',
      createUserTime: '2017-12-12',
      timeLimitValue: '立即',
      startTime: '2017-11-30 16:11:57',
      teakUser: 'lyl',
      destArea: '成都',
      teakUserTelephone: '18080982277',
      commodityWeight: '0.8',
      commodityVolume: '0.2',
      amount: '100',
      orderTrans: 'WAIT_TO_TAKE',
      orderTransValue: '新建',
      isNull: false,
      trackingNumber: 'kddh-23333',
    }
    let datas = {
      code: '200',
      msg: '操作成功！',
      data,
    }
    res.json(datas)
  },
  [`POST ${apiPrefix}/manager/logisticsDetailsPage`] (req, res) {
    let dataa = [{
      id: '1',
      orderNo: 'ORD-BUY-20171130161157-000001',
      org: '创意',
      commodityName: '河蟹',
      commodityCount: 100,
      commodityAmount: 2.23,
      remuneration: 2.35,
      createUser: 'lyl',
      createUsertelephone: '1484740573000',
      createUserTime: '2017-12-12',
      timeLimitValue: '立即',
      startTime: '2017-11-30 16:11:57',
      teakUser: 'lyl',
      destArea: '成都',
      teakUserTelephone: '18080982277',
      commodityWeight: '0.8',
      commodityVolume: '0.2',
      amount: '100',
      orderTrans: 'WAIT_TO_TAKE',
      orderTransValue: '新建',
      isNull: false,
      trackingNumber: 'kddh-23333',
    }]
    let datas = {
      code: '200',
      msg: '操作成功！',
      data: { content: dataa },
    }
    res.json(datas)
  },
  [`POST ${apiPrefix}/manager/selectDetails`] (req, res) {
    let data = [{ Information: '2017年10月27日19:20 成都金牛区XXX路XXX11号接运收件', id: 1 },
      { Information: '2017年10月27日19:20 成都金牛区XXX路XXX11号接运收件', id: 2 }]
    let datas = {
      code: '200',
      msg: '操作成功！',
      data,
    }
    res.json(datas)
  },
  [`POST ${apiPrefix}/wareType/query`] (req, res) {
    let dataa = [{
      name: "蘑菇",
      level: 2,
      parentId: 24,
      typeNo: "02010000",
      headerBit: "蘑菇图",
      id: 26,
      parent: {
        name: "野味",
        level: 1,
        typeNo: "02000000",
        headerBit: "野味图",
        id: 24,
        createdDate: "2017-12-22",
      },
      createdDate: "2017-12-22",

    }]
    let datas = {
      code: '200',
      msg: '操作成功！',
      data: { content: dataa },
    }
    res.json(datas)
  },
  [`POST ${apiPrefix}/wareType/all`] (req, res) {
    let data = [{
          "label": "野味",
          "id": 24,
          "value": 24,
          "headerBit": "野味图",
          "pid": null,
          "typeNo": "02000000",
          "level": 1,
          "children": [
              {
                  "label": "蘑菇",
                  "id": 26,
                  'value': 26,
                  "headerBit": "蘑菇图",
                  "pid": 24,
                  "typeNo": "02010000",
                  "level": 2
              }
          ]
      },
      {
          "label": "鱼类",
          "id": 25,
          'value': 25,
          "headerBit": "鱼图",
          "pid": null,
          "typeNo": "03000000",
          "level": 1,
          "children": [
              {
                  "label": "河鱼",
                  "id": 27,
                  value: 27,
                  "headerBit": "河鱼图",
                  "pid": 25,
                  "typeNo": "03010000",
                  "level": 2
              }
          ]
      }
    ]
    let datas = {
      code: '200',
      msg: '操作成功！',
      data,
    }
    res.json(datas)
  },
}
