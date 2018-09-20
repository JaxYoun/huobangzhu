import {
  makeRolesGuest,
  addGuest,
  disableGuest,
  getRegisterInfo,
  queryRole,
  enableGuest,
  updateGuest,
} from '../services/guestManagement'
import {
  getData,
  puts,
  getDatas,
  addPsyData,
  addQyhzData,
  addGrhzData,
  addGrsjData,
  addYsqyData,
} from '../services/userExamine'

export default {
  namespace: 'guestManagement',
  state: {
    fields: [],
    tableTime: 0,
    guestModal: {
      visible: false,
    },
    detailModal: {
      visible: false,
    },
    roleModal: {
      visible: false,
    },
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
      history.listen(location => {
        if (location.pathname === '/userMessage/guestManagement') {
          dispatch({
            type: 'guestManagement/update',
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
          dispatch({
            type: 'guestManagement/getRegistryData',
            payload: { enumname: 'RegistryCode' },
          })
          dispatch({
            type: 'guestManagement/getRegistryProgressData',
            payload: { enumname: 'RegistryProgress' },
          })
          dispatch({
            type: 'guestManagement/getCertificateTypeData',
            payload: { enumname: 'CertificateType' },
          })
          dispatch({
            type: 'guestManagement/getTransTypeData',
            payload: { enumname: 'TransType' },
          })
        }
      })
    },
  },
  effects: {
    *update ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },
    *addGuest ({ payload }, { call, put }) {
      const res = yield call(addGuest, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { guestModal: { visible: false }, tableTime: new Date() },
        })
      }
    },
    *updateGuest ({ payload }, { call, put }) {
      const res = yield call(updateGuest, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { guestModal: { visible: false }, tableTime: new Date() },
        })
      }
    },
    *enableGuest ({ payload }, { call, put }) {
      const res = yield call(enableGuest, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date() },
        })
      }
    },
    *queryRole ({ payload }, { call, put, select }) {
      const prestate = yield select(_ => _.guestManagement)
      const roleList = yield call(queryRole, {})
      const userRole = yield call(queryRole, payload)
      if (roleList.success && userRole.success) {
        yield put({
          type: 'updateStates',
          payload: {
            roleModal: {
              ...prestate.roleModal,
              roleList: roleList.data,
              userRole: userRole.data.map(item => item.id),
            },
          },
        })
      } else {
        const errorMessage = { message: '数据加载失败，请重试！' }
        throw errorMessage
      }
    },
    *makeRolesGuest ({ payload }, { call, put }) {
      const res = yield call(makeRolesGuest, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date(), roleModal: { visible: false } },
        })
      } else {
        const errorMessage = { message: '操作失败，请重试！' }
        throw errorMessage
      }
    },
    *disableGuest ({ payload }, { call, put }) {
      const res = yield call(disableGuest, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: { tableTime: new Date() },
        })
      }
    },
    *getRegisterInfo ({ payload }, { call, put }) {
      const res = yield call(getRegisterInfo, payload)
      if (res.success) {
        yield put({
          type: 'updateStates',
          payload: {
            detailModal: {
              visible: true,
              ...res.data,
            },
          },
        })
      }
    },
    *updatefirmModal ({ payload }, { put }) {
      yield put({
        type: 'updatefirmModalStates',
        payload,
      })
    },
    // 获取资质类型
    *getRegistryData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          registryData: res.data,
        },
      })
    },
    // 获取审核状态
    *getRegistryProgressData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          registryProgressData: res.data,
        },
      })
    },
    // 获取证件类型
    *getCertificateTypeData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          certificateTypeData: res.data,
        },
      })
    },
    // 获取车辆类型
    *getTransTypeData ({ payload }, { put, call }) {
      const res = yield call(getData, payload)
      yield put({
        type: 'update',
        payload: {
          transTypeData: res.data,
        },
      })
    },
    // 审核通过
    *passPut ({ payload }, { put, call }) {
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
    *getData ({ payload }, { put, call }) {
      const res = yield call(getDatas, payload)
      yield put({
        type: 'updatefirmModal',
        payload: {
          firmData: res.data,
        },
      })
    },
    // 关闭清空模态框
    *cleanData ({ payload }, { put }) {
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
    *addPsy ({ payload }, { put, call }) {
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
    *addQyhz ({ payload }, { put, call }) {
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
    *addGrhz ({ payload }, { put, call }) {
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
    *addGrsj ({ payload }, { put, call }) {
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
    *addYsqy ({ payload }, { put, call }) {
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
