import { routerRedux } from 'dva/router'
import { enums, queryUserList } from 'services/app'
import { message } from 'antd'
import { createOrder, addressQuery, getOrderDetail, orderConfirm, countPrice } from 'services/consignorEnterprise'

const urlToPath = (pathname, { orderType, orderId }) => {
  return `${pathname}?orderType=${orderType}&orderId=${orderId}`
}
export default {
  namespace: 'specialLineTransport',
  state: {
    orderType: null,
    visible: false,
    confirmLoading: false,
    transTypes: {},
    userAddrsList: [], // 用户常用信息
    orderDetail: {},
    CommodityTypes: {}, // 货物类型
    orderId: null, // 选择的订单id
    orderNo: '',
    aliPay: '',
    weChat: '',
    union: '',
    payType: '',
    WeightUnits: [],  // 重量单位
    VolumeUnits: [],   // 体积单位
    Roles: [], // 角色
    DriverList: [],
    takeUserId: '',
    SettlementType: '', // 支付类型
    SettlementTypeObj: {}, // 支付方式合集  ONLINE_PAYMENT: "在线支付", MONTHLY_SETTLEMENT: "月结", LEVY_ONLINE_PAYMENT: "车辆征集"
    showCountPrice: true, // 是否显示平台计算的费用模块
    priceData: '',
    publishUnitPrice: '',
    baseUploading: false,
    baseFileList: [],
    saveLoading: false,
    goPayLoading: false,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((match) => {
        const { query } = match
        dispatch({ type: 'setParams', payload: query })
        setTimeout(() => {
          dispatch({ type: 'initailPage' })
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
    * initailPage ({
        payload,
      }, { put }) {
      yield put({ type: 'getSelectLinst' })
      if (location.pathname.includes('confirmOrder')) {
        yield put({ type: 'getOrderDetail' })
      }
      if (location.pathname.includes('onlinePay')) {
        yield put({ type: 'getPayType' })
      }
      if (location.pathname.includes('selectedMonthlyPay')) {
        yield put({ type: 'getMonthlyPayData' })
      }
      if (location.pathname.includes('LtlOrder')) {
        yield put({ type: 'changeStates', payload: { orderType: 'LtlOrder', priceData: '', publishUnitPrice: '' } })
      }
      if (location.pathname.includes('fslOrder')) {
        yield put({ type: 'changeStates', payload: { orderType: 'fslOrder', priceData: '', publishUnitPrice: '' } })
      }
      if (location.pathname.includes('importFsl')) {
        yield put({ type: 'changeStates', payload: { orderType: 'fslOrder', baseFileList: [] } })
      }
      if (location.pathname.includes('importLtl')) {
        yield put({ type: 'changeStates', payload: { orderType: 'LtlOrder', baseFileList: [] } })
      }
    },
    * setParams ({
        payload,
      }, { put, select }) {
      const { specialLineTransport } = yield (select(_ => _))
      const orderType = specialLineTransport.orderType || payload.orderType || ''
      const orderId = specialLineTransport.orderId || payload.orderId || ''
      yield put({ type: 'updateState', payload: { orderType, orderId } })
    },
    * getSelectLinst ({
        payload,
      }, { put, call }) {
      const { data: transTypes } = yield call(enums, 'TransType')
      const { data: WeightUnits } = yield call(enums, 'WeightUnit')
      const { data: VolumeUnits } = yield call(enums, 'VolumeUnit')
      const { data: Roles } = yield call(enums, 'Role')
      const { data: CommodityTypes } = yield call(enums, 'CommodityType')
      yield put({ type: 'updateState', payload: {
        transTypes, WeightUnits, VolumeUnits, Roles, CommodityTypes,
      },
      })
    },
    * createOrder ({
        payload,
      }, { put, call, select }) {
      const { specialLineTransport } = yield (select(_ => _))
      const { orderType } = specialLineTransport
      const { type, body } = payload
      const res = yield call(createOrder, { body, apiType: orderType })
      if (res.success) {
        yield put({
          type: 'changeStates',
          payload: {
            [`${type}Loading`]: false,
          },
        })
        if (type === 'goPay') {
          const { orderNo, id: orderId } = res.data
          yield put({ type: 'updateState', payload: { orderId, orderNo } })
          yield put(routerRedux.push(urlToPath('/consignorEnterprise/specialLineTransport/confirmOrder', { orderType, orderId })))
        } else {
          yield put(routerRedux.push('/personalCenter/sendOrderList'))
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
      const { specialLineTransport } = yield (select(_ => _))
      const { orderType, orderId } = specialLineTransport
      if (!orderId || !orderType) {
        routerRedux.push('/consignorEnterprise/specialLineTransport/orderList')
        return
      }

      const res = yield call(getOrderDetail, { id: orderId, apiType: orderType })
      const { data: SettlementTypeObj } = yield call(enums, 'SettlementType')
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            orderDetail: res.data,
            SettlementTypeObj,
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
    // * orderConfirm ({ payload }, { put, call, select }) {
    //   const { specialLineTransport } = yield (select(_ => _))
    //   const { orderType, orderId, settlementType } = specialLineTransport
    //   const res = yield call(orderConfirm, {
    //     orderType,
    //     body: {
    //       id: orderId,
    //       settlementType,
    //     },
    //   })
    //   if (res.success) {
    //     yield put({
    //       type: 'updateState',
    //       payload: {
    //         DriverList: res.data,
    //       },
    //     })
    //   }
    // },
    * selectedTlementType ({ payload }, { put, call, select }) {
      const { specialLineTransport } = yield (select(_ => _))
      const { orderType, orderId, SettlementType } = specialLineTransport
      let pathname = ''
      switch (SettlementType) {
        case 'ONLINE_PAYMENT':
          pathname = '/consignorEnterprise/specialLineTransport/onlinePay'
          break
        case 'MONTHLY_SETTLEMENT':
          pathname = '/consignorEnterprise/specialLineTransport/selectedMonthlyPay'
          break
        case 'LEVY_ONLINE_PAYMENT':
          pathname = '/consignorEnterprise/carCollect/carCollectTodo'
          break
        default:
          message.warn('请选择结算方式')
      }
      if (SettlementType === 'ONLINE_PAYMENT' || SettlementType === 'LEVY_ONLINE_PAYMENT') {
        const res = yield call(orderConfirm, { fields: { id: orderId, settlementType: SettlementType }, apiType: orderType })
        if (res.success) {
          yield put(routerRedux.push(urlToPath(pathname, { orderType, orderId })))
        }
      } else {
        yield put(routerRedux.push(urlToPath(pathname, { orderType, orderId })))
      }
      // console.log(SettlementType, pathname, specialLineTransport)
    },
    * monthPayConfirm ({ payload }, { call, put, select }) {
      const { specialLineTransport } = yield (select(_ => _))
      const { orderType, orderId, SettlementType } = specialLineTransport
      const pathname = '/consignorEnterprise/pay'
      const res = yield call(orderConfirm, { fields: { ...payload, id: orderId, settlementType: SettlementType }, apiType: orderType })
      if (res.success) {
        yield put(routerRedux.push(urlToPath(pathname, { orderType, orderId })))
      }
    },
    // 计算平台参考价格
    * countPrice ({ payload }, { call, put, select }) {
      const { specialLineTransport } = yield (select(_ => _))
      const { orderType } = specialLineTransport
      const res = yield call(countPrice, { fields: payload, apiType: orderType })
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            priceData: res.data,
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
