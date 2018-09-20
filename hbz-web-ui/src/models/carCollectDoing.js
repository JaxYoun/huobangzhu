import { enums } from 'services/app'
import { getOrderId } from 'utils'
import { getDetail, tenderGet, consignorGet } from 'services/carCollectFinish'

export default {
  namespace: 'carCollectDoing',
  state: {
    id: '',
    transTypes: {},
    orderDetail: '',
    orderTender: '',
    driverList: [],
    takeUserId: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/carCollect/carCollectDoing') {
          console.log(location.pathname)
          const orderIds = getOrderId(location, 'orderId')
          dispatch({ type: 'queryEnums' })
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds },
          })
          dispatch({ type: 'queryOrder', payload: { id: orderIds } }) // 查询订单详情
          dispatch({ type: 'queryTender', payload: { orderId: orderIds } }) // 对车辆征集订单查询
          dispatch({ type: 'queryDriver', payload: { orderId: orderIds } }) // 查询参与司机
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
      yield put({
        type: 'updateState',
        payload: {
          transTypes,
        },
      })
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
    * queryDriver ({ payload }, { call, put }) {
      const res = yield call(consignorGet, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            driverList: res.data,
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
