import { typeval } from '../services/app'

const typevalObj = {
  msgType: 'SitePushMessageType',
  receiveType: 'SitePushMessageConsumerType',
}

export default {
  namespace: 'sentMsg',
  state: {
    fields: '',
    tableTime: '',
    visible: false,
    msgType: [],
    receiveType: [],
    imgUrlSmall: '',
    introductionData: '',
    textShow: true,
    sendMsgDetali: '',
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/msgManage/sentMsg') {
          dispatch({ type: 'getTypeval' })
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
