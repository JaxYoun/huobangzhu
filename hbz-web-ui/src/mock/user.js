const qs = require('qs')
const Mock = require('mockjs')
const config = require('../utils/config')
const { apiPrefix } = config

let usersListData = Mock.mock({
  'data|80-100': [
    {
      id: '@id',
      name: '@name',
      nickName: '@last',
      phone: /^1[34578]\d{9}$/,
      'age|11-99': 1,
      address: '@county(true)',
      isMale: '@boolean',
      email: '@email',
      createTime: '@datetime',
      avatar () {
        return Mock.Random.image('100x100', Mock.Random.color(), '#757575', 'png', this.nickName.substr(0, 1))
      },
    },
  ],
})


let database = usersListData.data

const userPermission = {
  DEFAULT: [
    'dashboard', 'chart',
  ],
  ADMIN: [
    'dashboard', 'users', 'UIElement', 'UIElementIconfont', 'chart',
  ],
  DEVELOPER: ['dashboard', 'users', 'UIElement', 'UIElementIconfont', 'chart'],
}

const adminUsers = [
  {
    id: 0,
    username: 'admin',
    password: 'admin',
    permissions: userPermission.ADMIN,
  }, {
    id: 1,
    username: 'guest',
    password: 'guest',
    permissions: userPermission.DEFAULT,
  }, {
    id: 2,
    username: '吴彦祖',
    password: '123456',
    permissions: userPermission.DEVELOPER,
  }, {
    id: 3,
    username: 'testyou',
    password: '111111',
    permissions: userPermission.ADMIN,
  },
]

const queryArray = (array, key, keyAlias = 'key') => {
  if (!(array instanceof Array)) {
    return null
  }
  let data

  for (let item of array) {
    if (item[keyAlias] === key) {
      data = item
      break
    }
  }

  if (data) {
    return data
  }
  return null
}

const NOTFOUND = {
  message: 'Not Found',
  documentation_url: 'http://localhost:8000/request',
}

