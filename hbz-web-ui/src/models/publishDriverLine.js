// import { create } from 'services/publishDriverLine'
// import { routerRedux } from 'dva/router'
import { enums } from 'services/app'
import { address01 } from 'utils'
import { transSize, createDriverLine } from 'services/publishDriverLine'

export default {
  namespace: 'publishDriverLine',
  state: {
    provinceData: [],
    cityData: [],
    areaData: [],
    distCityData: [],
    distAreaData: [],
    transTypes: {},
    visible: false,
    transSizeData: [],
    selectCarsId: [],
    selectCarsName: [],
    baseUploading: false,
    baseFileList: [],
  },
  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/driverEnterprise/publishDriverLine') {
          dispatch({ type: 'queryEnums' })
          dispatch({ type: 'getTreeData' })
          dispatch({
            type: 'changeStates',
            payload: {
              selectCarsId: [],
              selectCarsName: [],
              baseFileList: [],
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
    * queryEnums ({ payload }, { call, put }) {
      const { data: transTypes } = yield call(enums, 'TransType')
      yield put({
        type: 'updateState',
        payload: {
          transTypes,
        },
      })
    },
    * getTreeData ({ payload }, { put }) {
      const treeData = []
      for (let item of address01) {
        if (item.id === 0 || item.parentId === 0) {
          treeData.push(item)
        }
      }
      yield put({
        type: 'updateState',
        payload: {
          provinceData: treeData,
        },
      })
    },
    * getSelectData ({ payload }, { put }) {
      const parent = payload.parent
      const Data = []
      for (let item of address01) {
        if (item.parentId === payload.id) {
          Data.push(item)
        }
      }
      yield put({
        type: 'updateState',
        payload: {
          [parent]: Data,
        },
      })
    },
    * getAreaData ({ payload }, { put }) {
      const data = []
      for (let item of address01) {
        if (item.parentId === payload.id) {
          data.push(item)
        }
      }
      yield put({
        type: 'updateState',
        payload: {
          cityData: [],
          areaData: data,
        },
      })
    },
    * getDistAreaData ({ payload }, { put }) {
      const data = []
      for (let item of address01) {
        if (item.parentId === payload.id) {
          data.push(item)
        }
      }
      yield put({
        type: 'updateState',
        payload: {
          distCityData: [],
          distAreaData: data,
        },
      })
    },
    * showModals ({ payload = {} }, { call, put }) {
      const res = yield call(transSize, payload)
      if (res.success) {
        yield put({
          type: 'updateState',
          payload: {
            visible: true,
            selectCarsId: [],
            selectCarsName: [],
            transSizeData: res.data,
          },
        })
      }
    },
    * createDriverLine ({ payload }, { call, put }) {
      const res = yield call(createDriverLine, payload)
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
