// import { enums } from 'services/app'
import { getDetail } from 'services/driverHelpToBuy'
import { getOrderId } from 'utils'
import { browserHistory } from 'react-router'
import { comment } from 'services/appraiseOrder'

export default {
  namespace: 'appraise',
  state: {
    id: '',
    orderDetail: '',
    type: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/appraiseOrder/appraise') {
          const orderIds = getOrderId(location, 'orderId')
          const types = getOrderId(location, 'type')
          console.log('types', types)
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds, type: types },
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
    * queryDetail ({ payload }, { call, put }) {
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
    * comment ({ payload }, { call, select }) {
      const { appraise } = yield (select(_ => _))
      const type = appraise.type
      console.log('type', type)
      const res = yield call(comment, payload)
      if (res.success) {
        let url = ''
        type === 'driver' ?
        url = '/personalCenter/appraiseOrder/appraiseDriver' :
        url = '/personalCenter/appraiseOrder/appraiseOwner'
        browserHistory.push(url)
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
