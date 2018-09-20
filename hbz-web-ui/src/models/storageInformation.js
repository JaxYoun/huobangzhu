
import { getStorage, getBatchId } from 'services/storageInformation'

export default {
  namespace: 'storageInformation',
  state: {
    storageList: [],
    fields: '',
    tableTime: 0,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/storageCenter/storageInformation') {
          // dispatch({
          //   type: 'queryStorage',
          // })
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
    * queryStorage ({ payload = {} }, { call, put }) {
      const res = yield call(getStorage, payload)
      if (res.success) {
        const data = res.data.content
        for (let item of data) {
          if (item.titleImageBach) {
            const respones = yield call(getBatchId, { id: item.titleImageBach })
            if (respones.success) {
              item.imageId = respones.data
            }
          }
        }
        yield put({
          type: 'updateState',
          payload: {
            storageList: data,
          },
        })
        console.log('storageList', data)
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
