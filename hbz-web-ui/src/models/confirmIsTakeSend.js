import { getDetail } from 'services/driverHelpToBuy'
import { sendCarry } from 'services/driverHelpToSend'
import { enums } from 'services/app'
import { getOrderId } from 'utils'
import { browserHistory } from 'react-router'

export default {
  namespace: 'confirmIsTakeSend',
  state: {
    id: '',
    sendOrderDetail: '',
    timeLimit: [],
    visible: false,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/driverEnterprise/driverHelpToSend/confirmIsTakeSend') {
          const orderIds = getOrderId(location, 'orderId')
          dispatch({ type: 'queryEnums' })
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
    * queryEnums ({ payload }, { call, put }) {
      const { data: timeLimit } = yield call(enums, 'TimeLimit')
      yield put({
        type: 'updateState',
        payload: {
          timeLimit,
        },
      })
    },
    * queryDetail ({ payload = {} }, { call, put }) {
      const res = yield call(getDetail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            sendOrderDetail: res.data,
          },
        })
      }
    },
    * takeSend ({ payload }, { call }) {
      const res = yield call(sendCarry, { id: payload.orderId })
      if (res.success) {
        browserHistory.push('/driverEnterprise/driverHelpToSend')
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
