import { browserHistory } from 'react-router'
import { enums } from 'services/app'
import { getOrderId } from 'utils'
import { saveCollect } from 'services/carCollectTodo'

export default {
  namespace: 'carCollectTodo',
  state: {
    id: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/carCollect/carCollectTodo') {
          console.log(location.pathname)
          const orderIds = getOrderId(location, 'orderId')
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds },
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
    * saveCollect ({ payload }, { call }) {
      const res = yield call(saveCollect, payload)
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
