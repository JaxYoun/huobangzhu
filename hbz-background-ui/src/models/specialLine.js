import { Linkage, payOrderFls, hbzFslOrderDetails, teakUserInformation, enums, cehiclecCollections, hbzTakerInfoInformation, details } from '../services/specialLine'

export default {
  namespace: 'specialLine',

  state: {
    fields: [],
    tableTime: 0,
    provinceFromData: [],
    cityFromData: [],
    countyFromData: [],
    provinceToData: [],
    cityToData: [],
    countyToData: [],
    collectionData: {},
    payData: {},
    teakUserData: {},
    recruitData: [],
    collapse: false,
    selectData: {
      SettlementType: {},
      OrderTrans: {},
      OrderType: {},
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

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/orderManagement/specialLine') {
          // 清空参数
          dispatch({
            type: 'update',
            payload: { fields: [],
              tableTime: 0,
              provinceFromData: [],
              cityFromData: [],
              countyFromData: [],
              provinceToData: [],
              cityToData: [],
              countyToData: [],
              collectionData: {},
              payData: {},
              teakUserData: {},
              recruitData: [],
              collapse: false,
              selectData: {
                SettlementType: {},
                OrderTrans: {},
                OrderType: {},
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
          collectionData: {},
          payData: {},
          teakUserData: {},
          recruitData: [],
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
    * updateLogisticsModal ({ payload }, { put }) {
      yield put({
        type: 'updateLogisticsModalStates',
        payload,
      })
    },
      // 获取基础信息
    * getData ({ payload }, { put, call }) {
      const res = yield call(hbzFslOrderDetails, payload)
      // if (res.data === null) {
      //   res.data = { successInfo: true }
      // } else {
      //   res.data.successInfo = true
      // }
      yield put({
        type: 'updateModalStates',
        payload: {
          firmData: res.data,
        },
      })
    },
    * getSelect ({ payload }, { put, call }) {
      // 付款方式
      const SettlementType = yield call(enums, { enumname: 'SettlementType' })
      // 订单状态
      const OrderTrans = yield call(enums, { enumname: 'OrderTrans' })
      // 订单类型
      const OrderType = yield call(enums, { enumname: 'OrderType' })
      yield put({
        type: 'updateSelect',
        payload: {
          SettlementType: SettlementType.data,
          OrderTrans: OrderTrans.data,
          OrderType: OrderType.data,
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
    // 获取接单人信息
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
    // 获取征集条件
    * collectionDataGet ({ payload }, { put, call }) {
      const res = yield call(cehiclecCollections, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      // 司机类型判断
      if (res.data.need) {
        if (res.data.need === 1) {
          res.data.need = '个人司机'
        } else if (res.data.need === 2) {
          res.data.need = '运输企业'
        } else {
          res.data.need = '不限'
        }
      }
      yield put({
        type: 'updateStates',
        payload: {
          collectionData: res.data,
        },
      })
    },
    // 参与征集司机
    * recruitDataGet ({ payload }, { put, call }) {
      const res = yield call(hbzTakerInfoInformation, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      yield put({
        type: 'updateStates',
        payload: {
          recruitData: res.data,
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
          provinceFromData: res.data,
          provinceToData: res.data,
        },
      })
    },
    // 获取市
    * updateFromCity ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          cityFromData: res.data,
        },
      })
    },
    // // 获取直辖市
    // * updateFromCitys ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updateStates',
    //     payload: {
    //       countyFromData: res.data,
    //     },
    //   })
    // },
    * updateFromCounty ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          countyFromData: res.data,
        },
      })
    },
    * updateToCity ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          cityToData: res.data,
        },
      })
    },
    * updateToCounty ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          countyToData: res.data,
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
