const apiPrefix = '/api/system'
const Mock = require('mockjs')
const resCode = () => {
  let randres = {}
  let randcode = Math.random()
  if (randcode > 0.5) {
    randres.code = 200
    randres.msg = '操作成功了！恭喜你！'
  } else {
    randres.code = 201
    randres.msg = '操作失败了！再试试吧！'
  }
  return randres
}

module.exports = {
  [`POST ${apiPrefix}/user/list`] (req, res) {
    console.log(req.body)
    const dataUser = Mock.mock({
      'data|10-50': [
        {
          id: '@id',
          name: '@cname',
          orgId: '@id',
          loginName: '@word',
          mobilePhone: /^1[0-9]{10}$/,
          email: '@email',
          status: '@boolean',
        },
      ],
    })
    res.json(dataUser)
  }, [`POST ${apiPrefix}/role/list`] (req, res) {
    console.log(req.body)
    const randomData = Mock.mock({
      'data|5-10': [
        {
          key: '@id',
          roleName: '@word',
          roleDesc: '@cparagraph(2)',
          status: '@boolean',
        },
      ],
    })
    const data1 = {
      key: 1,
      roleName: '管理员',
      roleDesc: '管理员...',
      status: false,
    }
    const data = randomData
    data.push(data1)
    res.json(data)
  }, [`POST ${apiPrefix}/menu/list`] (req, res) {
    console.log(req.body)
    const randomData = Mock.mock({
      data: [
        {
          key: 1,
          pId: 0,
          menuName: '系统管理',
          menuUrl: 'http://www.baidu.com',
          menuIcon: 'smile-o"',
          orderCode: '@natural(0, 10)',
          status: '@boolean',
          level: 1,
        }, {
          key: 2,
          pId: 1,
          menuName: '角色管理',
          menuUrl: 'http://www.baidu.com',
          menuIcon: 'smile-o"',
          orderCode: '@natural(0, 10)',
          status: '@boolean',
          level: 1,
        }, {
          key: 3,
          pId: 1,
          menuName: '基础信息',
          menuUrl: 'http://www.baidu.com',
          menuIcon: 'smile-o"',
          orderCode: '@natural(0, 10)',
          status: '@boolean',
          level: 1,
        }, {
          key: 4,
          pId: 3,
          menuName: '岗位管理',
          menuUrl: 'http://www.baidu.com',
          menuIcon: 'smile-o"',
          orderCode: '@natural(0, 10)',
          status: '@boolean',
          level: 1,
        },
      ],
    })
    res.json(randomData)
  }, [`POST ${apiPrefix}/org/list`] (req, res) {
    console.log(req.body)
    const dataUser = {
      data: [
        {
          id: 11,
          orgName: '人力资源',
          orderCode: '1',
          pId: '0',
        }, {
          id: 21,
          orgName: '行政管理',
          orderCode: '1',
          pId: '0',
        }, {
          id: 31,
          orgName: '技术中心',
          orderCode: '1',
          pId: '0',
          children: [
            {
              orgName: '研发部',
              orderCode: '1',
              id: 31001,
              pId: 31,
            }, {
              orgName: '项目管理部',
              orderCode: '1',
              id: 31002,
              pId: 31,
            },
          ],
        },
      ],
    }
    res.json(dataUser)
  }, [`POST ${apiPrefix}/post/queryPostOrgTree`] (req, res) {
    console.log(req.body)
    const dataUser = {
      data: [
        {
          key: 11,
          name: '人力资源',
          orderCode: '1',
          nodeType: 1,
          pId: '0',
          postDesc: '描述...',
        }, {
          key: 21,
          name: '行政管理',
          nodeType: 1,
          orderCode: '1',
          pId: '0',
        }, {
          key: 31,
          name: '技术中心',
          orderCode: '1',
          nodeType: 1,
          pId: '0',
        }, {
          key: 311,
          name: '研发部',
          orderCode: '2',
          pId: 31,
          nodeType: 1,
        }, {
          key: 3111,
          postDesc: '描述...',
          name: '前端开发',
          orderCode: '2',
          pId: 311,
          nodeType: 2,
        },
      ],
    }
    res.json(dataUser)
  }, [`POST ${apiPrefix}/:type/save`] (req, res) {
    console.log(req.body)
    const randomData = resCode()
    const data = {
      code: randomData.code,
      msg: randomData.msg,
      data: [],
    }
    res.json(data)
  }, [`POST ${apiPrefix}/:type/del`] (req, res) {
    console.log(req.body)
    const randomData = resCode()
    const data = {
      code: randomData.code,
      msg: randomData.msg,
    }
    console.log(data)
    res.json(data)
  }, [`POST ${apiPrefix}/user/queryOrgUserTree`] (req, res) {
    const randomData = Mock.mock({
      data: [
        {
          id: 1,
          key: 1,
          pId: 0,
          nodeType: 1,
          name: '省公司',
        }, {
          id: 11,
          key: 11,
          pId: 1,
          nodeType: 1,
          name: '贵阳',
        }, {
          id: 21,
          key: 21,
          pId: 11,
          nodeType: 2,
          name: '张超',
        }, {
          id: 12,
          key: 12,
          pId: 1,
          nodeType: 1,
          name: '研发部',
        }, {
          id: 121,
          key: 121,
          pId: 12,
          nodeType: 2,
          name: '周正斌',
        }, {
          id: 122,
          key: 122,
          pId: 12,
          nodeType: 2,
          name: '钟凯',
        }, {
          id: 123,
          key: 123,
          pId: 12,
          nodeType: 2,
          name: '成凯',
        }, {
          id: 124,
          key: 124,
          pId: 12,
          nodeType: 2,
          name: '王敏',
        },
      ],
    })
    res.json(randomData)
  }, [`POST ${apiPrefix}/role/queryMenuListByRoleId`] (req, res) {
    console.log(req.body)
    const randomData = Mock.mock({
      data: [{
        key: 3,
        level: 1,
      }],
    })
    res.json(randomData)
  }, [`POST ${apiPrefix}/post/queryRolesByPostId`] (req, res) {
    console.log(req.body)
    const data = [1]
    res.json(data)
  }, [`POST ${apiPrefix}/limit/queryPostUsers`] (req, res) {
    console.log(req.body)
    const data = {
      data: '123,124',
    }
    res.json(data)
  }, [`POST ${apiPrefix}/role/saveMenuLimit`] (req, res) {
    console.log(req.body)
    const randomData = resCode()
    const data = {
      code: randomData.code,
      msg: randomData.msg,
      data: [],
    }
    res.json(data)
  }, [`POST ${apiPrefix}/role/saveRoleLimit`] (req, res) {
    console.log(req.body)
    const randomData = resCode()
    const data = {
      code: randomData.code,
      msg: randomData.msg,
      data: [],
    }
    res.json(data)
  }, [`POST ${apiPrefix}/limit/savePostUsers`] (req, res) {
    console.log(req.body)
    const randomData = resCode()
    const data = {
      code: randomData.code,
      msg: randomData.msg,
      data: [],
    }
    res.json(data)
  }, [`POST ${apiPrefix}/logs/queryLoginLogs`] (req, res) {
    console.log(req.body)
    const data = Mock.mock({
      'data|10-50': [
        {
          userId: '@id',
          loginIp: '@ip',
          userName: '@name',
          loginName: '@word(3,6)',
          loginType: '@natural()',
          logoutDt: '@datetime',
          createDt: '@datetime',
        },
      ],
    })
    res.json(data)
  }, [`POST ${apiPrefix}/logs/queryLoginFailLogs`] (req, res) {
    console.log(req.body)
    const data = Mock.mock({
      'data|10-50': [
        {
          userId: '@id',
          loginIp: '@ip',
          userName: '@name',
          loginName: '@word(3,6)',
          loginType: '@natural()',
          createDt: '@datetime',
        },
      ],
    })
    res.json(data)
  },
}
