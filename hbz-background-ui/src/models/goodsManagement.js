import { getData, deleteGood, addGood, updateGood } from '../services/goodsManagement'

export default {
  namespace: 'goodsManagement',
  state: {
    fields: [],
    tableTime: 0,
    getpackageData: [],
    firmModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/logistics/basicData/goodsManagement') {
          dispatch({
            type: 'update',
            payload: {
              fields: [],
              tableTime: 0,
              getpackageData: [],
              firmModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
            },
          })
          dispatch({
            type: 'getpackageData',
            payload: { type: 'PackageUnit' },
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
    // 获取包装单位
    * getpackageData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          getpackageData: res.data,
        },
      })
    },
    // 删除
    * deleteGoods ({ payload }, { put, call }) {
      const res = yield call(deleteGood, payload)
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
    // 新增
    * addGoods ({ payload }, { put, call }) {
      const res = yield call(addGood, payload)
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
    // 修改
    * updateGoods ({ payload }, { put, call }) {
      const res = yield call(updateGood, payload)
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
