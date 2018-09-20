import { typeval } from '../services/app'
import { addRule, updateRule } from '../services/ruleManage'

const typevalObj = {
  adjusts: 'FORMULA_ADJUST_TYPE',
  states: 'FORMULA_STATE',
  types: 'FORMULA_TYPE',
}
export default {
  namespace: 'ruleManage',
  state: {
    ruleConfigure: {
      fields: '',
      tableTime: 0,
      adjusts: [],
      states: [],
      types: [],
      visible: false,
      ruleDetail: '',
    },
    integralQuery: {
      fields: '',
      tableTime: 0,
    },
    reputationQuery: {
      fields: '',
      tableTime: 0,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        dispatch({
          type: 'getTypeval',
        })
        if (location.pathname === '/userMessage/ruleManage/ruleConfigure') {
          dispatch({
            type: 'changeStates',
            payload: {
              fields: '',
              parent: 'ruleConfigure',
            },
          })
        }
        if (location.pathname === '/userMessage/ruleManage/integralQuery') {
          dispatch({
            type: 'changeStates',
            payload: {
              fields: '',
              parent: 'integralQuery',
            },
          })
        }
        if (location.pathname === '/userMessage/ruleManage/reputationQuery') {
          dispatch({
            type: 'changeStates',
            payload: {
              fields: '',
              parent: 'reputationQuery',
            },
          })
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
    * getTypeval ({ payload }, { call, put }) {
      for (let key in typevalObj) {
        if (Object.prototype.hasOwnProperty.call(typevalObj, key)) {
          const res = yield call(typeval, typevalObj[key])
          if (res.success) {
            yield put({
              type: 'updateState',
              payload: {
                [key]: res.data,
                parent: 'ruleConfigure',
              },
            })
          }
        }
      }
    },
    * updateRule ({ payload }, { call, put }) {
      let toDo = payload.id ? updateRule : addRule
      const res = yield call(toDo, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            visible: false,
            tableTime: new Date().getTime(),
            parent: 'ruleConfigure',
          },
        })
      }
    },
  },
  reducers: {
    updateState (state, { payload }) {
      let parent = state[payload.parent]
      return { ...state, [payload.parent]: { ...parent, ...payload } }
    },
  },
}
