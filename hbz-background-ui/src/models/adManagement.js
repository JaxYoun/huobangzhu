import { getData, getBannerDetail, enableOrDisableBanner, addBanner, updateBanners } from '../services/adManagement'

export default {
  namespace: 'adManagement',
  state: {
    locationData: [],
    skipTypeData: [],
    activeKeys: '1',
    banner: {
      fields: [],
      tableTime: 0,
    },
    bannerModal: {
      visible: false,
      firmData: {},
      loading: false,
      imgUrlSmall: '',
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/webManagement/adManagement') {
          dispatch({
            type: 'update',
            payload: {
              locationData: [],
              skipTypeData: [],
              activeKeys: '1',
              banner: {
                fields: [],
                tableTime: 0,
              },
              bannerModal: {
                visible: false,
                firmData: {},
                loading: false,
                imgUrlSmall: '',
              },
            },
          })
          dispatch({
            type: 'getlocationData',
            payload: { type: 'BannerLocation' },
          })
          dispatch({
            type: 'getskipTypeData',
            payload: { type: 'BannerSkipType' },
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
    * updateBanner ({ payload }, { put }) {
      yield put({
        type: 'updateBannerStates',
        payload,
      })
    },
    * updateBannerModal ({ payload }, { put }) {
      yield put({
        type: 'updateBannerModalStates',
        payload,
      })
    },
    // 获取位置
    * getlocationData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          locationData: res.data,
        },
      })
    },
    // 跳转方式
    * getskipTypeData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          skipTypeData: res.data,
        },
      })
    },
    // 详情查询
    * getBannerData ({ payload }, { put, call }) {
      const res = yield call(getBannerDetail, payload)
      if (res.code === '200') {
        console.log(res.data)
        yield put({
          type: 'updateBannerModal',
          payload: {
            firmData: res.data,
          },
        })
      }
    },
    // 清空并关闭
    * cleanBannerModal ({ payload }, { put, call }) {
      yield put({
        type: 'updateBannerModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
          imgUrlSmall: '',
        },
      })
    },
    // 新增
    * addBanner ({ payload }, { put, call }) {
      const res = yield call(addBanner, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanBannerModal' })
        yield put({
          type: 'updateBanner',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      } else {
        yield put({
          type: 'updateBannerModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 修改
    * updateBanners ({ payload }, { put, call }) {
      const res = yield call(updateBanners, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanBannerModal' })
        yield put({
          type: 'updateBanner',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      } else {
        yield put({
          type: 'updateBannerModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 修改状态
    * updateBannerstatus ({ payload }, { put, call }) {
      const res = yield call(enableOrDisableBanner, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateBanner',
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
    updateBannerStates (state, { payload }) {
      let { banner } = state
      return { ...state, banner: { ...banner, ...payload } }
    },
    updateBannerModalStates (state, { payload }) {
      let { bannerModal } = state
      return { ...state, bannerModal: { ...bannerModal, ...payload } }
    },
  },
}
