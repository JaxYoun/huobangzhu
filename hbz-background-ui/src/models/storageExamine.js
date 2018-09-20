import { getData, updateWarehouseAudit, queryWarehouseAuditDetail } from '../services/storageExamine'

export default {
  namespace: 'storageExamine',
  state: {
    fields: [],
    tableTime: 0,
    typeData: [],
    firmModal: {
      visible: false,
      firmData: {},
      loading: false,
      type: '',
    },
    detailModal: {
      visible: false,
      firmData: {},
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/storageManagement/storageExamine') {
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
                type: '',
              },
              detailModal: {
                visible: false,
                firmData: {},
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
    * updatedetailModal ({ payload }, { put }) {
      yield put({
        type: 'updatedetailModalStates',
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
    * getdetailData ({ payload }, { put, call }) {
      const res = yield call(queryWarehouseAuditDetail, payload)
      if (res.code === '200') {
        yield put({
          type: 'updatedetailModal',
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
          type: '',
        },
      })
    },
    // 清空并关闭详情模态框
    * cleanDetailModal ({ payload }, { put, call }) {
      yield put({
        type: 'updatedetailModal',
        payload: {
          visible: false,
          firmData: {},
        },
      })
    },
    // 审核状态修改
    * updateExamine ({ payload }, { put, call }) {
      const res = yield call(updateWarehouseAudit, payload)
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
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updatefirmModalStates (state, { payload }) {
      let { firmModal } = state
      return { ...state, firmModal: { ...firmModal, ...payload } }
    },
    updatedetailModalStates (state, { payload }) {
      let { detailModal } = state
      return { ...state, detailModal: { ...detailModal, ...payload } }
    },
  },
}
