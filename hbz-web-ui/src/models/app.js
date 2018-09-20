import { routerRedux } from 'dva/router'
import { parse } from 'qs'
import config from 'config'
import { changeKeyNames, queryURL, COOKIE, getOrderId } from 'utils'
import { query, logout, publicTypeval } from 'services/app'

const { prefix } = config

const queryType = {
  driverType: 'Driver',
  capital: 'Registered_funds',
  driverLevel: 'Credit_level',
  security: 'Security_deposit',
  timeLimit: 'PTIME_LIMIT',
  massList: 'EX_WEIGHTEM',
}

export default {
  namespace: 'app',
  state: {
    currentLevel: '0',
    menus: {
      EnterpriseConsignor: [], //  货主菜单
      EnterpriseDriver: [],  // 车主菜单
      EnterprisePersonalCenter: [], // 个人中心菜单
      EnterpriseStorageCenter: [], // 仓储中心
    },
    user: {},
    role: {},
    permissions: {
      visit: [],
    },
    menuPopoverVisible: false,
    siderFold: localStorage.getItem(`${prefix}siderFold`) === 'true',
    darkTheme: localStorage.getItem(`${prefix}darkTheme`) === 'true',
    isNavbar: document.body.clientWidth < 769,
    navOpenKeys: JSON.parse(localStorage.getItem(`${prefix}navOpenKeys`)) || [],
    modalLogin: {
      visible: false,
      modalType: 'login',
      modalData: {},
    },
    tableData: {
      driverType: [],
      capital: [],
      driverLevel: [],
      security: [],
      timeLimit: [],
      massList: [],
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        setTimeout(() => {
          if (config.openPages && config.openPages.indexOf(location.pathname) < 0) {
            dispatch({ type: 'query' })
            const isLogin = getOrderId(location, 'login')
            if (isLogin) {
              dispatch({
                type: 'setModalLogin',
                payload: {
                  visible: true,
                },
              })
            }
          }
          // dispatch({ type: 'setLimitLevel' })
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
      const { success, user, menus } = yield call(query, payload)
      if (success) {
        // let pageMenu = changeKeyNames(menu, { bpid: 'pId', route: 'url', icon: 'menuIcon' })
        // for (let key of pageMenu.keys()) {
        //   pageMenu[key].mpid = pageMenu[key].menuType === '1' ? pageMenu[key].bpid : '-1'
        // }
        for (let key of Object.keys(menus)) {
          menus[key] = changeKeyNames(menus[key], { route: 'url', iconc: 'iconUrl' })
        }
        yield put({
          type: 'updateState',
          payload: {
            user,
            menus,
          },
        })
        // const from = queryURL('from')
        const from = location.search.split('from=')[1]
        if (location.pathname === '/home' && from) {
          yield put(routerRedux.push(from))
        }
      } else if (config.openPages && config.openPages.indexOf(location.pathname) < 0) {
        COOKIE.clearCookie() // 认证过期清除cookie
        let from = location.pathname + location.search
        window.location = `${location.origin}/home?from=${from}`
      }
      yield put({ type: 'queryTypeval' })
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
    * handalModalLogin ({
      payload,
    }, { put }) {
      yield put({ type: 'setModalLogin', payload })
    },
    * showLogin ({
      payload,
    }, { put }) {
      yield put({
        type: 'setModalLogin',
        payload: {
          visible: true,
          modalType: 'login',
        },
      })
    },
    * queryTypeval ({ payload }, { call, put }) {
      console.log('payload')
      for (let key in queryType) {
        if (Object.prototype.hasOwnProperty.call(queryType, key)) {
          console.log(key)
          const res = yield call(publicTypeval, { type: queryType[key] })
          if (res.success) {
            yield put({
              type: 'updateTableData',
              payload: {
                [key]: res.data,
              },
            })
          }
        }
      }
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

    setModalLogin (state, { payload }) {    // 设置 modallogin 的state
      const { modalLogin } = state
      return {
        ...state,
        modalLogin: { ...modalLogin, ...payload },
      }
    },

    updateTableData (state, { payload }) {
      const { tableData } = state
      return {
        ...state,
        tableData: { ...tableData, ...payload },
      }
    },
  },
}
