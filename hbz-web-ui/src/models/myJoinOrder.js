
export default {
  namespace: 'myJoinOrder',
  state: {
    vehicleList: [],
    fields: {
      takeType: 'TAKEING',
    },
    tableTime: 0,
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/personalCenter/myJoinOrder') {
          dispatch({
            type: 'changeStates',
            payload: {
              fields: {
                takeType: 'TAKEING',
              },
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
