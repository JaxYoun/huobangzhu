
import { getUser, imageChange } from 'services/personalCenter'
import { browserHistory } from 'react-router'

export default {
  namespace: 'personalInfo',
  state: {
    userDetail: '',
    imgUrlSmall: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/personalInfo') {
          dispatch({ type: 'getUserInfo' })
        }
      })
    },
  },
  effects: {
    * changeStates ({
        payload,
      }, { put }) {
      yield put({ type: 'updateState', payload })
    },
    * getUserInfo ({ payload = {} }, { call, put }) {
      const res = yield call(getUser, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            userDetail: res.data,
          },
        })
      }
    },
    * changeImage ({ payload }, { call }) {
      const res = yield call(imageChange, payload)
      if (res.success) {
        browserHistory.push('/personalCenter')
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
  },
}
