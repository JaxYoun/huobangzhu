import { routerRedux } from 'dva/router'
import { enums } from 'services/app'
import { getOrderId } from 'utils'
import { getDetail, tenderGet, consignorGet } from 'services/carCollectFinish'

export default {
  namespace: 'carCollectFinish',
  state: {
    id: '',
    orderDetail: '',
    orderTender: '',
    orderConsignor: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/carCollect/carCollectFinish') {
          const orderIds = getOrderId(location, 'orderId')
          console.log('orderId', orderIds)
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds },
          })
          dispatch({ type: 'queryOrder', payload: { id: orderIds } }) // 查询订单详情
          dispatch({ type: 'queryTender', payload: { orderId: orderIds } }) // 对车辆征集订单查询
          dispatch({ type: 'queryConsignor', payload: { orderId: orderIds } }) // 对车辆征集列表查询
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
    * queryOrder ({ payload }, { call, put }) {
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
    * queryTender ({ payload }, { call, put }) {
      const res = yield call(tenderGet, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            orderTender: res.data,
          },
        })
      }
    },
    * queryConsignor ({ payload }, { call, put }) {
      const res = yield call(consignorGet, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            orderConsignor: res.data[0],
          },
        })
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
