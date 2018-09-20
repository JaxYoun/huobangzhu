import { getData, addNew, updateNew, updateNewsStatus, getNewsDetail } from '../services/informationManagement'

export default {
  namespace: 'informationManagement',
  state: {
    infoData: [],
    activeKeys: '1',
    picAndArt: {
      fields: [],
      tableTime: 0,
    },
    picAndArtModal: {
      visible: false,
      firmData: {},
      loading: false,
      imgUrlSmall: '',
      introductionData: '',
      text: true,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/webManagement/informationManagement') {
          dispatch({
            type: 'update',
            payload: {
              infoData: [],
              activeKeys: '1',
              picAndArt: {
                fields: [],
                tableTime: 0,
              },
              picAndArtModal: {
                visible: false,
                firmData: {},
                loading: false,
                imgUrlSmall: '',
                introductionData: '',
                text: true,
              },
            },
          })
          dispatch({
            type: 'getinfoData',
            payload: { type: 'NewsType' },
          })
        }
      })
    },
  },
  effects: {
    * update ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    * updatePicAndArt ({ payload }, { put }) {
      yield put({
        type: 'updatePicAndArtStates',
        payload,
      })
    },
    * updatePicAndArtModal ({ payload }, { put }) {
      yield put({
        type: 'updatePicAndArtModalStates',
        payload,
      })
    },
    // 获取信息类型
    * getinfoData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          infoData: res.data,
        },
      })
    },
    // 获取数据
    * getPicAndArtData ({ payload }, { put, call }) {
      const res = yield call(getNewsDetail, payload)
      if (res.code === '200') {
        yield put({
          type: 'updatePicAndArtModal',
          payload: {
            firmData: res.data,
            visible: true,
          },
        })
      }
    },
    // 清空并关闭
    * cleanModal ({ payload }, { put, call }) {
      yield put({
        type: 'updatePicAndArtModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
          imgUrlSmall: '',
          introductionData: '',
          text: true,
        },
      })
    },
    // 新增
    * addNews ({ payload }, { put, call }) {
      const res = yield call(addNew, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanModal' })
        yield put({
          type: 'updatePicAndArt',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      } else {
        yield put({
          type: 'updatePicAndArtModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 修改
    * updateNews ({ payload }, { put, call }) {
      const res = yield call(updateNew, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanModal' })
        yield put({
          type: 'updatePicAndArt',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      } else {
        yield put({
          type: 'updatePicAndArtModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 修改状态
    * updateNewsStatus ({ payload }, { put, call }) {
      const res = yield call(updateNewsStatus, payload)
      if (res.code === '200') {
        yield put({
          type: 'updatePicAndArt',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updatePicAndArtStates (state, { payload }) {
      let { picAndArt } = state
      return { ...state, picAndArt: { ...picAndArt, ...payload } }
    },
    updatePicAndArtModalStates (state, { payload }) {
      let { picAndArtModal } = state
      return { ...state, picAndArtModal: { ...picAndArtModal, ...payload } }
    },
  },
}
