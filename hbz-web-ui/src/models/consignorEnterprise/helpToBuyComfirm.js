import { confirmProxyBuy, confirmOrder } from 'services/helpToBuy'
import { getOrderId } from 'utils'
import { browserHistory } from 'react-router'

export default {
  namespace: 'helpToBuyComfirm',
  state: {
    orderDetail: {},
    id: '',
    price: 0, // 配送费用
    visible: false,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/helpToBuy/helpToBuyComfirm') {
          const orderIds = getOrderId(location, 'orderId')
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds, price: 0 },
          })
          dispatch({
            type: 'queryOrderDetail',
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
    * queryOrderDetail ({ payload }, { call, put }) {
      const res = yield call(confirmProxyBuy, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            orderDetail: res.data,
          },
        })
      }
    },
    * comfirmOrder ({ payload }, { call }) {
      const res = yield call(confirmOrder, { id: payload.orderId, remuneration: payload.remuneration })
      if (res.success) {
        browserHistory.push(`/consignorEnterprise/pay?orderId=${payload.orderId}`)
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
