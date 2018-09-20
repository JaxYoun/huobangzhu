import React from 'react'
import { connect } from 'dva'
import PropTypes from 'prop-types'
import { Table, Card, Popconfirm, Row, Col, Button } from 'antd'
import MenuModal from './menuModal'
import { Iconfont } from 'components'

const Menu = ({ menuManagement, dispatch, loading }) => {
  const { menuData } = menuManagement
  const onDelete = record => {
    dispatch({ type: 'menuManagement/menuDelete', payload: { id: record.id } })
  }
  const handleMenuModal = (opt, record) => {
    let newState = {}
    let menuModal = {}
    menuModal.values = {}
    menuModal.ModalTitle = ''
    switch (opt) {
      case 'show':
        menuModal.visible = true
        break
      case 'hide':
        menuModal.visible = false
        break
      case 'edit':
        menuModal.visible = true
        menuModal.values = record
        menuModal.ModalTitle = '编辑菜单'
        break
      case 'add':
        menuModal.visible = true
        menuModal.ModalTitle = '创建菜单'
        newState.currentPid = record.id
        break
      default:
        menuModal = {
          visible: false,
        }
    }
    newState.menuModal = menuModal
    dispatch({
      type: 'menuManagement/handleModal',
      payload: { ...menuManagement, ...newState },
    })
  }
  const columnsMenu = [
    {
      title: '菜单名称',
      dataIndex: 'name',
    },
    {
      title: '操作',
      dataIndex: 'operation',
      render: (text, record, index) => {
        return (
          <div className="table-operation">
            <Popconfirm
              title="确认删除吗?"
              onConfirm={() => onDelete(record, index)}
            >
              {<Button icon="delete" title="删除菜单" />}
            </Popconfirm>
            {
              <Button
                onClick={handleMenuModal.bind(this, 'edit', record)}
                title="编辑菜单"
                icon="edit"
              />
            }
            {
              <Button
                onClick={handleMenuModal.bind(this, 'add', record)}
                title="添加下级菜单"
                icon="folder-add"
              />
            }
          </div>
        )
      },
    },
  ]
  return (
    <Row className="custom-style">
      <Col span={16}>
        <Card
          title={
            <span>
              <Iconfont type="createtask_fill" /> 菜单管理
            </span>
          }
          extra={
            <Button type="primary" onClick={handleMenuModal.bind(this, 'add')}>
              创建菜单
            </Button>
          }
        >
          {menuData.length && (
            <Table
              pagination={false}
              columns={columnsMenu}
              dataSource={menuData}
              rowKey="id"
              size="small"
              className="table-tree"
            />
          )}
        </Card>
      </Col>
      <MenuModal
        {...menuManagement}
        confirmLoading={loading.global}
        dispatch={dispatch}
        handleModal={handleMenuModal.bind(this)}
      />
    </Row>
  )
}
Menu.propTypes = {
  menuManagement: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,
  loading: PropTypes.bool,
}

export default connect(({ loading, menuManagement }) => ({
  loading,
  menuManagement,
}))(Menu)
