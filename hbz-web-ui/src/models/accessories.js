import {
  queryAddress,
  updateAddress,
  geoConvert,
  createAddress,
  deleteAddress,
  priceCompute,
  exCreate,
} from 'services/accessories'
import { enums } from 'services/app'
// import { routerRedux } from 'dva/router'
import { browserHistory } from 'react-router'

const useinArray = [1, 2]
export default {
  namespace: 'accessories',
  state: {
    isShow: false,
    isShowMore: false,
    visible: false,
    sendAddress: [],
    receiptAddress: [],
    defaultsendAddress: '',
    defaultreceiptAddress: '',
    changeSendAddress: '',
    changeReceiptAddress: '',
    modalType: '',
    formData: '',
    addlng: '',
    addlat: '',
    commodityType: '',
    price: '0.00',
    commodityDetial: [],
    readyToPay: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/consignorEnterprise/accessories') {
          dispatch({ type: 'queryAddress' })
          dispatch({ type: 'queryEnums' })
          dispatch({
            type: 'changeStates',
            payload: {
              price: '0.00',
              commodityDetial: [],
              readyToPay: '',
              defaultsendAddress: '',
              changeSendAddress: '',
              defaultreceiptAddress: '',
              changeReceiptAddress: '',
            },
          })// 清空价格
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
      const { data: commodityType } = yield call(enums, 'CommodityClass')
      yield put({
        type: 'updateState',
        payload: {
          commodityType,
        },
      })
    },
    * queryAddress ({ payload = {} }, { call, put }) {
      for (let item of useinArray) {
        const res = yield call(queryAddress, { usein: item })
        let key = item === 1 ? 'sendAddress' : 'receiptAddress'
        if (res.success) {
          yield put({
            type: 'updateState',
            payload: {
              [key]: res.data,
            },
          })
          yield put({ type: 'checkIsDefault', payload: { data: res.data, now: key } })
        }
      }
    },
    * checkIsDefault ({ payload }, { put }) {
      let { data, now } = payload
      for (let item of data) {
        if (item.idefault === 1) {
          yield put({
            type: 'updateState',
            payload: {
              [`default${now}`]: item,
            },
          })
        }
      }
    },
    * updateAddress ({ payload }, { call, put }) {
      const res = yield call(updateAddress, payload)
      if (res.success) {
        yield put({ type: 'queryAddress' })
        yield put({
          type: 'updateState',
          payload: {
            visible: false,
          },
        })
      }
    },
    * showMoreAddress ({ payload }, { put }) {
      if (payload.types === 'fahuo') {
        yield put({
          type: 'updateState',
          payload: {
            isShow: payload.isShow,
          },
        })
      } else {
        yield put({
          type: 'updateState',
          payload: {
            isShowMore: payload.isShow,
          },
        })
      }
    },
    * showModal ({ payload }, { put }) {
      let { modalType, formData } = payload
      yield put({
        type: 'updateState',
        payload: {
          visible: true,
          modalType,
          formData,
        },
      })
    },
    * hideModal ({ payload }, { put }) {
      yield put({
        type: 'updateState',
        payload,
      })
    },
    * queryLocation ({ payload }, { call, put }) {
      const res = yield call(geoConvert, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            addlng: res.data.lng,
            addlat: res.data.lat,
          },
        })
      }
    },
    * addAddress ({ payload }, { call, put }) {
      const res = yield call(createAddress, payload)
      if (res.success) {
        yield put({ type: 'updateState', payload: { visible: false } })
        yield put({ type: 'queryAddress' })
      }
    },
    * deleteAddress ({ payload }, { call, put }) {
      const res = yield call(deleteAddress, { id: payload.data.id })
      if (res.success) {
        if (payload.data.idefault === 1) {
          console.log('asaa')
          if (payload.now === 'sendAddress') {
            console.log('123123')
            yield put({
              type: 'updateState',
              payload: {
                defaultsendAddress: '',
                changeSendAddress: '',
              },
            })
          } else {
            yield put({
              type: 'updateState',
              payload: {
                defaultreceiptAddress: '',
                changeReceiptAddress: '',
              },
            })
          }
        }
        yield put({ type: 'queryAddress' })
      }
    },
    * countPrice ({ payload }, { call, put }) {
      const res = yield call(priceCompute, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            price: res.data,
            visible: false,
          },
        })
      }
    },
    * exCreate ({ payload }, { call }) {
      const res = yield call(exCreate, payload)
      if (res.success) {
        console.log(res.data)
        browserHistory.push(`/consignorEnterprise/pay?orderId=${res.data.id}`)
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
