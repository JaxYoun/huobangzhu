import { routerRedux } from 'dva/router'
import { enums } from 'services/app'
import { orderFslQuery } from 'services/carCollect'

const getApiType = () => {
  const apiType = location.pathname.includes('LtlOrder') ? 'LtlOrder' : 'fslOrder'
  return apiType
}

export default {
  namespace: 'carCollect',
  state: {
    carDetail: [],
    fields: {
      settlementType: 'LEVY_ONLINE_PAYMENT',
      orderTypes: ['FSL', 'LTL'],
    },
    tableTime: 0,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/carCollect') {
          dispatch({
            type: 'changeStates',
            payload: {
              fields: {
                settlementType: 'LEVY_ONLINE_PAYMENT',
                orderTypes: ['FSL', 'LTL'],
              },
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
    * myOrderQuery ({ payload = {} }, { call, put }) {
      const res = yield call(orderFslQuery, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            carDetail: res.data,
          },
        })
      }
    },
    * queryCarCollect ({ payload }, { call, put }) {
      const res = yield call(orderFslQuery, { ...payload, settlementType: 'LEVY_ONLINE_PAYMENT', orderTypes: ['FSL', 'LTL'] })
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            carDetail: res.data,
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
