import { queryPostList, queryRoleList, onDelete, savePostAndRole, save, findRoleIdsByPost } from '../services/batchpost'


export default {
  namespace: 'batchpost',

  state: {
    roleData: [], // 所有角色列表
    postData: [], // 岗位列表
    postRoleKeys: [], // 当前选中岗位对应角色keys
    postSelected: '', // 当前选中岗位
    loading: false,
    tableTime: '',
    postModal: {
      visible: false,
      confirmLoading: false,
      ModalTitle: '岗位管理',
      values: {
        name: '',
        postName: '',
        orgId: '',
        orderCode: '',
        id: '',
        postDesc: '',
      },
    },
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/system/batchpost') {
          dispatch({
            type: 'queryRole',
          })
          dispatch({
            type: 'queryPost',
          })
        }
      })
    },
  },

  effects: {

    * queryPost ({ payload = {} }, { call, put }) {
      const json = yield call(queryPostList, payload)
      if (json.success) {
        yield put({
          type: 'updateStates',
          payload: {
            postData: json.data,
          },
        })
      }
    },
    * queryRole ({ payload = {} }, { call, put }) {
      const json = yield call(queryRoleList, payload)
      if (json.success) {
        yield put({
          type: 'updateStates',
          payload: {
            roleData: json.data,
          },
        })
      }
    },
    * onDelete ({ payload = {} }, { call, put }) {
      const json = yield call(onDelete, payload)
      if (json.success) {
        yield put({ type: 'queryPost' })
        yield put({
          type: 'updateStates',
          payload: {
            postRoleKeys: [],
            postSelected: {},
            tableTime: new Date(),
          },
        })
      }
    },
    * onChangeRole ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * handlePostModal ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * saveRoleLimit ({ payload }, { call, put }) {
      yield put({ type: 'updateStates', payload: { loading: true } })
      yield call(savePostAndRole, payload)
      yield put({ type: 'updateStates', payload: { loading: false } })
    },
    * save ({ payload }, { call, put }) {
      const { values, form } = payload
      const json = yield call(save, values)
      if (json.success) {
        form.resetFields()
        yield put({ type: 'queryPost' })
        yield put({
          type: 'updateStates',
          payload: {
            postModal: { confirmLoading: false },
            tableTime: new Date(),
          },
        })
      }
    },
    * queryRolesByPostId ({ payload }, { call, put }) {
      const json = yield call(findRoleIdsByPost, payload)
      if (json.success) {
        let keys = []
        for (let value of json.data.values()) {
          keys.push(parseInt(value, 10))
        }
        yield put({
          type: 'updateStates',
          payload: {
            postRoleKeys: keys,
            postSelected: payload,
          },
        })
      }
    },
    * onCellClick ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * updatePostDesc ({ payload = {} }, { put }) {
      yield put({
        type: 'updateStates',
        payload: {
          postDesc: '',
        },
      })
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    confirmLoading (state, { payload }) {
      const confirmProps = state[payload]
      confirmProps.confirmLoading = !confirmProps.confirmLoading
      return { ...state, confirmProps }
    },
  },
}
