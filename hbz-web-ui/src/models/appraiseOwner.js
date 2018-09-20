import { enums } from 'services/app'

export default {
  namespace: 'appraiseOwner',
  state: {
    fields: '',
    tableTime: 0,
    orderTypeData: [],
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/appraiseOrder/appraiseOwner') {
          dispatch({ type: 'queryEnums' })
          dispatch({
            type: 'changeStates',
            payload: {
              fields: { type: 'CONSIGNOR' },
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
    * queryEnums ({ payload }, { call, put }) {
      const { data: orderTypeData } = yield call(enums, 'OrderType')
      yield put({
        type: 'updateState',
        payload: {
          orderTypeData,
        },
      })
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
