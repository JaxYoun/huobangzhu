import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Iconfont } from 'components'
import { Row, Col, Card, Table, Icon, Button, Popconfirm, message } from 'antd'
import { arrayToTree, isLimited } from 'utils'
import OrgModal from './orgModal'
import UserModal from './userModal'
import OrgFilter from './filter'

const Org = ({
  org,
  app,
  dispatch,
}) => {
  const { currentLevel } = app
  let { orgData, userData, currentOrg } = org
  orgData = arrayToTree(orgData, 'id', 'pId')
  const onDelete = (record) => {
    const type = record.loginName ? 'user' : 'org'
    if (record.children) {
      message.info('当前机构拥有子级机构不能删除！')
      return
    }
    if (type === 'org') {
      if (String(record.id) === sessionStorage.userOrgId) {
        message.info('不能删除当前登录用户所属机构！')
        return
      }
      dispatch({ type: 'org/deleteOrg', payload: record })
    }
    if (type === 'user') {
      if (String(record.id) === sessionStorage.userId) {
        message.info('不能删除当前登录用户！')
        return
      }
      dispatch({ type: 'org/deleteUser', payload: record })
    }
  }
  const handleOrgModal = (opt, record) => {
    let newState = {}
    let orgModal = {}
    orgModal.values = {
      orgName: '',
      pId: '',
      orderCode: '',
      id: '',
    }
    orgModal.ModalTitle = ''
    switch (opt) {
      case 'show':
        orgModal.visible = true
        orgModal.confirmLoading = false
        break
      case 'hide':
        orgModal.visible = false
        orgModal.confirmLoading = false
        break
      case 'edit':
        orgModal.visible = true
        orgModal.values = record
        orgModal.ModalTitle = '编辑机构'
        newState.currentOrg = record.pId
        break
      case 'addOrg':
        orgModal.visible = true
        orgModal.ModalTitle = '创建机构'
        newState.currentOrg = record.id
        break
      default:
        orgModal = {
          visible: false,
        }
    }
    newState.orgModal = orgModal
    dispatch({ type: 'org/handleModal', payload: { ...org, ...newState } })
  }

  const handleUserModal = (opt, record) => {
    let newState = {}
    let userModal = {}
    userModal.values = {
      orgName: '',
      orgId: '',
      userName: '',
      loginName: '',
      email: '',
      id: '',
      mobilePhone: '',
    }
    userModal.ModalTitle = '人员信息'
    switch (opt) {
      case 'show':
        userModal.visible = true
        userModal.confirmLoading = false
        break
      case 'hide':
        userModal.visible = false
        break
      case 'edit':
        userModal.visible = true
        userModal.values = record
        userModal.ModalTitle = '编辑人员信息'
        break
      case 'add':
        userModal.visible = true
        userModal.ModalTitle = '创建人员'
        break
      default:
        userModal = {
          visible: false,
        }
    }
    newState.userModal = userModal
    dispatch({ type: 'org/handleModal', payload: newState })
  }
  const columnsOrg = [{
    title: '机构名称',
    dataIndex: 'orgName',
    render: (text, record) => {
      let className = 'table-tree-selectable'
      className = record.id === currentOrg && record.pId ? `${className} table-tree-selected` : className
      return <span className={className}>{text}</span>
    },
    onCellClick: (record) => {
      dispatch({ type: 'org/queryUser', payload: { orgId: record.id } })
    },
  }, {
    title: '操作',
    dataIndex: 'operation',
    render: (text, record) => {
      return (
        <div className="table-operation">
          <Popconfirm title="确认删除吗?" onConfirm={onDelete.bind(this, record)}>
            {isLimited(currentLevel, 'delete') && <Button icon="delete" />}
          </Popconfirm>
          {isLimited(currentLevel, 'update') && <Button onClick={handleOrgModal.bind(this, 'edit', record)} title="编辑机构" icon="edit" />}
          {isLimited(currentLevel, 'create') && <Button onClick={handleOrgModal.bind(this, 'addOrg', record)} title="添加下级机构" icon="folder-add" />}
        </div>
      )
    },
  },
  ]
  const columnsUser = [{
    title: '姓名',
    dataIndex: 'userName',
  }, {
    title: '登录账号',
    dataIndex: 'loginName',
  }, {
    title: '邮箱',
    dataIndex: 'email',
  }, {
    title: '手机号',
    dataIndex: 'mobilePhone',
  }, {
    title: '状态',
    dataIndex: 'activated',
    className: 'table-activated',
    render: (text, record) => {
      let cls = 'close-circle-o'
      if (record.activated) {
        cls = 'check-circle-o'
      }
      return <Icon type={cls} />
    },
  }, {
    title: '操作',
    dataIndex: 'operation',
    render: (text, record, index) => {
      return (
        <div className="table-operation">
          {isLimited(currentLevel, 'create') && <Button onClick={handleUserModal.bind(this, 'edit', record)} title="编辑人员" icon="edit" />}
          <Popconfirm title="确认删除吗?" onConfirm={() => onDelete(record, index)}>
            {isLimited(currentLevel, 'delete') && <Button icon="delete" title="删除人员" />}
          </Popconfirm>
          <Popconfirm title="确认停用吗?" onConfirm={() => dispatch({ type: 'org/disableUser', payload: { id: record.id }})}>
            {isLimited(currentLevel, 'create') && record.activated && <Button icon="minus-circle-o" title="停用人员" />}
          </Popconfirm>
          <Popconfirm title="确认启用吗?" onConfirm={() => dispatch({ type: 'org/enableUser', payload: { id: record.id }})}>
            {isLimited(currentLevel, 'create') && !record.activated && <Button icon="check-circle-o" title="启用人员" />}
          </Popconfirm>
        </div>
      )
    },
  },
  ]
  return (
    <Row className="custom-style">
      <Col span={9}>
        <Card title={<span><Iconfont type="shu" /> 机构管理</span>} extra={isLimited(currentLevel, 'create') && <Button type="primary" onClick={handleOrgModal.bind(this, 'addOrg')}>创建机构</Button>}>
          <Table
            rowKey="id"
            className="table-tree"
            pagination={false}
            columns={columnsOrg}
            dataSource={orgData}
          />
        </Card>
      </Col>
      <Col span={14} offset={1}>
        <Card title="人员管理" extra={isLimited(currentLevel, 'create') && <Button type="primary" onClick={handleUserModal.bind(this, 'add')}>创建人员</Button>}>
          <OrgFilter orgData={orgData} dispatch={dispatch} />
          <Table
            rowKey="id"
            columns={columnsUser}
            dataSource={userData}
            size="small"
          />
        </Card>
      </Col>
      <OrgModal {...org} dispatch={dispatch} handleModal={handleOrgModal.bind(this)} />
      <UserModal {...org} dispatch={dispatch} handleModal={handleUserModal.bind(this)} />
    </Row>
  )
}


Org.propTypes = {
  form: PropTypes.isRequired,
  org: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  app: PropTypes.object,
}

export default connect(({ app, org }) => ({ app, org }))(Org)
