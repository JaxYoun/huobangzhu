import {
  makeAuth,
  addAuthority,
  queryUrl,
  deleteAuthority,
  updateRecord,
} from '../services/authorityManagement'

export default {
  namespace: 'authorityManagement',
  state: {
    tableTime: 0,
    authorityModal: {
      visible: false,
      authorityUrl: [],
    },
    menuModal: {
      visible: false,
      checkedMenuKeys: [],
    },
    detailModal: {
      visible: false,
    },
    urlModal: {
      visible: false,
    },
    selectedUrls: null,
  },
  effects: {
    *update ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    *addAuthority ({ payload }, { call, put }) {
      const res = yield call(addAuthority, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date(), detailModal: { visible: false } },
        })
      }
    },
    *queryUrl ({ payload }, { call, put, select }) {
      const prestate = yield select(_ => _.authorityManagement)
      const list = yield call(queryUrl, payload)
      if (list.success) {
        console.log(list)
        yield put({
          type: 'updateStates',
          payload: {
            authorityModal: {
              ...prestate.authorityModal,
              authorityUrl: list.data.content.map(item => item.id),
            },
          },
        })
      } else {
        const errorMessage = { message: '数据加载失败，请重试！' }
        throw errorMessage
      }
    },
    *deleteRecord ({ payload }, { call, put }) {
      const res = yield call(deleteAuthority, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            tableTime: new Date(),
          },
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
    *makeAuth ({ payload }, { call, put }) {
      const res = yield call(makeAuth, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            tableTime: new Date(),
            selectedUrls: null,
            authorityModal: { visible: false },
          },
        })
      } else {
        const errorMessage = { message: '操作失败，请重试！' }
        throw errorMessage
      }
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
  },
}
