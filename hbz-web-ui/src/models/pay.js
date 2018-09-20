
import { enums } from 'services/app'
import { getOrderId } from 'utils'
import { getDetail } from 'services/driverHelpToBuy'

export default {
  namespace: 'pay',
  state: {
    aliPay: '',
    weChat: '',
    union: '',
    payType: '',
    orderDetail: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/pay' || location.pathname === '/driverEnterprise/goPay') {
          console.log('aa')
          const orderIds = getOrderId(location, 'orderId')
          dispatch({ type: 'getPayType' })
          dispatch({ type: 'getOrderDetail', payload: { id: orderIds } })
        } else if (location.pathname === '/storageCenter/pay') {
          dispatch({ type: 'getPayType' })
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
    * getOrderDetail ({ payload }, { put, call }) {
      // const { consignorEnterprise } = yield (select(_ => _))
      // const { orderType, orderId } = consignorEnterprise
      // if (!orderId || !orderType) {
      //   routerRedux.push('/consignorEnterprise/specialLineTransport/orderList')
      //   return
      // }
      const res = yield call(getDetail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            orderDetail: res.data,
          },
        })
      }
    },
    * getPayType ({ payload }, { call, put }) {
      const payTypes = yield call(enums, 'PayType')
      yield put({
        type: 'updateState',
        payload: {
          payType: payTypes.data,
        },
      })
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
