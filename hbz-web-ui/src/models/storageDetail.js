
import { getStorageDetail } from 'services/storageInformation'
import { getOrderId } from 'utils'

export default {
  namespace: 'storageDetail',
  state: {
    id: '',
    detailData: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/storageCenter/storageInformation/storageDetail') {
          const orderIds = getOrderId(location, 'orderId')
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds },
          })
          dispatch({
            type: 'queryStorageDetail',
            payload: {
              id: orderIds,
            },
          })
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
    * queryStorageDetail ({ payload }, { call, put }) {
      const res = yield call(getStorageDetail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            detailData: res.data,
          },
        })
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
