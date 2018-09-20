import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Button, Switch, Popconfirm } from 'antd'
import { Ttable } from 'components'
import Filter from './filter'
import RoleModal from './roleModal'
import AuthorityModaL from './authorityModal'
import MenuModal from './menuModal'

const RoleManagement = ({ loading, dispatch, roleManagement }) => {
  const {
    fields,
    tableTime,
    roleModal,
    authorityModal,
    menuModal,
    roleCode,
  } = roleManagement
  const makeAuthorities = record => {
    dispatch({
      type: 'roleManagement/update',
      payload: {
        authorityModal: {
          visible: true,
          ...record,
        },
      },
    })
    dispatch({
      type: 'roleManagement/queryAuthority',
      payload: { roleId: record.id },
    })
  }
  const makeMenus = record => {
    dispatch({
      type: 'roleManagement/update',
      payload: {
        menuModal: {
          visible: true,
          ...record,
        },
      },
    })
    dispatch({
      type: 'roleManagement/queryMenus',
      payload: { roleId: record.id },
    })
  }
  const switchActive = (id, checked) => {
    dispatch({
      type: 'roleManagement/updateRecord',
      payload: { id, state: checked ? 1 : 0 },
    })
  }
  const deleteRecord = record => {
    dispatch({
      type: 'roleManagement/deleteRecord',
      payload: { id: record.id },
    })
  }
  const editRecord = record => {
    dispatch({
      type: 'roleManagement/update',
      payload: { roleModal: { visible: true, ...record } },
    })
  }
  let tableProps = {
    columns: [
      {
        title: '编号',
        dataIndex: 'id',
      },
      {
        title: '角色名称',
        dataIndex: 'roleName',
      },
      {
        title: '角色编码',
        dataIndex: 'role',
      },
      {
        title: '角色状态',
        dataIndex: 'state',
        render: (text, record) => {
          return (
            <Switch
              checkedChildren="启用"
              unCheckedChildren="停用"
              checked={Number(text) === 1}
              onChange={switchActive.bind(this, record.id)}
            />
          )
        },
      },
      {
        title: '操作',
        dataIndex: '',
        render: (text, record) => {
          return (
            <div>
              <Button
                type="primary"
                onClick={makeAuthorities.bind(this, record)}
              >
                配置权限
              </Button>
              <Button type="primary" onClick={makeMenus.bind(this, record)}>
                绑定菜单
              </Button>
              <Button onClick={editRecord.bind(this, record)}>编辑</Button>
              <Popconfirm
                title="确定要删除吗？"
                onConfirm={deleteRecord.bind(this, record)}
              >
                <Button>删除</Button>
              </Popconfirm>
            </div>
          )
        },
      },
    ],
    sourceUrl: '/role/queryPage',
    fields: fields || {},
    tableTime,
  }
  const authorityModalProps = {
    dispatch,
    loading,
    authorityModal,
  }
  const menuModalProps = {
    dispatch,
    loading,
    menuModal,
  }
  const roleModalProps = {
    roleModal,
    dispatch,
    loading,
    roleCode,
  }
  return (
    <div>
      <Filter dispatch={dispatch} />
      <Card>
        <Ttable {...tableProps} />
      </Card>
      {roleModal.visible && <RoleModal {...roleModalProps} />}
      {authorityModal.visible && <AuthorityModaL {...authorityModalProps} />}
      {menuModal.visible && <MenuModal {...menuModalProps} />}
    </div>
  )
}

RoleManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  roleManagement: PropTypes.obj,
  loading: PropTypes.bool,
  roleCode: PropTypes.object,
}
export default connect(({ roleManagement, loading }) => ({
  roleManagement,
  loading,
}))(RoleManagement)
