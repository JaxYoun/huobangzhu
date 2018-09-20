import { enums } from 'services/app'
import { getDetail } from 'services/driverHelpToBuy'
import { getLogistics, takeUrl, okUrl, refuseUrl, drivingAgreeUrl, refuseAgreeUrl } from 'services/receiveOrderList'
import { getOrderId } from 'utils'
import { browserHistory } from 'react-router'

const apiObj = {
  take: takeUrl,
  ok: okUrl,
  refuse: refuseUrl,
  drivingAgree: drivingAgreeUrl,
  refuseAgree: refuseAgreeUrl,
}
export default {
  namespace: 'receiveDetail',
  state: {
    id: '',
    receiveDetailData: '',
    logisticsData: [],
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/receiveOrderList/receiveDetail') {
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
    * queryDetail ({ payload }, { call, put }) {
      const res = yield call(getDetail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            receiveDetailData: res.data,
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
      const res = yield call(apiObj[payload.do], payload)
      if (res.success) {
        browserHistory.push('/personalCenter/receiveOrderList')
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
