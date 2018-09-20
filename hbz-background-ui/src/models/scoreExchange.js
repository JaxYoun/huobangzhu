import { allTree, queryBrand, modify, destroy, trans, getData, querytrans, create, updateRecommonGoods } from '../services/scoreExchange'

export default {
  namespace: 'scoreExchange',
  state: {
    activeKeys: '1',
    wareTypeData: [],
    transData: [],
    upShelf: {
      fields: [],
      tableTime: 0,
    },
    upShelfModal: {
      visible: false,
      firmData: {},
      brandData: [],
      introductionData: '',
      imgUrlBig: '',
      imgUrlSmall: '',
      text: true,
      loading: false,
    },
    exchange: {
      fields: [],
      tableTime: 0,
    },
    exchangeModal: {
      visible: false,
      firmData: {},
      brandData: [],
      sendDatas: [],
    },
    deliverModal: {
      visible: false,
      firmData: {},
      brandData: [],
      loading: false,
    },
    recommon: {
      fields: [],
      tableTime: 0,
    },
    recommonModal: {
      visible: false,
      firmData: {},
      loading: false,
      imgUrl: '',
    },
    recommonModalUpdate: {
      visible: false,
      firmData: {},
      loading: false,
      imgUrl: '',
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/integralShop/scoreExchange') {
          dispatch({ type: 'update', payload: {
            activeKeys: '1',
            wareTypeData: [],
            transData: [],
            upShelf: {
              fields: [],
              tableTime: 0,
            },
            upShelfModal: {
              visible: false,
              firmData: {},
              brandData: [],
              introductionData: '',
              imgUrlBig: '',
              imgUrlSmall: '',
              text: true,
              loading: false,
            },
            exchange: {
              fields: [],
              tableTime: 0,
            },
            exchangeModal: {
              visible: false,
              firmData: {},
              brandData: [],
              sendDatas: [],
            },
            deliverModal: {
              visible: false,
              firmData: {},
              brandData: [],
              loading: false,
            },
            recommon: {
              fields: [],
              tableTime: 0,
            },
            recommonModal: {
              visible: false,
              firmData: {},
              loading: false,
              imgUrl: '',
            },
            recommonModalUpdate: {
              visible: false,
              firmData: {},
              loading: false,
              imgUrl: '',
            },
          } })
          dispatch({ type: 'getTreeData', payload: {} })
          dispatch({ type: 'getBrandData', payload: {} })
          dispatch({ type: 'getTransData', payload: { type: 'expressCompanyType' } })
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
    * updateupShelf ({ payload }, { put }) {
      yield put({
        type: 'updateupShelfStates',
        payload,
      })
    },
    * updateExchange ({ payload }, { put }) {
      yield put({
        type: 'updateExchangeStates',
        payload,
      })
    },
    * updateRecommon ({ payload }, { put }) {
      yield put({
        type: 'updateRecommonStates',
        payload,
      })
    },
    * updateupShelfModal ({ payload }, { put }) {
      yield put({
        type: 'updateupShelfModalStates',
        payload,
      })
    },
    * updateExchangeModal ({ payload }, { put }) {
      yield put({
        type: 'updateExchangeModalStates',
        payload,
      })
    },
    * updateDeliverModal ({ payload }, { put }) {
      yield put({
        type: 'updateDeliverModalStates',
        payload,
      })
    },
    * updateRecommonModal ({ payload }, { put }) {
      yield put({
        type: 'updateRecommonModalStates',
        payload,
      })
    },
    * updateRecommonModalUpdate ({ payload }, { put }) {
      yield put({
        type: 'updateRecommonModalUpdateStates',
        payload,
      })
    },
    // 获取快递类型
    * getTransData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      if (res.code === '200') {
        yield put({
          type: 'update',
          payload: { transData: res.data },
        })
      }
    },
    // 获取下拉树
    * getTreeData ({ payload }, { put, call }) {
      const res = yield call(allTree, payload)
      if (res.code === '200') {
        yield put({
          type: 'update',
          payload: { wareTypeData: res.data },
        })
      }
    },
    // 获取品牌
    * getBrandData ({ payload }, { put, call }) {
      const res = yield call(queryBrand, payload)
      yield put({
        type: 'updateupShelfModal',
        payload: {
          brandData: res.data,
        },
      })
      yield put({
        type: 'updateExchangeModal',
        payload: {
          brandData: res.data,
        },
      })
      yield put({
        type: 'updateDeliverModal',
        payload: {
          brandData: res.data,
        },
      })
    },
    // 清空下架编辑模态框
    * cleanupShelfModal ({ payload }, { put }) {
      yield put({
        type: 'updateupShelfModal',
        payload: {
          visible: false,
          firmData: {},
          introductionData: '',
          imgUrlBig: '',
          imgUrlSmall: '',
          loading: false,
        },
      })
    },
    // 下架编辑
    * shelfUpGoods ({ payload }, { put, call }) {
      const res = yield call(modify, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanupShelfModal',
        })
        yield put({
          type: 'updateupShelf',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateupShelfModal',
          payload: { loading: false },
        })
      }
    },
    // 商品下架
    * shelfOffGoods ({ payload }, { put, call }) {
      const res = yield call(destroy, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateupShelf',
          payload: { tableTime: new Date().getTime() },
        })
      }
    },
    // 发货
    * transGoods ({ payload }, { put, call }) {
      const res = yield call(trans, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateExchange',
          payload: { tableTime: new Date().getTime() },
        })
        yield put({
          type: 'updateDeliverModal',
          payload: {
            visible: false,
            firmData: {},
            loading: false,
          },
        })
      } else {
        yield put({
          type: 'updateDeliverModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 清空发货详情模态框
    * cleanupExchangeModal ({ payload }, { put }) {
      yield put({
        type: 'updateExchangeModal',
        payload: {
          visible: false,
          firmData: {},
        },
      })
    },
    * cleanDeliverModal ({ payload }, { put }) {
      yield put({
        type: 'updateDeliverModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
        },
      })
    },
    // 商品发货查询
    * getSend ({ payload }, { put, call }) {
      const res = yield call(querytrans, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateExchangeModal',
          payload: { sendDatas: res.data[0] },
        })
      }
    },
    // 商品推荐
    * createRecommon ({ payload }, { put, call }) {
      const res = yield call(create, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanupRecommonModal',
          payload,
        })
        yield put({
          type: 'updateupShelf',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateRecommonModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 清空推荐模态框
    * cleanupRecommonModal ({ payload }, { put }) {
      yield put({
        type: 'updateRecommonModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
          imgUrl: '',
        },
      })
    },
    // 更新推荐
    * updateRecommons ({ payload }, { put, call }) {
      const res = yield call(updateRecommonGoods, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanupRecommonModalUpdate',
          payload,
        })
        yield put({
          type: 'updateRecommon',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateRecommonModalUpdate',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 清空推荐修改模态框
    * cleanupRecommonModalUpdate ({ payload }, { put }) {
      yield put({
        type: 'updateRecommonModalUpdate',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
          imgUrl: '',
        },
      })
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updateupShelfStates (state, { payload }) {
      let { upShelf } = state
      return { ...state, upShelf: { ...upShelf, ...payload } }
    },
    updateExchangeStates (state, { payload }) {
      let { exchange } = state
      return { ...state, exchange: { ...exchange, ...payload } }
    },
    updateRecommonStates (state, { payload }) {
      let { recommon } = state
      return { ...state, recommon: { ...recommon, ...payload } }
    },
    updateupShelfModalStates (state, { payload }) {
      let { upShelfModal } = state
      return { ...state, upShelfModal: { ...upShelfModal, ...payload } }
    },
    updateExchangeModalStates (state, { payload }) {
      let { exchangeModal } = state
      return { ...state, exchangeModal: { ...exchangeModal, ...payload } }
    },
    updateDeliverModalStates (state, { payload }) {
      let { deliverModal } = state
      return { ...state, deliverModal: { ...deliverModal, ...payload } }
    },
    updateRecommonModalStates (state, { payload }) {
      let { recommonModal } = state
      return { ...state, recommonModal: { ...recommonModal, ...payload } }
    },
    updateRecommonModalUpdateStates (state, { payload }) {
      let { recommonModalUpdate } = state
      return { ...state, recommonModalUpdate: { ...recommonModalUpdate, ...payload } }
    },
  },
}
