module.exports = {
  apiPrefix: '/api',
  api: {
    userLogin: '/security', // 登陆
    getUserRoleMenu: '/web/user/getUserRoleMenu', // 获取菜单列表
    userLogout: '/system/login/loginOut',
    userInfo: '/userInfo',
    users: '/users',
    user: '/system/login/getUserByUserIdToCache',
    menus: '/system/menu/list',
    switchPost: '/system/login/switchPost',
    queryPostList: '/system/post/queryPostList',
  },
}
