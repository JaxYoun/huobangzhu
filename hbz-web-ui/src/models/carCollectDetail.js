import { enums } from 'services/app'
import { getOrderId } from 'utils'
import { getOrderDetail } from 'services/consignorEnterprise'

export default {
  namespace: 'carCollectDetail',
  state: {
    WeightUnits: [],
    VolumeUnits: [],
    transTypes: {},
    collectDetail: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/carCollect/carCollectDetail') {
          console.log(location.pathname)
          dispatch({ type: 'queryEnums' })
          dispatch({
            type: 'queryCollectDetail',
            payload: { id: getOrderId(location, 'orderId'), apiType: 'fslOrder' },
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
      const { data: transTypes } = yield call(enums, 'TransType')
      const { data: WeightUnits } = yield call(enums, 'WeightUnit')
      const { data: VolumeUnits } = yield call(enums, 'VolumeUnit')
      yield put({
        type: 'updateState',
        payload: {
          transTypes,
          WeightUnits,
          VolumeUnits,
        },
      })
    },
    * queryCollectDetail ({ payload }, { call, put }) {
      const res = yield call(getOrderDetail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            collectDetail: res.data,
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
