
import { queryBail, creatPayOrder } from 'services/storageInformation'
import { getOrderId } from 'utils'
import { browserHistory } from 'react-router'

export default {
  namespace: 'payBail',
  state: {
    id: '',
    price: '',
    payDetail: '',
    visible: false,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/storageCenter/storageInformation/payBail') {
          const orderIds = getOrderId(location, 'orderId')
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds },
          })
          dispatch({
            type: 'queryBail',
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
    * queryBail ({ payload = {} }, { call, put }) {
      const res = yield call(queryBail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            price: res.data,
          },
        })
      }
    },
    * creatPay ({ payload }, { call, put, select }) {
      const { payBail } = yield (select(_ => _))
      const { id } = payBail
      console.log('id', id)
      const res = yield call(creatPayOrder, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            payDetail: res.data,
          },
        })
        browserHistory.push(`/storageCenter/pay?orderId=${id}`)
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
