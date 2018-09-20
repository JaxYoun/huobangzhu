import { routerRedux } from 'dva/router'
import { parse } from 'qs'
import config from 'config'
import { changeKeyNames } from 'utils'
import { query, logout } from '../services/app'

const { prefix } = config

export default {
  namespace: 'app',
  state: {
    currentLevel: '0',
    user: {},
    permissions: {
      visit: [],
    },
    menu: [],
    menuPopoverVisible: false,
    siderFold: localStorage.getItem(`${prefix}siderFold`) === 'true',
    darkTheme: localStorage.getItem(`${prefix}darkTheme`) === 'true',
    isNavbar: document.body.clientWidth < 769,
    navOpenKeys: JSON.parse(localStorage.getItem(`${prefix}navOpenKeys`)) || [],
  },
  subscriptions: {

    setup ({ dispatch, history }) {
      dispatch({ type: 'query' })
      history.listen(() => {
        setTimeout(() => {
          dispatch({ type: 'setLimitLevel' })
        }, 300)
      })
      // let tid
      // window.onresize = () => {
      //   clearTimeout(tid)
      //   tid = setTimeout(() => {
      //     dispatch({ type: 'changeNavbar' })
      //   }, 300)
      // }
    },

  },
  effects: {

    * query ({
      payload,
    }, { call, put }) {
      const { success, user = {} } = yield call(query, payload)
      const { permissions, menuList } = user
      if (success && user && permissions.visit.length > 0) {
        sessionStorage.userId = user.id
        sessionStorage.userOrgId = user.orgId
        user.postName = user.posts.find(item => item.postId === user.currentPostId).postName
        let menu = changeKeyNames(menuList, { bpid: 'pId', name: 'menuName', route: 'menuUrl', icon: 'menuIcon' })
        for (let key of menu.keys()) {
          menu[key].mpid = menu[key].menuType === '1' ? menu[key].bpid : '-1'
        }
        yield put({
          type: 'updateState',
          payload: {
            user,
            permissions,
            menu,
          },
        })
        if (location.pathname === '/login') {
          yield put(routerRedux.push('/index'))
        }
      } else if (config.openPages && config.openPages.indexOf(location.pathname) < 0) {
        let from = location.pathname
        window.location = `${location.origin}/login?from=${from}`
      }
    },

    * logout ({
      payload,
    }, { call, put }) {
      const data = yield call(logout, parse(payload))
      sessionStorage.isvisited = ''
      if (data.success) {
        yield put({ type: 'query' })
      } else {
        throw (data)
      }
    },

    * changeNavbar ({
      payload,
    }, { put, select }) {
      const { app } = yield (select(_ => _))
      const isNavbar = document.body.clientWidth < 769
      if (isNavbar !== app.isNavbar) {
        yield put({ type: 'handleNavbar', payload: isNavbar })
      }
    },
    * setLimitLevel ({ payload }, { put, select }) {
      const { app } = yield (select(_ => _))
      const { pathname } = location
      const current = app.menu.find(item => item.route === pathname)
      const currentLevel = current ? current.limitLevel : 0
      yield put({ type: 'updateState', payload: { currentLevel } })
    },
  },
  reducers: {
    updateState (state, { payload }) {
      return {
        ...state,
        ...payload,
      }
    },

    switchSider (state) {
      localStorage.setItem(`${prefix}siderFold`, !state.siderFold)
      return {
        ...state,
        siderFold: !state.siderFold,
      }
    },

    switchTheme (state) {
      localStorage.setItem(`${prefix}darkTheme`, !state.darkTheme)
      return {
        ...state,
        darkTheme: !state.darkTheme,
      }
    },

    switchMenuPopver (state) {
      return {
        ...state,
        menuPopoverVisible: !state.menuPopoverVisible,
      }
    },

    handleNavbar (state, { payload }) {
      return {
        ...state,
        isNavbar: payload,
      }
    },

    handleNavOpenKeys (state, { payload: navOpenKeys }) {
      return {
        ...state,
        ...navOpenKeys,
      }
    },
  },
}
