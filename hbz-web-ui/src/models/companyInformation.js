import { companyDetail, updateCompanyInfo } from 'services/companyInformation'
// import { enums } from 'services/app'
// import { getOrderId } from 'utils'
// import { browserHistory } from 'react-router'
import { message } from 'antd'

export default {
  namespace: 'companyInformation',
  state: {
    companyDetail: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/companyInformation') {
          dispatch({ type: 'queryCompanyDetail' })
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
    * queryCompanyDetail ({ payload = {} }, { call, put }) {
      const res = yield call(companyDetail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            companyDetail: res.data,
          },
        })
      }
    },
    * updateInfo ({ payload }, { call }) {
      const res = yield call(updateCompanyInfo, payload)
      if (res.success) {
        message.success(res.msg)
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
