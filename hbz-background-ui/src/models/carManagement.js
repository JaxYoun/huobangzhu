import { getData, deleteCar, addCar, updateCar } from '../services/carManagement'

export default {
  namespace: 'carManagement',
  state: {
    fields: [],
    tableTime: 0,
    carData: [],
    firmModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/logistics/basicData/carManagement') {
          dispatch({
            type: 'update',
            payload: {
              fields: [],
              tableTime: 0,
              carData: [],
              firmModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
            },
          })
          dispatch({
            type: 'getCarData',
            payload: { type: 'VehicleType' },
          })
        }
      })
    },
  },
  effects: {
    * update ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * updatefirmModal ({ payload }, { put }) {
      yield put({
        type: 'updatefirmModalStates',
        payload,
      })
    },
    // 获取车辆类型数据
    * getCarData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          carData: res.data,
        },
      })
    },
    // 删除车辆
    * deleteCars ({ payload }, { put, call }) {
      const res = yield call(deleteCar, payload)
      if (res.code === '200') {
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
    },
    // 清空并关闭
    * cleanModal ({ payload }, { put, call }) {
      yield put({
        type: 'updatefirmModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
        },
      })
    },
    // 新增车辆
    * addCars ({ payload }, { put, call }) {
      const res = yield call(addCar, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanModal' })
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      } else {
        yield put({
          type: 'updatefirmModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 修改车辆
    * updateCars ({ payload }, { put, call }) {
      const res = yield call(updateCar, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanModal' })
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      } else {
        yield put({
          type: 'updatefirmModal',
          payload: {
            loading: false,
          },
        })
      }
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updatefirmModalStates (state, { payload }) {
      let { firmModal } = state
      return { ...state, firmModal: { ...firmModal, ...payload } }
    },
  },
}
