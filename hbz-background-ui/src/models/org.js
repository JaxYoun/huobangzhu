import {
  queryOrgList,
  queryUserList,
  orgSave,
  userSave,
  deleteUser,
  deleteOrg,
  activateUser,
  inactivateUser,
 } from '../services/org'

export default {
  namespace: 'org',

  state: {
    currentOrg: '',
    orgData: [],
    orgModal: {},
    userData: [],
    userModal: {},
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/system/org') {
          dispatch({
            type: 'queryOrg',
          })
          dispatch({
            type: 'queryUser',
          })
        }
      })
    },
  },

  effects: {

    * queryOrg ({ payload = {} }, { call, put }) {
      const res = yield call(queryOrgList, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            orgData: res.data,
          },
        })
      }
    },
    * queryUser ({ payload = {} }, { call, put }) {
      yield put({ type: 'updateStates', payload: { currentOrg: payload.orgId } })
      const res = yield call(queryUserList, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            userData: res.data,
          },
        })
      }
    },
    * handleModal ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * saveUser ({ payload }, { call, put }) {
      const { values, form } = payload
      const res = yield call(userSave, values)
      if (res.success) {
        form.resetFields()
        yield put({ type: 'queryUser' })
        yield put({ type: 'updateStates', payload: {
          userModal: {
            confirmLoading: false,
            visible: false,
          },
        },
        })
      } else {
        yield put({ type: 'updateStates', payload: {
          userModal: {
            confirmLoading: false,
            visible: true,
          },
        },
        })
      }
    },
    * saveOrg ({ payload }, { call, put }) {
      const { values, form } = payload
      const res = yield call(orgSave, values)
      if (res.success) {
        form.resetFields()
        yield put({ type: 'queryOrg' })
        yield put({ type: 'updateStates', payload: {
          orgModal: {
            confirmLoading: false,
            visible: false,
          },
        },
        })
      }
    },
    * deleteUser ({ payload }, { call, put }) {
      const res = yield call(deleteUser, { id: payload.id })
      res.success && (yield put({ type: 'queryUser' }))
    },
    * deleteOrg ({ payload }, { call, put }) {
      const res = yield call(deleteOrg, { id: payload.id })
      if (res.success) {
        yield put({ type: 'queryOrg' })
        yield put({ type: 'updateStates', payload: { currentOrg: '' } })
      }
    },
    * disableUser ({ payload }, { call, put }) {
      const res = yield call(inactivateUser, payload)
      if (res.success) {
        yield put({ type: 'queryUser' })
      }
    },
    * enableUser ({ payload }, { call, put }) {
      const res = yield call(activateUser, payload)
      if (res.success) {
        yield put({ type: 'queryUser' })
      }
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
