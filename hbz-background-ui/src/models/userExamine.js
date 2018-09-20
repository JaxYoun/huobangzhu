import { getData, puts, getDatas, addPsyData, addQyhzData, addGrhzData, addGrsjData, addYsqyData } from '../services/userExamine'

export default {
  namespace: 'userExamine',
  state: {
    fields: [],
    tableTime: 0,
    registryData: [],
    registryProgressData: [],
    certificateTypeData: [],
    transTypeData: [],
    firmModal: {
      visible: false,
      firmData: {},
      imgBusinessLicense: '',
      imgId: '',
      imgCertifiedPhoto: '',
      imgVehicle45degreePhoto: '',
      imgDrivingLicense: '',
      imgDrivingLicense2: '',
      imgPlyLicense: '',
      imgPlyLicense2: '',
      loading: false,
    },
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/userMessage/userExamine') {
          dispatch({
            type: 'update',
            payload: {
              fields: [],
              tableTime: 0,
              registryData: [],
              registryProgressData: [],
              certificateTypeData: [],
              transTypeData: [],
              firmModal: {
                visible: false,
                firmData: {},
                imgBusinessLicense: '',
                imgId: '',
                imgCertifiedPhoto: '',
                imgVehicle45degreePhoto: '',
                imgDrivingLicense: '',
                imgDrivingLicense2: '',
                imgPlyLicense: '',
                imgPlyLicense2: '',
                loading: false,
              },
            },
          })
          dispatch({ type: 'getRegistryData', payload: { enumname: 'RegistryCode' } })
          dispatch({ type: 'getRegistryProgressData', payload: { enumname: 'RegistryProgress' } })
          dispatch({ type: 'getCertificateTypeData', payload: { enumname: 'CertificateType' } })
          dispatch({ type: 'getTransTypeData', payload: { enumname: 'TransType' } })
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
    // 获取资质类型
    * getRegistryData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          registryData: res.data,
        },
      })
    },
    // 获取审核状态
    * getRegistryProgressData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          registryProgressData: res.data,
        },
      })
    },
    // 获取证件类型
    * getCertificateTypeData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          certificateTypeData: res.data,
        },
      })
    },
    // 获取车辆类型
    * getTransTypeData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          transTypeData: res.data,
        },
      })
    },
    // 审核通过
    * passPut ({ payload }, { put, call }) {
      const res = yield call(puts, payload)
      if (res.code === '200') {
        yield put({
          type: 'update',
          payload: {
            tableTime: new Date().getTime(),
          },
        })
      }
    },
    // 数据获取
    * getData  ({ payload }, { put, call }) {
      const res = yield call(getDatas, payload)
      yield put({
        type: 'updatefirmModal',
        payload: {
          firmData: res.data,
        },
      })
    },
    // 关闭清空模态框
    * cleanData  ({ payload }, { put }) {
      yield put({
        type: 'updatefirmModal',
        payload: {
          visible: false,
          firmData: {},
          imgBusinessLicense: '',
          imgId: '',
          imgCertifiedPhoto: '',
          imgVehicle45degreePhoto: '',
          imgDrivingLicense: '',
          imgDrivingLicense2: '',
          imgPlyLicense: '',
          imgPlyLicense2: '',
          loading: false,
        },
      })
      yield put({
        type: 'update',
        payload: {
          tableTime: new Date().getTime(),
        },
      })
    },
    // 五种状态数据保存
    // 配送员
    * addPsy  ({ payload }, { put, call }) {
      const res = yield call(addPsyData, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanData',
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
    // 企业货主
    * addQyhz  ({ payload }, { put, call }) {
      const res = yield call(addQyhzData, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanData',
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
    // 个人货主
    * addGrhz  ({ payload }, { put, call }) {
      const res = yield call(addGrhzData, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanData',
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
    // 个人司机
    * addGrsj  ({ payload }, { put, call }) {
      const res = yield call(addGrsjData, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanData',
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
    // 运输企业
    * addYsqy  ({ payload }, { put, call }) {
      const res = yield call(addYsqyData, payload)
      if (res.code === '200') {
        yield put({
          type: 'cleanData',
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
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    updatefirmModalStates (state, { payload }) {
      let { firmModal } = state
      return { ...state, firmModal: { ...firmModal, ...payload } }
    },
  },
}
