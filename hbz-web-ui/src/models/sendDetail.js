import { enums } from 'services/app'
import { getDetail } from 'services/driverHelpToBuy'
import { confirmSign, doFslAgree, doLtlAgree } from 'services/sendOrderList'
import { getLogistics } from 'services/receiveOrderList'
import { getOrderId } from 'utils'
import { browserHistory } from 'react-router'

export default {
  namespace: 'sendDetail',
  state: {
    id: '',
    sendDetailData: '',
    logisticsData: [],
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/sendOrderList/sendDetail') {
          const orderIds = getOrderId(location, 'orderId')
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds },
          })
          dispatch({
            type: 'queryDetail',
            payload: {
              id: orderIds,
            },
          })
          dispatch({
            type: 'queryLogistics',
            payload: {
              orderId: orderIds,
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
    * queryDetail ({ payload }, { call, put }) {
      const res = yield call(getDetail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            sendDetailData: res.data,
          },
        })
      }
    },
    * queryLogistics ({ payload }, { call, put }) {
      const res = yield call(getLogistics, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            logisticsData: res.data,
          },
        })
      }
    },
    * confirmSign ({ payload }, { call }) {
      const res = yield call(confirmSign, payload)
      if (res.success) {
        browserHistory.push('/personalCenter/sendOrderList')
      }
    },
    * doAgree ({ payload }, { call }) {
      const res = yield call(payload.orderType === 'FSL' ? doFslAgree : doLtlAgree, { id: payload.orderId })
      if (res.success) {
        browserHistory.push('/personalCenter/sendOrderList')
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
