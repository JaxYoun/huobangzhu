import React from 'react'
import { Table, Card, Button, Popconfirm } from 'antd'
import PropTypes from 'prop-types'
import { isLimited } from 'utils'
import { Iconfont } from 'components'

const roleList = ({
  dispatch,
  queryMenuListByRoleId,
  addRole,
  editRole,
  checkedId,
  roleData,
  currentLevel,
}) => {
  const editrole = (record) => {
    editRole(record)
  }
  const queryMenuList = (record) => {
    queryMenuListByRoleId(record.id)
  }
  const deleteRole = (record) => {
    dispatch({
      type: 'role/deleteRole',
      payload: { id: record.id },
    })
  }
  const columns = [{
    title: '角色名称',
    dataIndex: 'roleName',
    render: (text, record) => {
      let className = 'table-tree-selectable'
      className = record.id === checkedId ? `${className} table-tree-selected` : className
      return <span className={className}>{text}</span>
    },
  }, {
    title: '操作',
    dataIndex: 'options',
    render: (text, record) => {
      return (
        <div>
          {isLimited(currentLevel, 'update') && <Button icon="edit" onClick={editrole.bind(this, record)} />}
          <Popconfirm title="确认删除角色吗？" okText="确认" cancelText="取消" onConfirm={deleteRole.bind(this, record)}>
            {isLimited(currentLevel, 'delete') && <Button icon="delete" />}
          </Popconfirm>
        </div>
      )
    },
  }]
  return (
    <Card title={<span>角色列表</span>} extra={isLimited(currentLevel, 'create') && <Button type="primary" onClick={addRole}>创建角色</Button>}>
      <Table columns={columns} dataSource={roleData} pagination={false} onRowClick={queryMenuList.bind(this)} />
    </Card>
  )
}
roleList.propTypes = {
  queryRoleList: PropTypes.func,
  roleData: PropTypes.array,
  addRole: PropTypes.func,
  checkedId: PropTypes.string,
  queryMenuList: PropTypes.func,
  onChangeMenu: PropTypes.func,
  addMenu: PropTypes.func,
  saveMenu: PropTypes.func,
  queryMenuListByRoleId: PropTypes.func,
  editRole: PropTypes.func,
  handleModal: PropTypes.func,
  dispatch: PropTypes.func,
  currentLevel: PropTypes.object,
}
export default roleList
