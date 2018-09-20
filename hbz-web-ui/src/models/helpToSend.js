import { create } from 'services/helpToSend'
import { enums } from 'services/app'
import { browserHistory } from 'react-router'

export default {
  namespace: 'helpToSend',
  state: {
    timeLimitData: [],
    defaultStartTime: new Date().getTime(),
    defaultTakeTime: '',
    loading: false,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/helpToSend') {
          dispatch({ type: 'queryEnums' })
          dispatch({
            type: 'changeStates',
            payload: {
              defaultTakeTime: '',
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
      const { data: timeLimitData } = yield call(enums, 'TimeLimit')
      yield put({
        type: 'updateState',
        payload: {
          timeLimitData,
        },
      })
    },
    * creatOrder ({ payload }, { call, put }) {
      const res = yield call(create, payload)
      if (res.success) {
        yield put({
          type: 'changeStates',
          payload: {
            loading: false,
          },
        })
        browserHistory.push(`/consignorEnterprise/helpToSend/helpToSendComfirm?orderId=${res.data.id}`)
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
