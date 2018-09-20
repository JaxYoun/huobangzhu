import { enums } from 'services/app'
import { getDetail } from 'services/driverHelpToBuy'
import { tenderGet, consignorGet } from 'services/carCollectFinish'
import { takerCreate } from 'services/vehicleCollect'
import { getOrderId } from 'utils'
import { browserHistory } from 'react-router'

export default {
  namespace: 'checkVehicleDetail',
  state: {
    id: '',
    vehicleDetailData: '',
    transTypes: [],
    collectionData: '',
    driverList: [],
    showTakeInBtn: true,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/driverEnterprise/vehicleCollect/checkVehicleDetail') {
          const orderIds = getOrderId(location, 'orderId')
          const type = getOrderId(location, 'orderType')
          console.log('type', type)
          if (type === 'joinOrder') {
            console.log(type)
            dispatch({
              type: 'changeStates',
              payload: {
                showTakeInBtn: '',
              },
            })
          } else {
            dispatch({
              type: 'changeStates',
              payload: {
                showTakeInBtn: true,
              },
            })
          }
          dispatch({ type: 'queryEnums' })
          dispatch({
            type: 'changeStates',
            payload: { id: orderIds, driverList: [], collectionData: '', vehicleDetailData: '' },
          })
          dispatch({
            type: 'queryVehicleDetail',
            payload: {
              id: orderIds,
            },
          })
          dispatch({
            type: 'getTender', // 车辆征集单查看条件
            payload: {
              orderId: orderIds,
            },
          })
          dispatch({
            type: 'getConsignor', // 车辆征集列表
            payload: {
              orderId: orderIds,
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
    * queryEnums ({ payload }, { call, put }) {
      const { data: transTypes } = yield call(enums, 'TransType')
      yield put({
        type: 'updateState',
        payload: {
          transTypes,
        },
      })
    },
    * queryVehicleDetail ({ payload }, { call, put }) {
      const res = yield call(getDetail, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            vehicleDetailData: res.data,
          },
        })
      }
    },
    * getTender ({ payload }, { call, put }) {
      const res = yield call(tenderGet, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            collectionData: res.data,
          },
        })
      }
    },
    * getConsignor ({ payload }, { call, put }) {
      const res = yield call(consignorGet, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            driverList: res.data,
          },
        })
      }
    },
    * takeInCollect ({ payload }, { call }) {
      const res = yield call(takerCreate, payload)
      if (res.success) {
        browserHistory.push('/driverEnterprise/vehicleCollect')
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
