import { deleteClass, allTree, newClass, Linkage, queryBrand, changeClass,
  newGoods, changeGooods, deleteGoods, newBrand, deleteBrand, destroy, updateBrand,
  upGoods } from '../services/basicData'

export default {
  namespace: 'basicData',

  state: {
    activeKeys: '1',
    wareTypeData: [],
    classification: {
      fields: [],
      tableTime: 0,
    },
    classificationModal: {
      visible: false,
      firmData: {},
      imgUrl: '',
      goodslevel: '1',
      loading: false,
    },
    brand: {
      fields: [],
      tableTime: 0,
    },
    brandModal: {
      visible: false,
      firmData: {},
      provinceData: [],
      cityData: [],
      countyData: [],
      provinceModalData: [],
      cityModalData: [],
      countyModalData: [],
      imgUrl: '',
      loading: false,
    },
    goods: {
      fields: [],
      tableTime: 0,
    },
    goodsModal: {
      visible: false,
      firmData: {},
      brandData: [],
      introductionData: '',
      imgUrlBig: '',
      imgUrlSmall: '',
      text: true,
      loading: false,
    },
    goodsShelvesModal: {
      visible: false,
      firmData: {},
      brandData: [],
      introductionData: '',
      imgUrlBig: '',
      imgUrlSmall: '',
      text: true,
      loading: false,
    },
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/integralShop/basicData') {
          dispatch({
            type: 'update',
            payload: {
              activeKeys: '1',
              wareTypeData: [],
              classification: {
                fields: [],
                tableTime: 0,
              },
              classificationModal: {
                visible: false,
                firmData: {},
                imgUrl: '',
                goodslevel: '1',
                loading: false,
              },
              brand: {
                fields: [],
                tableTime: 0,
              },
              brandModal: {
                visible: false,
                firmData: {},
                provinceData: [],
                cityData: [],
                countyData: [],
                provinceModalData: [],
                cityModalData: [],
                countyModalData: [],
                imgUrl: '',
                loading: false,
              },
              goods: {
                fields: [],
                tableTime: 0,
              },
              goodsModal: {
                visible: false,
                firmData: {},
                brandData: [],
                introductionData: '',
                imgUrlBig: '',
                imgUrlSmall: '',
                text: true,
                loading: false,
              },
              goodsShelvesModal: {
                visible: false,
                firmData: {},
                brandData: [],
                introductionData: '',
                imgUrlBig: '',
                imgUrlSmall: '',
                loading: false,
              },
            },
          })
          dispatch({
            type: 'updateProvince',
            payload: { parentId: 0 },
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
    * updateClassification ({ payload }, { put }) {
      yield put({
        type: 'updateClassificationStates',
        payload,
      })
    },
    * updateClassificationModal ({ payload }, { put }) {
      yield put({
        type: 'updateClassificationModalStates',
        payload,
      })
    },
    * updateBrand ({ payload }, { put }) {
      yield put({
        type: 'updateBrandStates',
        payload,
      })
    },
    * updateBrandModal ({ payload }, { put }) {
      yield put({
        type: 'updateBrandModalStates',
        payload,
      })
    },
    * updateGoods ({ payload }, { put }) {
      yield put({
        type: 'updateGoodsStates',
        payload,
      })
    },
    * updateGoodsModal ({ payload }, { put }) {
      yield put({
        type: 'updateGoodsModalStates',
        payload,
      })
    },
    * updateGoodsShelvesModal ({ payload }, { put }) {
      yield put({
        type: 'updateGoodsShelvesModalStates',
        payload,
      })
    },
    // 删除分类
    * deleteClass ({ payload }, { put, call }) {
      const res = yield call(deleteClass, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateClassification',
          payload: { tableTime: new Date().getTime() },
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
    // 新增分类
    * addClass ({ payload }, { put, call }) {
      const res = yield call(newClass, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanClassificationModal',
        })
        yield put({
          type: 'updateClassification',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateClassificationModal',
          payload: { loading: false },
        })
      }
    },
    // 修改分类
    * updateClass ({ payload }, { put, call }) {
      const res = yield call(changeClass, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanClassificationModal',
        })
        yield put({
          type: 'updateClassification',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateClassificationModal',
          payload: { loading: false },
        })
      }
    },
    // 新增模态框关闭清空
    * cleanClassificationModal ({ payload }, { put }) {
      yield put({
        type: 'updateClassificationModalStates',
        payload: {
          visible: false,
          firmData: {},
          imgUrl: '',
          goodslevel: '',
          loading: false,
        },
      })
      yield put({
        type: 'updateStates',
        payload: {
          wareTypeData: [],
        },
      })
    },
    // 清空商品模态框清空
    * cleanGoodsModal ({ payload }, { put }) {
      yield put({
        type: 'updateGoodsModal',
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
    // 清空品牌模态框
    * cleanBrandModal ({ payload }, { put }) {
      yield put({
        type: 'updateBrandModal',
        payload: {
          visible: false,
          firmData: {},
          provinceModalData: [],
          cityModalData: [],
          countyModalData: [],
          imgUrl: '',
          loading: false,
        },
      })
    },
    // 清空上架模态框
    * cleanGoodsShelvesModal ({ payload }, { put }) {
      yield put({
        type: 'updateGoodsShelvesModal',
        payload: {
          visible: false,
          firmData: {},
          brandData: [],
          introductionData: '',
          imgUrlBig: '',
          imgUrlSmall: '',
          loading: false,
        },
      })
    },
    // 删除品牌
    * deleteBrand ({ payload }, { put, call }) {
      const res = yield call(deleteBrand, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateBrand',
          payload: { tableTime: new Date().getTime() },
        })
      }
    },
    // 新增品牌
    * addBrand ({ payload }, { put, call }) {
      const res = yield call(newBrand, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanBrandModal',
        })
        yield put({
          type: 'updateBrand',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateBrandModal',
          payload: { loading: false },
        })
      }
    },
    // 修改品牌
    * updateBrands ({ payload }, { put, call }) {
      const res = yield call(updateBrand, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanBrandModal',
        })
        yield put({
          type: 'updateBrand',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateBrandModal',
          payload: { loading: false },
        })
      }
    },
    // 新增商品
    * addGoods ({ payload }, { put, call }) {
      const res = yield call(newGoods, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanGoodsModal',
        })
        yield put({
          type: 'updateGoods',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateGoodsModal',
          payload: { loading: false },
        })
      }
    },
    // 修改商品
    * updeteGoods ({ payload }, { put, call }) {
      const res = yield call(changeGooods, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanGoodsModal',
        })
        yield put({
          type: 'updateGoods',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateGoodsModal',
          payload: { loading: false },
        })
      }
    },
    // 删除商品
    * deleteGoods ({ payload }, { put, call }) {
      const res = yield call(deleteGoods, payload)
      if (res.code === '200') {
        yield put({
          type: 'updateGoods',
          payload: { tableTime: new Date().getTime() },
        })
      }
    },
    // 商品上架
    * shelfUpGoods ({ payload }, { put, call }) {
      const res = yield call(upGoods, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanGoodsShelvesModal',
        })
        yield put({
          type: 'updateGoods',
          payload: { tableTime: new Date().getTime() },
        })
      } else {
        yield put({
          type: 'updateGoodsShelvesModal',
          payload: { loading: false },
        })
      }
    },
    // 获取省
    * updateProvince ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateBrandModal',
        payload: {
          provinceData: res.data,
        },
      })
    },
    * updateModalProvince ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateBrandModal',
        payload: {
          provinceModalData: res.data,
        },
      })
    },
    // 获取市
    * updateFromCity ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateBrandModal',
        payload: {
          cityData: res.data,
        },
      })
    },
    * updateModalFromCity ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateBrandModal',
        payload: {
          cityModalData: res.data,
        },
      })
    },
    // 获取区
    * updateModalFromCounty ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateBrandModal',
        payload: {
          countyModalData: res.data,
        },
      })
    },
    * updateFromCounty ({ payload }, { put, call }) {
      const res = yield call(Linkage, payload)
      yield put({
        type: 'updateBrandModal',
        payload: {
          countyData: res.data,
        },
      })
    },
    // 获取品牌
    * getBrandData ({ payload }, { put, call }) {
      const res = yield call(queryBrand, payload)
      yield put({
        type: 'updateGoodsModal',
        payload: {
          brandData: res.data,
        },
      })
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updateClassificationStates (state, { payload }) {
      let { classification } = state
      return { ...state, classification: { ...classification, ...payload } }
    },
    updateClassificationModalStates (state, { payload }) {
      let { classificationModal } = state
      return { ...state, classificationModal: { ...classificationModal, ...payload } }
    },
    updateBrandStates (state, { payload }) {
      let { brand } = state
      return { ...state, brand: { ...brand, ...payload } }
    },
    updateBrandModalStates (state, { payload }) {
      let { brandModal } = state
      return { ...state, brandModal: { ...brandModal, ...payload } }
    },
    updateGoodsStates (state, { payload }) {
      let { goods } = state
      return { ...state, goods: { ...goods, ...payload } }
    },
    updateGoodsModalStates (state, { payload }) {
      let { goodsModal } = state
      return { ...state, goodsModal: { ...goodsModal, ...payload } }
    },
    updateGoodsShelvesModalStates (state, { payload }) {
      let { goodsShelvesModal } = state
      return { ...state, goodsShelvesModal: { ...goodsShelvesModal, ...payload } }
    },
  },
}
