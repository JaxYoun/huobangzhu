
// import { getStorageDetail } from 'services/storageInformation'
import { getOrderId } from 'utils'

export default {
  namespace: 'specialLineReceipt',
  state: {
    orderType: '',
    fields: '',
    tableTime: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/driverEnterprise/specialLineReceipt/driverFslOrder') {
          dispatch({
            type: 'changeStates',
            payload: {
              orderType: 'flsOrder',
              fields: '',
            },
          })
        }
        if (location.pathname === '/driverEnterprise/specialLineReceipt/driverLtlOrder') {
          dispatch({
            type: 'changeStates',
            payload: {
              orderType: 'ltlOrder',
              fields: '',
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
