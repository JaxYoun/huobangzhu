
// import { getStorageDetail } from 'services/storageInformation'
import { getOrderId } from 'utils'
import { getFlsDetial, getLtlDetial, takeFlsOrder, takeLtlOrder } from 'services/specialLineReceipt'
import { enums } from 'services/app'
import { browserHistory } from 'react-router'

export default {
  namespace: 'driverOrderDetail',
  state: {
    orderType: '',
    orderDetail: '',
    id: '',
    transTypes: {},
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/driverEnterprise/specialLineReceipt/driverOrderDetail') {
          const orderIds = getOrderId(location, 'orderId')
          const orderTypes = getOrderId(location, 'orderType')
          dispatch({
            type: 'queryEnums',
          })
          dispatch({
            type: 'changeStates',
            payload: {
              id: orderIds,
              orderType: orderTypes,
              orderDetail: '',
            },
          })
          setTimeout(() => {
            dispatch({
              type: 'queryDetail',
              payload: {
                id: orderIds,
                orderType: orderTypes,
              },
            })
          }, 300)
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
      const { data: transTypes } = yield call(enums, 'TransType')
      yield put({ type: 'updateState', payload: {
        transTypes,
      },
      })
    },
    * queryDetail ({ payload }, { call, put }) {
      const res = yield call(payload.orderType === 'flsOrder' ? getFlsDetial : getLtlDetial, { id: payload.id })
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            orderDetail: res.data,
          },
        })
      }
    },
    * driverTakeOrder ({ payload }, { call }) {
      const res = yield call(payload.orderType === 'flsOrder' ? takeFlsOrder : takeLtlOrder, { id: payload.id })
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
