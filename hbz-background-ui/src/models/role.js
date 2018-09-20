import { queryRoleList, queryMenuList, queryMenuListByRoleId, saveMenuLimit, deleteRole, save } from '../services/role'

export default {
  namespace: 'role',

  state: {
    roleData: [], // 角色列表
    menuData: [], // 所有菜单列表
    menuSave: [],
    roleMenu: [], // 当前角色对应的菜单
    selectedRole: '',
    checkedId: '',
    loading: false,
    roleModal: {
      ModalTitle: '',
      visible: false,
      confirmLoading: false,
      values: {
        roleDesc: '',
        roleName: '',
        id: '',
      },
    },
    menuModal: {
      ModalTitle: '创建菜单',
      visible: false,
      confirmLoading: false,
      values: {
        menuName: '',
        pId: '',
        id: '',
        relationship: '',
        menuUrl: '',
        menuIcon: '',
        orderCode: '',
      },
    },
  },

  subscriptions: {
    setup ({ dispatch, history }) {
      history.listen((location) => {
        if (location.pathname === '/system/role') {
          dispatch({
            type: 'queryRole',
          })
          dispatch({
            type: 'queryMenu',
          })
        }
      })
    },
  },

  effects: {

    * queryRole ({ payload = {} }, { call, put }) {
      const json = yield call(queryRoleList, payload)
      if (json.success) {
        yield put({
          type: 'updateStates',
          payload: {
            roleData: json.data,
          },
        })
      }
    },
    * queryMenu ({ payload = {} }, { call, put }) {
      yield put({ type: 'updateStates', payload: { currentOrg: payload.orgId } })
      const json = yield call(queryMenuList, payload)
      if (json.success) {
        yield put({
          type: 'updateStates',
          payload: {
            menuData: json.data,
            roleMenu: [],
            selectedRole: '',
          },
        })
      }
    },

    * changeStates ({ payload }, { put }) {
      yield put({
        type: 'updateStates',
        payload,
      })
    },

    * handleModal ({ payload }, { put }) {
      yield put({ type: 'setVisiblefalse', payload: 'roleModal' })
      yield put({ type: 'setVisiblefalse', payload: 'menuModal' })
    },

    * queryMenuListByRoleId ({ payload }, { call, put }) {
      const json = yield call(queryMenuListByRoleId, payload)
      if (json.success) {
        const roleMenu = json.data.map(item => ({ menuId: item.menuId, level: item.level }))
        yield put({
          type: 'updateStates',
          payload: {
            checkedId: payload.roleId,
            roleMenu,
            selectedRole: payload.roleId,
          },
        })
      }
    },

    * saveMenu ({ payload }, { call, put }) {
      yield put({ type: 'updateStates', payload: { loading: true } })
      yield call(saveMenuLimit, payload)
      yield put({ type: 'updateStates', payload: { loading: false } })
    },

    * deleteRole ({ payload }, { call, put }) {
      const res = yield call(deleteRole, payload)
      if (res.success) {
        yield put({ type: 'queryRole' })
        yield put({ type: 'queryMenu' })
      }
    },

    * save ({ payload }, { call, put }) {
      yield put({ type: 'confirmLoading', payload: 'roleModal' })
      const { values, form } = payload
      const res = yield call(save, values)
      if (res.success) {
        yield put({ type: 'handleModal', payload: 'hide' })
        yield put({ type: 'confirmLoading', payload: 'roleModal' })
        yield put({ type: 'queryRole' })
        form.resetFields()
      } else {
        yield put({ type: 'confirmLoading', payload: 'roleModal' })
      }
    },

    * saveMenuModal ({ payload }, { call, put }) {
      yield put({ type: 'confirmLoading', payload: 'menuModal' })
      const { values, form } = payload
      const res = yield call(save, values)
      if (res.success) {
        yield put({ type: 'handleModal', payload: 'hide' })
        yield put({ type: 'confirmLoading', payload: 'menuModal' })
        yield put({ type: 'queryMenu' })
        form.resetFields()
      } else {
        yield put({ type: 'confirmLoading', payload: 'menuModal' })
      }
    },
  },

  reducers: {

    updateStates (state, { payload }) {
      return { ...state, ...payload }
    },
    confirmLoading (state, { payload }) {
      const confirmProps = state[payload]
      confirmProps.confirmLoading = !confirmProps.confirmLoading
      return { ...state, confirmProps }
    },
    setVisiblefalse (state, { payload }) {
      const confirmProps = state[payload]
      confirmProps.visible = false
      return { ...state, confirmProps }
    },
  },
}
