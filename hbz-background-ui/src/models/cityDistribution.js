import { Linkage,
   payOrderFls,
   teakUserInformation,
   enums,
   details,
   platformDetails,
   helpBuyOrderDetails,
   HelpSendDetails,
   takeDetailsSingle } from '../services/cityDistribution'

export default {
  namespace: 'cityDistribution',

  state: {
    // buy
    fields: [],
    provinceData: [],
    cityData: [],
    countyData: [],
    buyCollapse: false,
    // send
    sendfields: [],
    provinceDataSend: [],
    cityDataSend: [],
    countyDataSend: [],
    sendCollapse: false,
    // 共用
    tableTime: 0,
    activeKey: '1',
    payData: {},
    teakUserData: {},
    platformData: {},
    recruitData: [],
    buyData: {},
    sendData: {},
    selectData: {
      OrderTrans: [],
    },
    logisticsModal: {
      visible: false,
      logisticsData: [],
    },
    modalState: {
      visible: false,
      firmData: {},
    },
    modalStateSend: {
      visible: false,
      firmData: {},
    },
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/orderManagement/cityDistribution') {
          // 清空参数
          dispatch({
            type: 'update',
            payload: {
              // buy
              fields: [],
              provinceData: [],
              cityData: [],
              countyData: [],
              buyCollapse: false,
              // send
              sendfields: [],
              provinceDataSend: [],
              cityDataSend: [],
              countyDataSend: [],
              sendCollapse: false,
              // 共用
              tableTime: 0,
              activeKey: '1',
              payData: {},
              teakUserData: {},
              recruitData: [],
              buyData: {},
              sendData: {},
              selectData: {
                OrderTrans: {},
              },
              logisticsModal: {
                visible: false,
                logisticsData: [],
              },
              modalState: {
                visible: false,
                firmData: {},
              },
            },
          })
          dispatch({
            type: 'updateProvince',
            payload: { parentId: 0 },
          })
          dispatch({
            type: 'getSelect',
          })
        }
      })
    },
  },

  effects: {
    * clean ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload: {
          payData: {},
          teakUserData: {},
          buyData: {},
          sendData: {},
          platformData: {},
        },
      })
      yield put({
        type: 'updateModalStates',
        payload: {
          firmData: {},
        },
      })
    },
    * update ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * updateModal ({ payload }, { put }) {
      yield put({
        type: 'updateModalStates',
        payload,
      })
    },
    * updateModalSend ({ payload }, { put }) {
      yield put({
        type: 'updateModalSendStates',
        payload,
      })
    },
    // 物流详情
    * updateLogisticsModal ({ payload }, { put }) {
      yield put({
        type: 'updateLogisticsModalStates',
        payload,
      })
    },
    * getSelect ({ payload }, { put, call }) {
      // 订单状态
      const OrderTrans = yield call(enums, { enumname: 'OrderTrans' })
      yield put({
        type: 'updateSelect',
        payload: {
          OrderTrans: OrderTrans.data,
        },
      })
    },
    // 获取帮我买基础信息
    * buyDataGet ({ payload }, { put, call }) {
      const res = yield call(helpBuyOrderDetails, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      yield put({
        type: 'updateStates',
        payload: {
          buyData: res.data,
        },
      })
    },
    // 获取帮我送基础信息
    * sendDataGet ({ payload }, { put, call }) {
      const res = yield call(HelpSendDetails, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      yield put({
        type: 'updateStates',
        payload: {
          sendData: res.data,
        },
      })
    },
    // 获取支付详情
    * payDataGet ({ payload }, { put, call }) {
      const res = yield call(payOrderFls, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      yield put({
        type: 'updateStates',
        payload: {
          payData: res.data,
        },
      })
    },
    // 帮我买获取接单人信息
    * teakUserDataGet ({ payload }, { put, call }) {
      const res = yield call(teakUserInformation, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      yield put({
        type: 'updateStates',
        payload: {
          teakUserData: res.data,
        },
      })
    },
    // 帮我买获取接单人信息
    * teakUserSendDataGet ({ payload }, { put, call }) {
      const res = yield call(takeDetailsSingle, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      yield put({
        type: 'updateStates',
        payload: {
          teakUserData: res.data,
        },
      })
    },
    // 获取平台付款详情
    * platformDetails ({ payload }, { put, call }) {
      const res = yield call(platformDetails, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      yield put({
        type: 'updateStates',
        payload: {
          platformData: res.data,
        },
      })
    },
    // 获取物流信息
    * logisticsDataGet ({ payload }, { put, call }) {
      const res = yield call(details, payload)
      yield put({
        type: 'updateLogisticsModalStates',
        payload: {
          logisticsData: res.data,
        },
      })
    },
    // 获取省
    * updateProvince ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          provinceData: res.data,
          provinceDataSend: res.data,
        },
      })
    },
    // 获取市
    * updateFromCity ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          cityData: res.data,
        },
      })
    },
    * updateCitySend ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          cityDataSend: res.data,
        },
      })
    },
    // 获取区
    * updateFromCounty ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          countyData: res.data,
        },
      })
    },
    * updateCountySend ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          countyDataSend: res.data,
        },
      })
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updateModalStates (state, { payload }) {
      let { modalState } = state
      return { ...state, modalState: { ...modalState, ...payload } }
    },
    updateModalSendStates (state, { payload }) {
      let { modalStateSend } = state
      return { ...state, modalStateSend: { ...modalStateSend, ...payload } }
    },
    updateSelect (state, { payload }) {
      let { selectData } = state
      return { ...state, selectData: { ...selectData, ...payload } }
    },
    updateLogisticsModalStates (state, { payload }) {
      let { logisticsModal } = state
      return { ...state, logisticsModal: { ...logisticsModal, ...payload } }
    },
  },
}
