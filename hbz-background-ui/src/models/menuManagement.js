import {
  queryMenus,
  menuSave,
  menuDelete,
  menuMerge,
  getWebModel,
} from '../services/menuManagement'
import { arrayToTree } from 'utils'

export default {
  namespace: 'menuManagement',

  state: {
    currentPid: '',
    userData: [],
    menuData: [],
    menuSimpleData: [],
    menuModal: {
      visible: false,
      ModalTitle: '菜单管理',
      values: {},
    },
    webModuleList: {},
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen(location => {
        if (location.pathname === '/userMessage/menuManagement') {
          dispatch({
            type: 'query',
          })
          dispatch({
            type: 'getWebModel',
          })
        }
      })
    },
  },

  effects: {
    *query ({ payload = {} }, { call, put }) {
      const res = yield call(queryMenus, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            menuSimpleData: res.data,
            menuData: arrayToTree(res.data, 'id', 'parentId'),
          },
        })
      }
    },
    *getWebModel ({ payload }, { call, put }) {
      const res = yield call(getWebModel, { enumname: 'WebModule' })
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            webModuleList: res.data,
          },
        })
      }
    },
    *handleModal ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    *menuSave ({ payload }, { call, put, select }) {
      const preState = yield select(_ => _.menuManagement)
      const { value, form } = payload
      const res = yield call(menuSave, value)
      if (res.success) {
        form.resetFields()
        yield put({ type: 'query' })
        yield put({
          type: 'updateStates',
          payload: {
            menuModal: {
              visible: false,
            },
          },
        })
      } else {
        yield put({
          type: 'updateStates',
          payload: {
            menuModal: {
              ...preState.menuModal,
              visible: true,
            },
          },
        })
      }
    },
    *menuDelete ({ payload }, { call, put }) {
      const res = yield call(menuDelete, { id: payload.id })
      if (res.success) {
        yield put({ type: 'query' })
        yield put({ type: 'updateStates', payload: { currentPid: '' } })
      }
    },
  },

  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    confirmLoading (state, { payload }) {
      const confirmProps = state[payload]
      confirmProps.confirmLoading = !confirmProps.confirmLoading
      return { ...state, confirmProps }
    },
  },
}
