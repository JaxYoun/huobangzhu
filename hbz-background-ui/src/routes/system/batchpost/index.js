import React from 'react'
import { Table, Card, Popconfirm, Row, Col, Button, Icon, message } from 'antd'
import PostModal from './postModal'
import { isLimited } from 'utils'
import { connect } from 'dva'
import PropTypes from 'prop-types'
import { Iconfont, Ttable } from 'components'

const Post = ({ batchpost, app, dispatch }) => {
  const { currentLevel } = app
  let { roleData, postRoleKeys, postSelected, loading, tableTime } = batchpost
  const onDelete = record => {
    dispatch({ type: 'batchpost/onDelete', payload: record })
  }
  const handlePostModal = (opt, record) => {
    console.log(record)
    let newState = {}
    let modalData = {}
    modalData.values = {
      name: '',
      postName: '',
      orgId: '',
      orderCode: '',
      id: '',
      postDesc: '',
    }
    modalData.ModalTitle = ''
    switch (opt) {
      case 'show':
        modalData.visible = true
        modalData.confirmLoading = false
        break
      case 'hide':
        modalData.visible = false
        break
      case 'edit':
        modalData.visible = true
        modalData.values = record
        modalData.ModalTitle = '编辑岗位'
        break
      case 'add':
        modalData.visible = true
        modalData.values.pId = record.id
        modalData.ModalTitle = '创建岗位'
        break
      default:
        modalData = {
          visible: false,
        }
    }
    newState.postModal = modalData
    dispatch({ type: 'batchpost/handlePostModal', payload: newState })
  }
  const saveRoleLimit = () => {
    if (!postSelected) {
      message.info('请先选择岗位！')
      return
    }
    dispatch({
      type: 'batchpost/saveRoleLimit',
      payload: { ...postSelected, roleIds: postRoleKeys },
    })
  }
  const columnsOrg = [
    {
      title: '岗位名称',
      dataIndex: 'postName',
      render: (text, record) => {
        const type = record.nodeType === 1 ? '' : 'user'
        let className = record.nodeType === 1 ? '' : 'table-tree-selectable'
        className =
          record.id === postSelected.id ? `${className} table-tree-selected` : className
        return (
          <span className={className}>
            <Icon type={type} />
            {text}
          </span>
        )
      },
      onCellClick: (record) => {
        console.log(record)
        dispatch({ type: 'batchpost/queryRolesByPostId', payload: record })
      },
    },
    {
      title: '岗位级别',
      dataIndex: 'postLevel',
    },
    {
      title: '操作',
      dataIndex: 'operation',
      render: (text, record) => {
        let children = (
          <div className="table-operation">
            <Popconfirm
              title="确认删除吗?"
              onConfirm={onDelete.bind(this, record)}
            >
              {isLimited(currentLevel, 'delete') && <Button icon="delete" />}
            </Popconfirm>
            {isLimited(currentLevel, 'update') && (
              <Button
                icon="edit"
                onClick={handlePostModal.bind(this, 'edit', record)}
                title="编辑岗位"
              />
            )}
          </div>
        )
        return children
      },
    },
  ]
  const columnsRole = [
    {
      title: '角色名称',
      dataIndex: 'roleName',
      key: 'id',
    },
    {
      title: '角色描述',
      dataIndex: 'roleDesc',
    },
    {
      title: '状态',
      dataIndex: 'status',
      className: 'table-status',
      render: (text, record) => {
        let cls = 'close-circle-o'
        if (record.status) {
          cls = 'check-circle-o'
        }
        return <Icon type={cls} />
      },
    },
  ]
  const onChangeRole = keys => {
    dispatch({
      type: 'batchpost/onChangeRole',
      payload: { postRoleKeys: keys },
    })
  }
  const rowSelection = {
    selectedRowKeys: postRoleKeys,
    onChange: (selectedRowKeys, selectedRows) => {
      onChangeRole(selectedRowKeys, selectedRows)
    },
  }
  const tableProps = {
    columns: columnsOrg,
    sourceUrl: '/system/post/findBatchOptionPost',
    tableTime,
  }
  return (
    <Row className="custom-style">
      <Col span={9}>
        <Card
          title={
            <span>
              <Iconfont type="shu" />岗位管理
            </span>
          }
          extra={
            isLimited(currentLevel, 'create') && (
              <Button
                type="primary"
                onClick={handlePostModal.bind(this, 'add')}
              >
                新增岗位
              </Button>
            )
          }
        >
          <Ttable {...tableProps} />
        </Card>
      </Col>
      <Col span={14} offset={1}>
        <Card
          title={
            <span>
              <Iconfont type="shenfen-tianchong" /> 角色列表
            </span>
          }
          extra={
            isLimited(currentLevel, 'create') && (
              <Button
                loading={loading}
                type="primary"
                onClick={saveRoleLimit.bind(this, 'add')}
              >
                保存岗位角色
              </Button>
            )
          }
        >
          <Table
            columns={columnsRole}
            dataSource={roleData}
            size="small"
            rowSelection={rowSelection}
            pagination={false}
            rowKey="id"
          />
        </Card>
      </Col>
      <PostModal
        {...batchpost}
        handleModal={handlePostModal.bind(this)}
        dispatch={dispatch}
      />
    </Row>
  )
}

Post.propTypes = {
  form: PropTypes.isRequired,
  batchpost: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  app: PropTypes.object,
  tableTime: PropTypes.string,
}

export default connect(({ batchpost, app }) => ({ batchpost, app }))(Post)
