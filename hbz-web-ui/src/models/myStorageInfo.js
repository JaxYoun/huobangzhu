// import {  } from 'services/myStorageManage'

export default {
  namespace: 'myStorageInfo',
  state: {
    fields: '',
    tableTime: 0,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/myStorageManage/myStorageInfo') {
          // const orderIds = getOrderId(location, 'orderId')
          // if (orderIds) {
          //   dispatch({ type: 'queryStorage', payload: { id: orderIds } })
          // } else {
          //   dispatch({ type: 'changeStates', payload: { storageData: '' } })
          // }
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
