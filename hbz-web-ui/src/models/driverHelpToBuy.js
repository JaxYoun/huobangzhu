import { nearQuery } from 'services/driverHelpToBuy'
// import { routerRedux } from 'dva/router'

export default {
  namespace: 'driverHelpToBuy',
  state: {
    buyOrderList: [],
    fields: '',
    tableTime: 0,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/driverEnterprise/driverHelpToBuy') {
          // dispatch({ type: 'queryBuyOrder' })
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
    * queryBuyOrder ({ payload = {} }, { call, put }) {
      const res = yield call(nearQuery, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            buyOrderList: res.data,
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
