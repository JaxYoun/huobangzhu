import {
  makeAuthorities,
  addRole,
  disableGuest,
  getRegisterInfo,
  queryAuthority,
  makeMenus,
  queryMenus,
  deleteRole,
  getRoleCode,
  updateRecord,
} from '../services/roleManagement'

export default {
  namespace: 'roleManagement',
  state: {
    fields: [],
    tableTime: 0,
    authorityModal: {
      visible: false,
    },
    menuModal: {
      visible: false,
      checkedMenuKeys: [],
    },
    roleModal: {
      visible: false,
    },
    roleCode: {},
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen(location => {
        if (location.pathname === '/userMessage/roleManagement') {
          dispatch({
            type: 'getRoleCode',
            payload: { enumname: 'Role' },
          })
        }
      })
    },
  },
  effects: {
    *update ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    *getRoleCode ({ payload }, { call, put }) {
      const res = yield call(getRoleCode, payload)
      if (res.success) {
        yield put({
          type: 'update',
          payload: { roleCode: res.data },
        })
      }
    },
    *addRole ({ payload }, { call, put }) {
      const res = yield call(addRole, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date(), roleModal: { visible: false } },
        })
      }
    },
    *updateRecord ({ payload }, { call, put }) {
      const res = yield call(updateRecord, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date() },
        })
      }
    },
    *queryAuthority ({ payload }, { call, put, select }) {
      const prestate = yield select(_ => _.roleManagement)
      const authorityList = yield call(queryAuthority, {})
      const roleAuthority = yield call(queryAuthority, payload)
      if (roleAuthority.success && authorityList.success) {
        yield put({
          type: 'updateStates',
          payload: {
            authorityModal: {
              ...prestate.authorityModal,
              authorityList: authorityList.data,
              roleAuthority: roleAuthority.data.map(item => item.id),
            },
          },
        })
      } else {
        const errorMessage = { message: '数据加载失败，请重试！' }
        throw errorMessage
      }
    },
    *makeAuthorities ({ payload }, { call, put }) {
      const res = yield call(makeAuthorities, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            tableTime: new Date(),
            authorityModal: { visible: false },
          },
        })
      } else {
        const errorMessage = { message: '操作失败，请重试！' }
        throw errorMessage
      }
    },
    *queryMenus ({ payload }, { call, put, select }) {
      const prestate = yield select(_ => _.roleManagement)
      const menuList = yield call(queryMenus, {})
      const roleMenu = yield call(queryMenus, payload)
      if (menuList.success && roleMenu.success) {
        yield put({
          type: 'updateStates',
          payload: {
            menuModal: {
              ...prestate.menuModal,
              menuList: menuList.data,
              roleMenu: roleMenu.data.map(item => item.id),
            },
          },
        })
      } else {
        const errorMessage = { message: '数据加载失败，请重试！' }
        throw errorMessage
      }
    },
    *updateMenuKeys ({ payload }, { put, select }) {
      const prestate = yield select(_ => _.roleManagement)
      yield put({
        type: 'updateStates',
        payload: {
          menuModal: {
            ...prestate.menuModal,
            ...payload,
          },
        },
      })
    },
    *makeMenus ({ payload }, { call, put }) {
      const res = yield call(makeMenus, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date(), menuModal: { visible: false } },
        })
      } else {
        const errorMessage = { message: '操作失败，请重试！' }
        throw errorMessage
      }
    },
    *disableGuest ({ payload }, { call, put }) {
      const res = yield call(disableGuest, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date() },
        })
      }
    },
    *deleteRecord ({ payload }, { call, put }) {
      const res = yield call(deleteRole, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            tableTime: new Date(),
          },
        })
      }
    },
    *getRegisterInfo ({ payload }, { call, put }) {
      const res = yield call(getRegisterInfo, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            detailModal: {
              visible: true,
              ...res.data,
            },
          },
        })
      }
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
  },
}
