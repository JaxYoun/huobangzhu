import { nearQuery } from 'services/driverHelpToSend'

export default {
  namespace: 'driverHelpToSend',
  state: {
    sendOrderList: [],
    fields: '',
    tableTime: 0,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/driverEnterprise/driverHelpToSend') {
          // dispatch({ type: 'querySendOrder' })
          dispatch({
            type: 'changeStates',
            payload: {
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
    * querySendOrder ({ payload = {} }, { call, put }) {
      const res = yield call(nearQuery, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            sendOrderList: res.data,
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
