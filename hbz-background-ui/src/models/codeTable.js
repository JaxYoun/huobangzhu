import { addCodeTable, editCodeTable, deleteCodeTable, updateCodeTable } from '../services/codeTable'

export default {
  namespace: 'codeTable',
  state: {
    fields: {},
    visible: false,
    tableTime: 0,
    formData: '',
    type: '',
  },
  effects: {
    *queryTable ({ payload }, { put }) {
      yield put({ type: 'updateStates',
      payload: {
        fields: payload,
        tableTime: new Date(),
      } })
    },
    *showModal ({ payload }, { put }) {
      yield put({ type: 'updateStates',
      payload: {
        visible: true,
        type: payload.type,
        formData: payload.data,
      } })
    },
    *hideModal ({ payload }, { put }) {
      yield put({ type: 'updateStates',
      payload: {
        visible: false,
      } })
    },
    *saveCodeTable ({ payload }, { put, call, select }) {
      const types = yield select(state => state.codeTable.type)
      let data = {}
      types === "add" ?
      data = yield call(addCodeTable, payload)
      :
      data = yield call(editCodeTable, payload)
      if (data.success) {
        yield put({
          type: 'updateStates',
          payload: {
            visible: false,
            fields: {},
            tableTime: new Date(),
        } })
      }
    },
    *deleteCode ({ payload }, { put, call }) {
      const codeData = yield call(deleteCodeTable, { dicCode: payload })
      if (codeData.success) {
        yield put({
          type: 'updateStates',
          payload: {
            fields: {},
            tableTime: new Date(),
        } })
      }
    },
    *updateTable ({ payload }, { put, call }) {
      const tableData = yield call(updateCodeTable, { dicCode: payload })
      if (tableData.success) {
        yield put({
          type: 'updateStates',
          payload: {
            fields: {},
            tableTime: new Date(),
        } })
      }
    },
  },
  reducers: {
    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
  },
}
