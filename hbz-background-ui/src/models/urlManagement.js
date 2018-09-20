import {
  addUrl,
  disableGuest,
  queryUrl,
  deleteUrl,
  updateRecord,
} from '../services/urlManagement'

export default {
  namespace: 'urlManagement',
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
    detailModal: {
      visible: false,
    },
  },
  effects: {
    *update ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    *addUrl ({ payload }, { call, put }) {
      const res = yield call(addUrl, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date(), detailModal: { visible: false } },
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
    *editModal ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload: { detailModal: { visible: true, ...payload } },
      })
    },
    *deleteRecord ({ payload }, { call, put }) {
      const res = yield call(deleteUrl, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            tableTime: new Date(),
          },
        })
      }
    },
    *queryUrl ({ payload }, { call, put, select }) {
      const prestate = yield select(_ => _.authorityManagement)
      const list = yield call(queryUrl, payload)
      if (list.success) {
        yield put({
          type: 'updateStates',
          payload: {
            authorityModal: {
              ...prestate.authorityModal,
              authorityUrl: list.data.map(item => item.id),
            },
          },
        })
      } else {
        const errorMessage = { message: '数据加载失败，请重试！' }
        throw errorMessage
      }
    },
    *disableUrl ({ payload }, { call, put }) {
      const res = yield call(disableGuest, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date() },
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
