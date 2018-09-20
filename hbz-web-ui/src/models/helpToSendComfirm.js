import { create, confirmProxyDelivery, computePrice, confirmOrder } from 'services/helpToSend'
import { browserHistory } from 'react-router'
import { enums } from 'services/app'
import { getOrderId } from 'utils'

export default {
  namespace: 'helpToSendComfirm',
  state: {
    timeLimitData: [],
    orderDetail: '',
    id: '',
    referToPrice: 0, // 平台参考价格
    visible: false,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/helpToSend/helpToSendComfirm') {
          const orderIds = getOrderId(location, 'orderId')
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds },
          })
          dispatch({
            type: 'queryOrderDetail',
            payload: {
              id: orderIds,
            },
          })
          dispatch({ type: 'queryEnums' })
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
      const { data: timeLimitData } = yield call(enums, 'TimeLimit')
      yield put({
        type: 'updateState',
        payload: {
          timeLimitData,
        },
      })
    },
    * queryOrderDetail ({ payload }, { call, put }) {
      const res = yield call(confirmProxyDelivery, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            orderDetail: res.data,
          },
        })
        let { originX, originY, destX, destY } = res.data
        let obj = {
          originX,
          originY,
          destX,
          destY,
        }
        yield put({
          type: 'countSendPrice',
          payload: obj,
        })
      }
    },
    * countSendPrice ({ payload }, { call, put }) {
      const res = yield call(computePrice, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            referToPrice: res.data,
          },
        })
      }
    },
    * confirmOrder ({ payload }, { call }) {
      const res = yield call(confirmOrder, { id: payload.orderId, amount: payload.amount })
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
