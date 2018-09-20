import { getData, Linkage, getDatas, deleteMessage, addMessage, updateMessage } from '../services/receiptManagement'

export default {
  namespace: 'receiptManagement',
  state: {
    fields: [],
    tableTime: 0,
    payData: [],
    collapse: false,
    provinceFromData: [],
    cityFromData: [],
    countyFromData: [],
    provinceToData: [],
    cityToData: [],
    countyToData: [],
    firmModal: {
      visible: false,
      firmData: {},
      loading: false,
      packageData: [],
      statusData: [],
      serviceData: [],
      paymentMethodData: [],
      paymentMethodValue: '',
      totalCost: 0,
      provinceFromData: [],
      cityFromData: [],
      countyFromData: [],
      provinceToData: [],
      cityToData: [],
      countyToData: [],
      ratioData: [],
    },
    goodsModal: {
      visible: false,
      firmData: {},
      loading: false,
      tableTime: 0,
      fields: [],
    },
    userModal: {
      visible: false,
      firmData: {},
      loading: false,
      tableTime: 0,
      fields: [],
      type: '',
    },
    goodsMessage: {},
    getuserMessage: {},
    senduserMessage: {},
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/logistics/receiptManagement') {
          dispatch({
            type: 'update',
            payload: {
              fields: [],
              tableTime: 0,
              payData: [],
              collapse: false,
              provinceFromData: [],
              cityFromData: [],
              countyFromData: [],
              provinceToData: [],
              cityToData: [],
              countyToData: [],
              firmModal: {
                visible: false,
                firmData: {},
                loading: false,
                packageData: [],
                statusData: [],
                paymentMethodData: [],
                serviceData: [],
                paymentMethodValue: '',
                totalCost: 0,
                provinceFromData: [],
                cityFromData: [],
                countyFromData: [],
                provinceToData: [],
                cityToData: [],
                countyToData: [],
                ratioData: [],
              },
              goodsModal: {
                visible: false,
                firmData: {},
                loading: false,
                tableTime: 0,
                fields: [],
              },
              userModal: {
                visible: false,
                firmData: {},
                loading: false,
                tableTime: 0,
                fields: [],
                type: '',
              },
              goodsMessage: {},
              getuserMessage: {},
              senduserMessage: {},
            },
          })
          dispatch({
            type: 'getPayData',
            payload: { type: 'PaymentMethod' },
          })
          dispatch({
            type: 'getPackageData',
            payload: { type: 'PackageUnit' },
          })
          dispatch({
            type: 'getStatusData',
            payload: { type: 'PackagingStatus' },
          })
          dispatch({
            type: 'getPaymentMethodData',
            payload: { type: 'PaymentMethod' },
          })
          dispatch({
            type: 'getsratioData',
            payload: { type: 'RatioValue' },
          })
          dispatch({
            type: 'getsServiceMethodTypeData',
            payload: { enumname: 'ServiceMethodType' },
          })
          // dispatch({
          //   type: 'updateProvince',
          //   payload: { parentId: 0 },
          // })
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
    * updatefirmModal ({ payload }, { put }) {
      yield put({
        type: 'updatefirmModalStates',
        payload,
      })
    },
    * updategoodsModal ({ payload }, { put }) {
      yield put({
        type: 'updategoodsModalStates',
        payload,
      })
    },
    * updateuserModal ({ payload }, { put }) {
      yield put({
        type: 'updateuserModalStates',
        payload,
      })
    },
    // 获取付款方式
    * getPayData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          payData: res.data,
        },
      })
    },
    // 获取包装单位
    * getPackageData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'updatefirmModal',
        payload: {
          packageData: res.data,
        },
      })
    },
    // 获取包装状态
    * getStatusData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'updatefirmModal',
        payload: {
          statusData: res.data,
        },
      })
    },
    // 获取付款方式
    * getPaymentMethodData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'updatefirmModal',
        payload: {
          paymentMethodData: res.data,
        },
      })
    },
    // 获取服务方式
    * getsServiceMethodTypeData ({ payload }, { put, call }) {
      const res = yield call(getDatas, payload)
      yield put({
        type: 'updatefirmModal',
        payload: {
          serviceData: res.data,
        },
      })
    },
    // 获取保率
    * getsratioData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'updatefirmModal',
        payload: {
          ratioData: res.data,
        },
      })
    },
    // 删除信息
    * deleteMessages ({ payload }, { put, call }) {
      const res = yield call(deleteMessage, payload)
      if (res.code === '200') {
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
    },
    // 清空并关闭
    * cleanModal ({ payload }, { put, call }) {
      yield put({
        type: 'updatefirmModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
          totalCost: 0,
          cityFromData: [],
          countyFromData: [],
          cityToData: [],
          countyToData: [],
        },
      })
      yield put({
        type: 'update',
        payload: {
          goodsMessage: {},
          senduserMessage: {},
          getuserMessage: {},
        },
      })
    },
    * cleangoodsModal ({ payload }, { put, call }) {
      yield put({
        type: 'updategoodsModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
          tableTime: 0,
          fields: [],
        },
      })
    },
    * cleanuserModal ({ payload }, { put, call }) {
      yield put({
        type: 'updateuserModal',
        payload: {
          visible: false,
          firmData: {},
          loading: false,
          tableTime: 0,
          fields: [],
          type: '',
        },
      })
    },
    // 新增车辆
    * addMessage ({ payload }, { put, call }) {
      const res = yield call(addMessage, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanModal' })
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      } else {
        yield put({
          type: 'updatefirmModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // 修改车辆
    * updateMessage ({ payload }, { put, call }) {
      const res = yield call(updateMessage, payload)
      if (res.code === '200') {
        yield put({ type: 'cleanModal' })
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      } else {
        yield put({
          type: 'updatefirmModal',
          payload: {
            loading: false,
          },
        })
      }
    },
    // // 获取省
    // * updateProvince ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updateStates',
    //     payload: {
    //       provinceFromData: res.data,
    //       provinceToData: res.data,
    //     },
    //   })
    //   yield put({
    //     type: 'updatefirmModalStates',
    //     payload: {
    //       provinceFromData: res.data,
    //       provinceToData: res.data,
    //     },
    //   })
    // },
    // // 获取市
    // * updateFromCity ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updateStates',
    //     payload: {
    //       cityFromData: res.data,
    //     },
    //   })
    // },
    // * updateFromCounty ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updateStates',
    //     payload: {
    //       countyFromData: res.data,
    //     },
    //   })
    // },
    // * updateToCity ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updateStates',
    //     payload: {
    //       cityToData: res.data,
    //     },
    //   })
    // },
    // * updateToCounty ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updateStates',
    //     payload: {
    //       countyToData: res.data,
    //     },
    //   })
    // },
    // // 模态框省市区
    // * updateFromCitys ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updatefirmModalStates',
    //     payload: {
    //       cityFromData: res.data,
    //     },
    //   })
    // },
    // * updateFromCountys ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updatefirmModalStates',
    //     payload: {
    //       countyFromData: res.data,
    //     },
    //   })
    // },
    // * updateToCitys ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updatefirmModalStates',
    //     payload: {
    //       cityToData: res.data,
    //     },
    //   })
    // },
    // * updateToCountys ({ payload }, { put, call }) {
    //   const res = yield call(Linkage, payload)
    //   yield put({
    //     type: 'updatefirmModalStates',
    //     payload: {
    //       countyToData: res.data,
    //     },
    //   })
    // },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updatefirmModalStates (state, { payload }) {
      let { firmModal } = state
      return { ...state, firmModal: { ...firmModal, ...payload } }
    },
    updategoodsModalStates (state, { payload }) {
      let { goodsModal } = state
      return { ...state, goodsModal: { ...goodsModal, ...payload } }
    },
    updateuserModalStates (state, { payload }) {
      let { userModal } = state
      return { ...state, userModal: { ...userModal, ...payload } }
    },
  },
}
