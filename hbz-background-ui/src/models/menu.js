import { query, menuSave, menuDelete } from '../services/menu'
import { arrayToTree } from 'utils'

export default {
  namespace: 'menu',

  state: {
    currentPid: '',
    userData: [],
    menuData: [],
    menuSimpleData: [],
    menuModal: {
      visible: false,
      confirmLoading: false,
      ModalTitle: '菜单管理',
      values: {},
    },
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/system/menu') {
          dispatch({
            type: 'query',
          })
        }
      })
    },
  },

  effects: {

    * query ({ payload = {} }, { call, put }) {
      const res = yield call(query, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            menuSimpleData: res.data,
            menuData: arrayToTree(res.data, 'id', 'pId'),
          },
        })
      }
    },
    * handleModal ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * menuSave ({ payload }, { call, put }) {
      yield put({
        type: 'confirmLoading',
        payload: 'menuModal',
      })
      const { value, form } = payload
      const res = yield call(menuSave, value)
      if (res.success) {
        form.resetFields()
        yield put({ type: 'query' })
        yield put({ type: 'updateStates', payload: {
          menuModal: {
            confirmLoading: false,
            visible: false,
          },
        },
        })
      } else {
        yield put({ type: 'updateStates', payload: {
          menuModal: {
            confirmLoading: false,
            visible: true,
          },
        },
        })
      }
    },
    * menuDelete ({ payload }, { call, put }) {
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
