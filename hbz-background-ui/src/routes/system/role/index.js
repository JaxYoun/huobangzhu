import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Row, Col, message } from 'antd'
import RoleList from './roleList'
import MenuList from './menuList'
import RoleModal from './roleModal'
import MenuModal from './menuModal'


const Role = ({
  role,
  app,
  dispatch,
}) => {
  const { currentLevel } = app
  const onChangeMenuExpand = (selectedMenus) => {
    dispatch({
      type: 'role/changeStates',
      payload: { selectedMenus },
    })
  }
  /*
  * 修改菜单权限，两种情况：1.修改菜单勾选，2.修改菜单下拉权限等级
  */
  const onChangeMenu = (records) => {
    let { roleMenu } = role
    if (records.level) {                     // 修改下拉菜单权限等级时
      const key = roleMenu.findIndex(item => item.menuId === records.id)
      if (!roleMenu.length || key < 0) {
        message.info('请先勾选菜单')
      } else {
        roleMenu[key].level = records.level
      }
    } else {                                // 更改菜单勾选时
      roleMenu = records.map(item => {
        return ({ menuId: item.id, level: item.level || 99 })
      })
    }
    console.log('roleMenu', roleMenu)
    console.log('records', records)
    dispatch({
      type: 'role/changeStates',
      payload: { roleMenu },
    })
  }

  const addMenu = () => {
    const newState = role.menuModal
    Object.assign(newState, { visible: true, values: {} })
    dispatch({
      type: 'role/changeStates',
      payload: { newState },
    })
  }
  const saveMenu = () => {
    const { roleMenu, selectedRole } = role
    if (!selectedRole) {
      return message.info('请先选择角色')
    }
    const menus = roleMenu.map(item => {
      return ({ menuId: item.menuId, level: item.level || 99 })
    })
    return dispatch({
      type: 'role/saveMenu',
      payload: {
        menus,
        roleId: selectedRole,
      },
    })
  }

  const queryMenuListByRoleId = (id) => {
    dispatch({
      type: 'role/queryMenuListByRoleId',
      payload: { roleId: id },
    })
  }

  const addRole = () => {
    const newState = role.roleModal
    Object.assign(newState, { visible: true, values: {}, ModalTitle: '创建角色' })
    dispatch({
      type: 'role/changeStates',
      payload: { newState },
    })
  }
  const editRole = (record) => {
    let newState = role.roleModal
    Object.assign(newState, { visible: true, values: record, ModalTitle: '编辑角色' })
    dispatch({
      type: 'role/changeStates',
      payload: { newState },
    })
  }

  const parameters = { currentLevel, onChangeMenuExpand, onChangeMenu, addMenu, saveMenu, queryMenuListByRoleId, addRole, editRole }
  return (
    <Row className="custom-style">
      <Col span="8">
        <RoleList {...role} {...parameters} dispatch={dispatch} />
      </Col>
      <Col span="15" offset="1">
        <MenuList {...role} {...parameters} dispatch={dispatch} />
      </Col>
      <RoleModal {...role} {...parameters} dispatch={dispatch} />
      <MenuModal {...role} {...parameters} dispatch={dispatch} />
    </Row>
  )
}
Role.propTypes = {
  role: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
export default connect(({ role, app }) => ({ role, app }))(Role)
