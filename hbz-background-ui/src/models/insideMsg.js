import { typeval } from '../services/app'
import { send, save, update } from '../services/insideMsg'

const typevalObj = {
  msgType: 'SitePushMessageType',
  platType: 'SitePushReceivePlatformType',
  sendType: 'SitePushType',
  receiveType: 'SitePushMessageConsumerType',
}

export default {
  namespace: 'insideMsg',
  state: {
    fields: '',
    tableTime: 0,
    msgType: [],
    platType: [],
    sendType: [],
    receiveType: [],
    insideMsgDetail: '',
    visible: false,
    imgUrlSmall: '',
    introductionData: '',
    textShow: true,
    isPhoneNo: false,
    isSendTime: false,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/msgManage/insideMsg') {
          dispatch({
            type: 'getTypeval',
          })
          dispatch({
            type: 'changeStates',
            payload: {
              fields: '',
              imgUrlSmall: '',
              introductionData: '',
              textShow: true,
              isPhoneNo: false,
              isSendTime: false,
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
              },
            })
          }
        }
      }
    },
    * sendMsg ({ payload }, { call, put }) {
      const res = yield call(send, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
    },
    * saveMsg ({ payload }, { call, put }) {
      const res = yield call(payload.id ? update : save, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            visible: false,
            imgUrlSmall: '',
            introductionData: '',
            textShow: true,
            isPhoneNo: true,
            isSendTime: true,
            tableTime: new Date().getTime(),
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
