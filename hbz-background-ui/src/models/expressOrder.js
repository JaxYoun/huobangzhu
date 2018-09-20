import { Modal } from 'antd'
import { Linkage, payOrderFls, hbzExpressPiecesDetails, enums, details, addHbzExOrder, deleteData, addLogisticsDetail } from '../services/expressOrder'

export default {
  namespace: 'expressOrder',

  state: {
    fields: [],
    tableTime: 0,
    activeKey: '1',
    fieldsHistory: [],
    queryCollapse: false,
    historyCollapse: false,
    // 订单收取城市(快递列表)
    provinceFromData: [],
    cityFromData: [],
    countyFromData: [],
    provinceToData: [],
    cityToData: [],
    countyToData: [],
    // 模态框信息
    commonData: {},
    payData: {},
    modalState: {
      visible: false,
      firmData: {},
    },
    // 物流信息
    logisticsModal: {
      visible: false,
      logisticsData: [],
    },
    // 快递物流详情 可删除
    expressShowModal: {
      visible: false,
      expressShowData: [],
    },
    // 新增快递派件
    addExpressModal: {
      visible: false,
      addData: [],
    },
    // 新增快递记录
    addExpressForEachModal: {
      visible: false,
      firmData: [],
      loading: false,
    },
    // 搜索框
    selectData: {
      OrderTrans: {},
      expressCompanyType: {},
    },
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/orderManagement/expressOrder') {
          // 清空参数
          dispatch({
            type: 'update',
            payload: {
              fields: [],
              tableTime: 0,
              activeKey: '1',
              fieldsHistory: [],
              queryCollapse: false,
              historyCollapse: false,
              // 订单收取城市(快递列表)
              provinceFromData: [],
              cityFromData: [],
              countyFromData: [],
              provinceToData: [],
              cityToData: [],
              countyToData: [],
              // 模态框信息
              commonData: {},
              payData: {},
              modalState: {
                visible: false,
                firmData: {},
              },
              // 物流信息
              logisticsModal: {
                visible: false,
                logisticsData: [],
              },
              // 快递物流详情 可删除
              expressShowModal: {
                visible: false,
                expressShowData: [],
              },
              // 新增快递派件
              addExpressModal: {
                visible: false,
                addData: [],
              },
              // 新增快递记录
              addExpressForEachModal: {
                visible: false,
                firmData: [],
                loading: false,
              },
              // 搜索框
              selectData: {
                OrderTrans: {},
                expressCompanyType: {},
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
          commonData: {},
          payData: {},
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
    * updateAddExpressModal ({ payload }, { put }) {
      yield put({
        type: 'updateAddExpressModalStates',
        payload,
      })
    },
    * updateExpressShowModal ({ payload }, { put }) {
      yield put({
        type: 'updateExpressShowModalStates',
        payload,
      })
    },
    * updateAddExpressForEachModal ({ payload }, { put }) {
      yield put({
        type: 'updateAddExpressForEachModalStates',
        payload,
      })
    },
      // 获取基础信息
    * getData ({ payload }, { put, call }) {
      const res = yield call(hbzExpressPiecesDetails, payload)
      if (res.data === null) {
        res.data = { successInfo: true }
      } else {
        res.data.successInfo = true
      }
      yield put({
        type: 'updateStates',
        payload: {
          commonData: res.data,
        },
      })
    },
    * getSelect ({ payload }, { put, call }) {
      // 快递公司
      const expressCompanyType = yield call(enums, { enumname: 'ExpressCompanyType' })
      // 订单状态
      const OrderTrans = yield call(enums, { enumname: 'OrderTrans' })
      // 订单类型
      const OrderType = yield call(enums, { enumname: 'OrderType' })
      yield put({
        type: 'updateSelect',
        payload: {
          expressCompanyType: expressCompanyType.data,
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
    * showDataGet ({ payload }, { put, call }) {
      const res = yield call(details, payload)
      yield put({
        type: 'updateExpressShowModalStates',
        payload: {
          expressShowData: res.data,
        },
      })
    },
    // 删除物流信息
    * deleteData ({ payload }, { put, call }) {
      const res = yield call(deleteData, payload)
      if (res.code === '200') {
        Modal.success({
          title: '删除成功',
          content: '删除信息成功',
        })
        yield put({
          type: 'updateExpressShowModalStates',
          payload: { visible: false },
        })
      }
    },
    // 新增派件
    * addExpress ({ payload }, { put, call }) {
      const res = yield call(addHbzExOrder, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateAddExpressModalStates',
          payload: { visible: false },
        })
        yield put({
          type: 'getData',
          payload: { id: payload.id },
        })
      }
    },
    // 新增记录
    * addExpressForEach ({ payload }, { put, call }) {
      const res = yield call(addLogisticsDetail, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date().getTime() },
        })
        yield put({
          type: 'updateAddExpressForEachModalStates',
          payload: { visible: false,
            loading: false },
        })
      } else {
        yield put({
          type: 'updateAddExpressForEachModalStates',
          payload: {
            loading: false },
        })
      }
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
    // 获取省市区
    * updateFromCity ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateStates',
        payload: {
          cityFromData: res.data,
        },
      })
    },
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
    updateAddExpressModalStates (state, { payload }) {
      let { addExpressModal } = state
      return { ...state, addExpressModal: { ...addExpressModal, ...payload } }
    },
    updateExpressShowModalStates (state, { payload }) {
      let { expressShowModal } = state
      return { ...state, expressShowModal: { ...expressShowModal, ...payload } }
    },
    updateAddExpressForEachModalStates (state, { payload }) {
      let { addExpressForEachModal } = state
      return { ...state, addExpressForEachModal: { ...addExpressForEachModal, ...payload } }
    },
  },
}
