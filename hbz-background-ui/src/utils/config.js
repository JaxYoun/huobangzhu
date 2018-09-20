
module.exports = {
  name: 'troy',
  prefix: 'troy',
  footerText: 'troy  © 2017 Troy',
  logo: '/logo.png',
  logo2: '/logo2.png',
  iconFontCSS: '/iconfont.css',
  iconFontJS: '/iconfont.js',
  CORS: [],
  openPages: ['/login'],
  apiPrefix: '/api',
  api: {
    userLogin: '/system/login/doLogin',
    userLogout: '/system/login/loginOut',
    userInfo: '/userInfo',
    users: '/users',
    user: '/system/login/getUserByUserIdToCache',
    menus: '/system/menu/list',
    switchPost: '/system/login/switchPost',
    queryPostList: '/system/post/queryPostList',
    typeval: '/manager/typeval',
    ruleManage: {
      addRule: '/formula/add',
      updateRule: '/formula/update',
    },
    insideMsg: {
      send: '/manager/sitePushMessage/sendSitePushMessage', // 推送站内消息
      save: '/manager/sitePushMessage/addSitePushMessage', // 新增站内推送消息
      update: '/manager/sitePushMessage/updateSitePushMessage', // 修改站内推送消息
    },
  },
  state: {
    1: '启用',
    0: '停用',
  },
  adjust: {
    1: '快递计价',
    2: '整车专线计价',
    3: '零担专线计价',
    4: '帮我送计价',
  },
  type: {
    0: '用户积分',
    1: '车主信誉',
    2: '货主信誉',
    3: '计价',
  },
}
