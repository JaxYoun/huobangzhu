// import { enums } from 'services/app'
// import { createOrder } from 'services'
import { queryAvailableTender } from 'services/vehicleCollect'

export default {
  namespace: 'vehicleCollect',
  state: {
    vehicleList: [],
    fields: {
      orderTrans: 'ORDER_TO_BE_RECEIVED',
      settlementType: 'LEVY_ONLINE_PAYMENT',
    },
    tableTime: 0,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/driverEnterprise/vehicleCollect') {
          // dispatch({
          //   type: 'queryVehicleList',
          //   payload: {
          //     orderTrans: 'ORDER_TO_BE_RECEIVED',
          //     settlementType: 'LEVY_ONLINE_PAYMENT',
          //   },
          // })
          dispatch({
            type: 'changeStates',
            payload: {
              fields: {
                orderTrans: 'ORDER_TO_BE_RECEIVED',
                settlementType: 'LEVY_ONLINE_PAYMENT',
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
    * queryVehicleList ({ payload = {} }, { call, put }) {
      const res = yield call(queryAvailableTender, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            vehicleList: res.data,
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
