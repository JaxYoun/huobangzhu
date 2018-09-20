import { enums } from 'services/app'
import { getUser, getRegistry } from 'services/personalCenter'
import { transSize } from 'services/publishDriverLine'
import { changeCar, addCar } from 'services/carInformation'

export default {
  namespace: 'carInformation',
  state: {
    userInfo: '',
    register: '',
    fields: '',
    visible: false,
    tableTime: 0,
    transTypes: {},
    carDetail: '',
    selectCar: [],
    carSizeModal: {
      transSizeData: [],
      visible: false,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/carInformation') {
          dispatch({ type: 'getUserInfo' })
          dispatch({ type: 'queryEnums' })
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
    * updateModal ({ payload }, { put }) {
      yield put({ type: 'updateCarSizeModal', payload })
    },
    * getUserInfo ({ payload = {} }, { call, put }) {
      const res = yield call(getUser, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            userInfo: res.data,
          },
        })
      }
      const respone = yield call(getRegistry, payload)
      if (respone.success) {
        yield put({
          type: 'updateState',
          payload: {
            register: respone.data,
          },
        })
      }
    },
    * queryEnums ({ payload }, { call, put }) {
      const { data: transTypes } = yield call(enums, 'TransType')
      yield put({
        type: 'updateState',
        payload: {
          transTypes,
        },
      })
    },
    * showCarSizeModal ({ payload = {} }, { call, put }) {
      const res = yield call(transSize, payload)
      if (res.success) {
        yield put({
          type: 'updateCarSizeModal',
          payload: {
            visible: true,
            transSizeData: res.data,
          },
        })
      }
    },
    * addOrChange ({ payload }, { call, put }) {
      const res = yield call(payload.id ? changeCar : addCar, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            visible: false,
            tableTime: new Date().getTime(),
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
    updateCarSizeModal (state, { payload }) {
      let { carSizeModal } = state
      return { ...state, carSizeModal: { ...carSizeModal, ...payload } }
    },
  },
}
