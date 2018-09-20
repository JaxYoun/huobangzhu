import { getData, updateWarehouseManage, queryWarehouseManageDetail, updateLifecycleOverdue } from '../services/storageInfo'

export default {
  namespace: 'storageInfo',
  state: {
    fields: [],
    tableTime: 0,
    typeData: [],
    firmModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/storageManagement/storageInfo') {
          dispatch({
            type: 'update',
            payload: {
              fields: [],
              tableTime: 0,
              typeData: [],
              firmModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
            },
          })
          dispatch({
            type: 'getTypeData',
            payload: { type: 'WarehouseAuditType' },
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
    // 获取审核状态
    * getTypeData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          typeData: res.data,
        },
      })
    },
    // 获取详情
    * getfirmData ({ payload }, { put, call }) {
      const res = yield call(queryWarehouseManageDetail, payload)
      if (res.code === '200') {
        yield put({
          type: 'updatefirmModal',
          payload: {
            firmData: res.data,
            visible: true,
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
    // 修改信息
    * updateInfo ({ payload }, { put, call }) {
      const res = yield call(updateWarehouseManage, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanModal',
          payload,
        })
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
    // 过期
    * outDate ({ payload }, { put, call }) {
      const res = yield call(updateLifecycleOverdue, payload)
      if (res.code === '200') {
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
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
