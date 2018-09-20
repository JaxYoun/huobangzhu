import { getData, deleteUser, addUsers, updateUser } from '../services/userManagement'

export default {
  namespace: 'userManagement',
  state: {
    wareTypeData: [],
    fields: [],
    tableTime: 0,
    userClassification: [],
    bankData: [],
    firmModal: {
      visible: false,
      firmData: {},
      loading: false,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/logistics/basicData/userManagement') {
          dispatch({
            type: 'update',
            payload: {
              wareTypeData: [],
              fields: [],
              tableTime: 0,
              userClassification: [],
              bankData: [],
              firmModal: {
                visible: false,
                firmData: {},
                loading: false,
              },
            },
          })
          dispatch({
            type: 'getClassificationData',
            payload: { type: 'UserClassification' },
          })
          dispatch({
            type: 'getBank',
            payload: { type: 'Bank' },
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
    // 获取客户分类数据
    * getClassificationData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          userClassification: res.data,
        },
      })
    },
    // 获取开户行
    * getBank ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          bankData: res.data,
        },
      })
    },
    // 删除客户
    * deleteUsers ({ payload }, { put, call }) {
      const res = yield call(deleteUser, payload)
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
    // 新增客户
    * addUsers ({ payload }, { put, call }) {
      const res = yield call(addUsers, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanModal' })
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
      yield put({
        type: 'updatefirmModal',
        payload: {
          loading: false,
        },
      })
    },
    // 修改客户
    * updateUsers ({ payload }, { put, call }) {
      const res = yield call(updateUser, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanModal' })
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
      yield put({
        type: 'updatefirmModal',
        payload: {
          loading: false,
        },
      })
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
