import { saveStorage, updateStorage, queryStroage } from 'services/myStorageManage'
import { getOrderId } from 'utils'
import { browserHistory } from 'react-router'

export default {
  namespace: 'myStorageEdit',
  state: {
    storageData: '',
    imgUrlSmall: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/myStorageManage/myStorageEdit') {
          const orderIds = getOrderId(location, 'orderId')
          if (orderIds) {
            dispatch({ type: 'queryStorage', payload: { id: orderIds } })
          } else {
            dispatch({ type: 'changeStates', payload: { storageData: '' } })
          }
          dispatch({ type: 'changeStates', payload: { imgUrlSmall: '' } })
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
    * queryStorage ({ payload }, { call, put }) {
      const res = yield call(queryStroage, payload)
      if (res.success) {
        yield put({
          type: 'changeStates',
          payload: {
            storageData: res.data,
          },
        })
      }
    },
    * save ({ payload }, { call }) {
      const res = yield call(payload.id ? updateStorage : saveStorage, payload)
      if (res.success) {
        browserHistory.push('/personalCenter/myStorageManage/myStorageInfo')
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
