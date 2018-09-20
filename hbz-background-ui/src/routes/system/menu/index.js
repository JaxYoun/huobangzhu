import React from 'react'
import { connect } from 'dva'
import PropTypes from 'prop-types'
import { Table, Card, Popconfirm, Row, Col, Button } from 'antd'
import MenuModal from './menuModal'
import { isLimited } from 'utils'
import { Iconfont } from 'components'

const Menu = ({
  menu,
  app,
  dispatch,
}) => {
  const { currentLevel } = app
  const { menuData } = menu
  const onDelete = (record) => {
    dispatch({ type: 'menu/menuDelete', payload: { id: record.id } })
  }
  const handleMenuModal = (opt, record) => {
    let newState = {}
    let menuModal = {}
    menuModal.values = {}
    menuModal.ModalTitle = ''
    switch (opt) {
      case 'show':
        menuModal.visible = true
        menuModal.confirmLoading = false
        break
      case 'hide':
        menuModal.visible = false
        menuModal.confirmLoading = false
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
    dispatch({ type: 'menu/handleModal', payload: { ...menu, ...newState } })
  }
  const columnsMenu = [{
    title: '菜单名称',
    dataIndex: 'menuName',
    render: (text, record) => {
      return <span className={record.menuType === 1 ? '' : 'disabled-item'}>{text}</span>
    },
  }, {
    title: '操作',
    dataIndex: 'operation',
    render: (text, record, index) => {
      return (
        <div className="table-operation">
          <Popconfirm title="确认删除吗?" onConfirm={() => onDelete(record, index)}>
          {isLimited(currentLevel, 'delete') && <Button icon="delete" title="删除菜单" />}
          </Popconfirm>
          {isLimited(currentLevel, 'update') && <Button onClick={handleMenuModal.bind(this, 'edit', record)} title="编辑菜单" icon="edit" />}
          {isLimited(currentLevel, 'create') && <Button onClick={handleMenuModal.bind(this, 'add', record)} title="添加下级菜单" icon="folder-add" />}
        </div>
      )
    },
  },
  ]
  return (<Row className="custom-style">
    <Col span={10}>
      <Card title={<span><Iconfont type="createtask_fill" /> 菜单管理</span>} extra={isLimited(currentLevel, 'create') && <Button type="primary" onClick={handleMenuModal.bind(this, 'add')}>创建菜单</Button>}>
        {menuData.length &&
          <Table pagination={false} columns={columnsMenu} dataSource={menuData} rowKey="id" size="small" className="table-tree" />
         }
      </Card>
    </Col>
    <MenuModal {...menu} dispatch={dispatch} handleModal={handleMenuModal.bind(this)} />
  </Row>
  )
}
Menu.propTypes = {
  menu: PropTypes.object.isRequired,
  app: PropTypes.object.isRequired,
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ app, menu }) => ({ app, menu }))(Menu)
