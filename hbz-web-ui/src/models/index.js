import { enums } from 'services/app'
import { createOrder } from 'services'

export default {
  namespace: 'index',
  state: {
    visible: false,
    confirmLoading: false,
    transTypes: {},
    userAddrsList: [], // 用户常用信息
    orderDetail: {},
    orderId: '', // 选择的订单id
    orderNo: '',
    month: '',
    online: '',
    carOnline: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen(() => {
        setTimeout(() => {

        }, 300)
      })
    },
  },
  effects: {
    * changeStates ({
        payload,
      }, { put }) {
      yield put({ type: 'updateState', payload })
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
