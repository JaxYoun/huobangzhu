import { create } from 'services/helpToBuy'
import { enums } from 'services/app'
import { addressQuery } from 'services/consignorEnterprise'
import { routerRedux } from 'dva/router'
import { browserHistory } from 'react-router'

export default {
  namespace: 'helpToBuy',
  state: {
    userAddrsList: [],
    visible: false,
    confirmLoading: false,
    timeLimit: [],
    loading: false,
    defaultStartTime: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/helpToBuy') {
          dispatch({ type: 'queryEnums' })
          dispatch({ type: 'changeStates', payload: { defaultStartTime: '' } })
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
      const { data: timeLimit } = yield call(enums, 'TimeLimit')
      yield put({
        type: 'changeStates',
        payload: {
          timeLimit,
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
        browserHistory.push(`/consignorEnterprise/helpToBuy/helpToBuyComfirm?orderId=${res.data.id}`)
      }
    },
    * addrsModal ({ payload }, { put, call }) {
      yield put({
        type: 'updateState',
        payload: {
          visible: true,
        },
      })
      const res = yield call(addressQuery, payload)
      if (res.success) {
        yield put({ type: 'updateState', payload: { userAddrsList: res.data } })
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
