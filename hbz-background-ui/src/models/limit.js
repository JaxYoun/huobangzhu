import { queryPostOrgTree, queryOrgUserTree, savePostUsers, queryPostUsers } from '../services/limit'
import { config } from 'utils'

export default {
  namespace: 'limit',

  state: {
    postData: [], // 所有岗位列表
    userData: [], // 所有岗位人员列表
    userKeys: [], // 当前选中岗位对应人员keys
    postId: [], // 当前选中岗位key
    temp: {},
    loading: false,
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/system/limit') {
          dispatch({
            type: 'queryPostOrg',
          })
          dispatch({
            type: 'queryUser',
          })
        }
      })
    },
  },

  effects: {
    * queryPostOrg ({ payload = {} }, { call, put, select }) {
      const todos = yield select(state => state)
      const res = yield call(queryPostOrgTree, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            postData: res.data,
          },
        })
      }
    },
    * queryUser ({ payload = {} }, { call, put }) {
      const res = yield call(queryOrgUserTree, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            userData: res.data,
          },
        })
      }
    },
    * queryPostUsers ({ payload }, { call, put }) {
      const { id } = payload
      const res = yield call(queryPostUsers, { postId: id.split('-')[1] })

      if (res.success) {
        let userKeys = []
        for (let value of res.data.values()) {
          userKeys.push(`user-${value}`)
        }
        yield put({
          type: 'updateStates',
          payload: {
            userKeys,
          },
        })
      }
      yield put({ type: 'updateStates', payload: { postId: id } })
    },
    * onChangeUser ({ payload }, { call, put }) {
      let { selectedRows } = payload
      let userKeys = []
      for (let item of selectedRows) {
        userKeys.push(item.id)
      }
      yield put({
        type: 'updateStates',
        payload: {
          userKeys,
        },
      })
    },
    * savePostUser ({ payload }, { call, put }) {
      let { postId, userIds } = payload
      yield put({
        type: 'updateStates',
        payload: {
          loading: true,
        },
      })
      yield call(savePostUsers, { postId, userIds })
      yield put({
        type: 'updateStates',
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
  },
}