module.exports = {
  [`POST ${apiPrefix}/system/login/getUserByUserIdToCache`] (req, res) {
    const cookie = req.headers.cookie || ''
    const cookies = qs.parse(cookie.replace(/\s/g, ''), { delimiter: ';' })
    if (!cookies.token) {
      res.status(200).send({
        code: 201,
        msg: '无数据',
        data: {},
      })
      return
    }
    res.json({
      code: 200,
      msg: '操作成功',
      data: {
        id: 5623,
        orgId: 5622,
        userName: '成凯',
        type: 1,
        loginName: 'chengkai',
        email: '12334@qq.com',
        mobilePhone: '13880008000',
        menuIds: [{
          menuUrl: '/',
          menuIcon: 'smile-o',
          limitLevel: '1',
          pId: '',
          menuName: '首页',
          orderCode: '1',
          menuType: '1',
          id: '1',
          relationship: '1-1',
        }, {
          menuUrl: '/system/org',
          menuIcon: 'smile-o',
          limitLevel: '1',
          pId: '5118',
          menuName: '基础信息管理',
          orderCode: '1',
          menuType: '1',
          id: '5120',
          relationship: '5118-5120',
        }, {
          menuUrl: '/system/role',
          menuIcon: 'smile-o',
          limitLevel: '1',
          pId: '5118',
          menuName: '角色管理',
          orderCode: '2',
          menuType: '1',
          id: '5121',
          relationship: '5118-5121',
        }, {
          menuUrl: '/system/menu',
          menuIcon: 'smile-o',
          limitLevel: '1',
          pId: '5118',
          menuName: '菜单管理',
          orderCode: '3',
          menuType: '1',
          id: '5132',
          relationship: '5118-5132',
        }, {
          menuUrl: '/system/post',
          menuIcon: 'smile-o',
          limitLevel: '1',
          pId: '5118',
          menuName: '岗位管理',
          orderCode: '4',
          menuType: '1',
          id: '5133',
          relationship: '5118-5133',
        }, {
          menuUrl: '/system/limit',
          menuIcon: 'smile-o',
          limitLevel: '1',
          pId: '5118',
          menuName: '权限管理',
          orderCode: '5',
          menuType: '1',
          id: '337671',
          relationship: '5118-337671',
        }, {
          menuUrl: '',
          menuIcon: 'manage_fill',
          limitLevel: '1',
          pId: '',
          menuName: '系统管理',
          orderCode: '10',
          menuType: '1',
          id: '5118',
          relationship: '5118',
        }],
        currentPostId: 339025,
        posts: [{
          postName: '管理员',
          postId: 323174,
        }, {
          postName: '超级管理员',
          postId: 339025,
        }, {
          postName: 'ichengdu',
          postId: 407444,
        }, {
          postName: '开发工程师',
          postId: 5627,
        }, {
          postName: '测试工程师',
          postId: 5628,
        }, {
          postName: '临时工',
          postId: 383156,
        }],
      },
    })
  },
  [`POST ${apiPrefix}/system/login/doLogin`] (req, res) {
    const { body } = req
    res.cookie('token', JSON.stringify({ ...body }), {
      maxAge: 900000,
      httpOnly: true,
    })
    res.json({ success: true, message: 'Ok' })
  },
  [`POST ${apiPrefix}/system/login/switchPost`] (req, res) {
    const { body } = req
    res.cookie('token', JSON.stringify({ ...body }), {
      maxAge: 900000,
      httpOnly: true,
    })
    res.json({
      code: '200',
      msg: '操作成功',
      data: [{
        menuUrl: '/',
        menuIcon: 'smile-o',
        limitLevel: '1',
        pId: '',
        menuName: '首页',
        orderCode: '1',
        menuType: '1',
        id: '1',
        relationship: '1-1',
      }, {
        menuUrl: '/system/org',
        menuIcon: 'smile-o',
        limitLevel: '1',
        pId: '5118',
        menuName: '基础信息管理',
        orderCode: '1',
        menuType: '1',
        id: '5120',
        relationship: '5118-5120',
      }, {
        menuUrl: '/system/role',
        menuIcon: 'smile-o',
        limitLevel: '1',
        pId: '5118',
        menuName: '角色管理',
        orderCode: '2',
        menuType: '1',
        id: '5121',
        relationship: '5118-5121',
      }, {
        menuUrl: '/system/menu',
        menuIcon: 'smile-o',
        limitLevel: '1',
        pId: '5118',
        menuName: '菜单管理',
        orderCode: '3',
        menuType: '1',
        id: '5132',
        relationship: '5118-5132',
      }, {
        menuUrl: '/system/post',
        menuIcon: 'smile-o',
        limitLevel: '1',
        pId: '5118',
        menuName: '岗位管理',
        orderCode: '4',
        menuType: '1',
        id: '5133',
        relationship: '5118-5133',
      }, {
        menuUrl: '/system/limit',
        menuIcon: 'smile-o',
        limitLevel: '1',
        pId: '5118',
        menuName: '权限管理',
        orderCode: '5',
        menuType: '1',
        id: '337671',
        relationship: '5118-337671',
      }, {
        menuUrl: '',
        menuIcon: 'manage_fill',
        limitLevel: '1',
        pId: '',
        menuName: '系统管理',
        orderCode: '10',
        menuType: '1',
        id: '5118',
        relationship: '5118',
      }],
    })
  },
  [`GET ${apiPrefix}/user/logout`] (req, res) {
    res.clearCookie('token')
    res.status(200).end()
  },

  [`GET ${apiPrefix}/user`] (req, res) {
    const cookie = req.headers.cookie || ''
    const cookies = qs.parse(cookie.replace(/\s/g, ''), { delimiter: ';' })
    const response = {}
    const user = {}
    if (!cookies.token) {
      res.status(200).send({ message: 'Not Login' })
      return
    }
    const token = JSON.parse(cookies.token)
    if (token) {
      response.success = token.deadline > new Date().getTime()
    }
    if (response.success) {
      const userItem = adminUsers.filter(_ => _.id === token.id)
      if (userItem.length > 0) {
        user.permissions = userItem[0].permissions
        user.username = userItem[0].username
        user.id = userItem[0].id
      }
    }
    response.user = user
    res.json(response)
  },

  [`GET ${apiPrefix}/users`] (req, res) {
    const { query } = req
    let { pageSize, page, ...other } = query
    pageSize = pageSize || 10
    page = page || 1

    let newData = database
    for (let key in other) {
      if ({}.hasOwnProperty.call(other, key)) {
        newData = newData.filter((item) => {
          if ({}.hasOwnProperty.call(item, key)) {
            if (key === 'address') {
              return other[key].every(iitem => item[key].indexOf(iitem) > -1)
            } else if (key === 'createTime') {
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

    res.status(200).json({
      data: newData.slice((page - 1) * pageSize, page * pageSize),
      total: newData.length,
    })
  },

  [`DELETE ${apiPrefix}/users`] (req, res) {
    const { ids } = req.body
    database = database.filter((item) => !ids.some(_ => _ === item.id))
    res.status(204).end()
  },


  [`POST ${apiPrefix}/user`] (req, res) {
    const newData = req.body
    newData.createTime = Mock.mock('@now')
    newData.avatar = newData.avatar || Mock.Random.image('100x100', Mock.Random.color(), '#757575', 'png', newData.nickName.substr(0, 1))
    newData.id = Mock.mock('@id')

    database.unshift(newData)

    res.status(200).end()
  },

  [`GET ${apiPrefix}/user/:id`] (req, res) {
    const { id } = req.params
    const data = queryArray(database, id, 'id')
    if (data) {
      res.status(200).json(data)
    } else {
      res.status(404).json(NOTFOUND)
    }
  },

  [`DELETE ${apiPrefix}/user/:id`] (req, res) {
    const { id } = req.params
    const data = queryArray(database, id, 'id')
    if (data) {
      database = database.filter((item) => item.id !== id)
      res.status(204).end()
    } else {
      res.status(404).json(NOTFOUND)
    }
  },
  [`PATCH ${apiPrefix}/user/:id`] (req, res) {
    const { id } = req.params
    const editItem = req.body
    let isExist = false

    database = database.map((item) => {
      if (item.id === id) {
        isExist = true
        return Object.assign({}, item, editItem)
      }
      return item
    })

    if (isExist) {
      res.status(201).end()
    } else {
      res.status(404).json(NOTFOUND)
    }
  }, [`POST ${apiPrefix}/security`] (req, res) {      //  货帮主 hbz HBZ 登陆
    const { body } = req
    res.cookie('token', JSON.stringify({ ...body }), {
      maxAge: 900000,
      httpOnly: true,
    })
    const { j_pass } = body
    if (j_pass && j_pass === '111111') {
      res.json({
        code: '200',
        msg: '操作成功',
        data: {
          'X-AUTH-TOKEN': '918770acad0e4ea58fec334ef8b61756',
          'X-AUTH-USER': '15608209129',
        },
      })
    } else {
      res.json({
        code: '202',
        msg: '密码错误',
        data: null,
      })
    }
  }, [`POST ${apiPrefix}/web/user/getUserRoleMenu`] (req, res) {     //  货帮主 hbz HBZ 获取用户信息
    const { body } = req
    res.cookie('token', JSON.stringify({ ...body }), {
      maxAge: 900000,
      httpOnly: true,
    })
    res.json({
      code: '200',
      msg: '操作成功',
      data: {
        user: { id: '123456', name: '张三' },
        role: { id: '34', name: '管理员' },
        menu: [
          { id: '1', pId: '0', name: '货主', url: 'null' },
          { id: '2', pId: '1', name: '专线货源', url: '/get/goodsList' },
          { id: '3', pId: '1', name: '其他货源', url: '/get/otherList' },
        ],
      },
    })
  }, [`POST ${apiPrefix}/user/authCode/send`] (req, res) {      //  货帮主 hbz HBZ 获取短信
    res.json({
      code: '200',
      msg: '操作成功',
      data: true,
    })
  }, [`POST ${apiPrefix}/user/authCode/peek`] (req, res) {      //  货帮主 hbz HBZ 获取验证码图片
    res.json({
      code: '200',
      msg: '操作成功',
      data: "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",
    })
  },
}

// send: '/user/authCode/send',
// peek: '/user/authCode/peek'
