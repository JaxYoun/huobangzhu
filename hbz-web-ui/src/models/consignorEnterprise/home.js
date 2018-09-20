import { routerRedux } from 'dva/router'
import { enums, queryUserList } from 'services/app'
import { createOrder, addressQuery, getOrderDetail, orderConfirm } from 'services/consignorEnterprise'

export default {
  namespace: 'consignorEnterprise',
  state: {
    orderType: null,
    visible: false,
    confirmLoading: false,
    transTypes: {},
    userAddrsList: [], // 用户常用信息
    orderDetail: {},
    orderId: null, // 选择的订单id
    orderNo: '',
    month: '',
    online: '',
    carOnline: '',
    aliPay: '',
    weChat: '',
    union: '',
    payType: '',
    WeightUnits: [],  // 重量单位
    VolumeUnits: [],   // 体积单位
    Roles: [], // 角色
    DriverList: [],
    takeUserId: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((match) => {
        const { query } = match
        dispatch({ type: 'setParams', payload: query })
        setTimeout(() => {
          dispatch({ type: 'getSelectLinst' })
          if (location.pathname.includes('confirmOrder')) {
            dispatch({ type: 'getOrderDetail' })
          }
          if (location.pathname.includes('onlinePay')) {
            dispatch({ type: 'getPayType' })
          }
          if (location.pathname.includes('selectedMonthlyPay')) {
            dispatch({ type: 'getMonthlyPayData' })
          }
          if (location.pathname.includes('LtlOrder')) {
            dispatch({ type: 'changeStates', payload: { orderType: 'LtlOrder' } })
          }
          if (location.pathname.includes('fslOrder')) {
            dispatch({ type: 'changeStates', payload: { orderType: 'fslOrder' } })
          }
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
    * setParams ({
        payload,
      }, { put, select }) {
      const { consignorEnterprise } = yield (select(_ => _))
      const orderType = consignorEnterprise.orderType || payload.orderType
      const orderId = consignorEnterprise.orderId || payload.id
      yield put({ type: 'updateState', payload: { orderType, orderId } })
    },
    * getSelectLinst ({
        payload,
      }, { put, call }) {
      const { data: transTypes } = yield call(enums, 'TransType')
      const { data: WeightUnits } = yield call(enums, 'WeightUnit')
      const { data: VolumeUnits } = yield call(enums, 'VolumeUnit')
      const { data: Roles } = yield call(enums, 'Role')
      const { data: SettlementType } = yield call(enums, 'SettlementType')
      yield put({ type: 'updateState', payload: {
        transTypes, WeightUnits, VolumeUnits, Roles,
      },
      })
    },
    * createOrder ({
        payload,
      }, { put, call, select }) {
      const { consignorEnterprise } = yield (select(_ => _))
      const { orderType } = consignorEnterprise
      const { type, body } = payload
      const res = yield call(createOrder, { body, apiType: orderType })
      if (res.success) {
        if (type === 'goPay') {
          const { orderNo, id } = res.data
          yield put({ type: 'updateState', payload: { orderId: id, orderNo } })
          yield put(routerRedux.push(`/consignorEnterprise/specialLineTransport/confirmOrder?orderType=${orderType}&id=${id}`))
        } else {
          yield put(routerRedux.push('/consignorEnterprise/specialLineTransport/orderList'))
        }
      }
    },
    * addrsModal ({ payload }, { put, call }) {
      yield put({
        type: 'updateState',
        payload: {
          visible: true,
        },
      })
      const res = yield call(addressQuery, payload)
      if (res.success) {
        yield put({ type: 'updateState', payload: { userAddrsList: res.data } })
      }
    },
    * getOrderDetail ({ payload }, { put, call, select }) {
      const { consignorEnterprise } = yield (select(_ => _))
      const { orderType, orderId } = consignorEnterprise
      if (!orderId || !orderType) {
        routerRedux.push('/consignorEnterprise/specialLineTransport/orderList')
        return
      }
      const res = yield call(getOrderDetail, { id: orderId, apiType: orderType })
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            orderDetail: res.data,
          },
        })
      }
    },
    * getPayType ({ payload }, { call, put }) {
      const payTypes = yield call(enums, 'PayType')
      yield put({
        type: 'updateState',
        payload: {
          payType: payTypes.data,
        },
      })
    },
    * getMonthlyPayData ({ payload }, { put, call }) {
      yield put({ type: 'getOrderDetail' })
      yield put({ type: 'getDriverList', payload: {} })
    },
    * getDriverList ({ payload }, { put, call }) {
      const res = yield call(queryUserList, {
        roles: ['EnterpriseDriver', 'PersionDriver'],
        ...payload })
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            DriverList: res.data,
          },
        })
      }
    },
    * orderConfirm ({ payload }, { put, call, select }) {
      const { consignorEnterprise } = yield (select(_ => _))
      const { orderType, orderId } = consignorEnterprise
      const res = yield call(orderConfirm, {
        orderType,
        body: {
          id: orderId,
          settlementType: [],
        },
      })
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            DriverList: res.data,
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
