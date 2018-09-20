import {
  getUser,
  getRegistry,
  registrySearch,
  enterpriseConsignorSubmit,
  transEnterpriseSubmit,
} from 'services/personalCenter'
const submit = {
  enterpriseConsignor: enterpriseConsignorSubmit,
  transEnterprise: transEnterpriseSubmit,
}
import { browserHistory } from 'react-router'

export default {
  namespace: 'personalCenter',
  state: {
    userInfo: '',
    register: '',
    enterpriseConsignor: {
      iDcardImage: '',
      licenceImage: '',
      registryData: '',
    },
    transEnterprise: {
      iDcardImage: '',
      licenceImage: '',
      registryData: '',
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter') {
          dispatch({ type: 'personalCenter/getUserInfo' })
        }
      })
    },
  },
  effects: {
    * update ({ payload }, { put }) {
      yield put({
        type: 'updateState',
        payload,
      })
    },
    * updateOther ({ payload }, { put }) {
      yield put({
        type: 'updateOtherStates',
        payload,
      })
    },
    * getUserInfo ({ payload = {} }, { call, put }) {
      const res = yield call(getUser, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            userInfo: res.data,
          },
        })
      }
      const respone = yield call(getRegistry, payload)
      if (respone.success) {
        yield put({
          type: 'updateState',
          payload: {
            register: respone.data,
          },
        })
      }
    },
    * queryRegistry ({ payload }, { call, put }) {
      const res = yield call(registrySearch, payload.questFields)
      if (res.success) {
        yield put({
          type: 'updateOtherStates',
          payload: {
            registryData: res.data,
            parent: payload.parent,
            iDcardImage: res.data.certificates,
            licenceImage: '',
          },
        })
      }
    },
    * submit ({ payload }, { call }) {
      const res = yield call(submit[payload.tabs], payload.value)
      if (res.success) {
        browserHistory.goBack()
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
    updateOtherStates (state, { payload }) {
      let parent = state[payload.parent]
      return { ...state, [payload.parent]: { ...parent, ...payload } }
    },
  },
}
