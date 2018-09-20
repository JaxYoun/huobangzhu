// import { nearQuery } from 'services/driverHelpToBuy'
import { enable, disable, queryRole, enterpriseOrg, userAdd, userUpdate, userDelete } from 'services/accountManage'

export default {
  namespace: 'accountManage',
  state: {
    fields: '',
    tableTime: 0,
    availableRole: [],
    accountDetail: '',
    visible: false,
    orgData: [],
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/accountManage') {
          dispatch({
            type: 'changeStates',
            payload: {
              fields: '',
            },
          })
          dispatch({
            type: 'availableRole',
          }) // 查询可用角色
          dispatch({
            type: 'queryOrg',
          }) // 查询组织机构部门
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
    * isEnable ({ payload }, { call, put }) {
      const res = yield call(payload.checked ? enable : disable, { id: payload.id })
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
    },
    * availableRole ({ payload = {} }, { call, put }) {
      const res = yield call(queryRole, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            availableRole: res.data,
          },
        })
      }
    },
    * queryOrg ({ payload = {} }, { call, put }) {
      const res = yield call(enterpriseOrg, payload)
      if (res.success) {
        let obj = {
          id: res.data.id,
          organizationName: res.data.organizationName,
          parentId: null,
          orgType: res.data.orgType,
          createdDate: res.data.createdDate,
          selectable: false,
        }
        res.data.subs.push(obj)
        yield put({
          type: 'updateState',
          payload: {
            orgData: res.data.subs,
          },
        })
      }
    },
    * submit ({ payload }, { call, put }) {
      const res = yield call(payload.id ? userUpdate : userAdd, payload)
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
    * DeletaUser ({ payload }, { call, put }) {
      const res = yield call(userDelete, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
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
  },
}
