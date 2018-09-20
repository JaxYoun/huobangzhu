import React from 'react'
import { Table, Card, Popconfirm, Row, Col, Button, Icon, message } from 'antd'
import PostModal from './postModal'
import { arrayToTree, isLimited } from 'utils'
import { connect } from 'dva'
import PropTypes from 'prop-types'
import { Iconfont } from 'components'

const Post = ({
  post,
  app,
  dispatch,
}) => {
  const { currentLevel } = app
  let { roleData, postData, postRoleKeys, postId, loading } = post
  postData = arrayToTree(postData, 'id', 'pId')
  const onDelete = (record) => {
    let obj = {}
    obj.id = record.id.split('-')[1]
    dispatch({ type: 'post/onDelete', payload: obj })
  }
  const getPost = (data) => {
    let obj = {}
    obj.id = data.split('-')[1]
    dispatch({ type: 'post/getPost', payload: obj })
  }
  const handlePostModal = (opt, record) => {
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
        getPost(record.id)
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
    dispatch({ type: 'post/handlePostModal', payload: newState })
  }
  const saveRoleLimit = () => {
    if (!postId) {
      message.info('请先选择岗位！')
      return
    }
    dispatch({ type: 'post/saveRoleLimit', payload: { postId: postId.split('-')[1], roleIds: postRoleKeys } })
  }
  const columnsOrg = [{
    title: '岗位名称',
    dataIndex: 'name',
    render: (text, record) => {
      const type = record.nodeType === 1 ? '' : 'user'
      let className = record.nodeType === 1 ? '' : 'table-tree-selectable'
      className = record.id === postId ? `${className} table-tree-selected` : className
      return <span className={className}><Icon type={type} />{text}</span>
    },
    onCellClick: (record, e) => {
      console.log(e.target)
      if (record.nodeType === 1) {
        dispatch({ type: 'post/onCellClick', payload: { postId: '', postRoleKeys: [] } })
        return
      }
      dispatch({ type: 'post/queryRolesByPostId', payload: record })
    },
  }, {
    title: '操作',
    dataIndex: 'operation',
    render: (text, record) => {
      let children
      if (record.nodeType === 1) {
        children = (<div className="table-operation">
          {isLimited(currentLevel, 'create') && <Button icon="folder-add" onClick={handlePostModal.bind(this, 'add', record)} title="新增岗位" />}
        </div>)
      } else {
        children = (<div className="table-operation">
          <Popconfirm title="确认删除吗?" onConfirm={onDelete.bind(this, record)}>
            {isLimited(currentLevel, 'delete') && <Button icon="delete" />}
          </Popconfirm>
          {isLimited(currentLevel, 'update') && <Button icon="edit" onClick={handlePostModal.bind(this, 'edit', record)} title="编辑岗位" />}
        </div>)
      }
      return children
    },
  },
  ]
  const columnsRole = [{
    title: '角色名称',
    dataIndex: 'roleName',
    key: 'id',
  }, {
    title: '角色描述',
    dataIndex: 'roleDesc',
  }, {
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
  const onChangeRole = (keys) => {
    dispatch({ type: 'post/onChangeRole', payload: { postRoleKeys: keys } })
  }
  const rowSelection = {
    selectedRowKeys: postRoleKeys,
    onChange: (selectedRowKeys, selectedRows) => {
      onChangeRole(selectedRowKeys, selectedRows)
    },
  }
  return (
    <Row className="custom-style">
      <Col span={9}>
        <Card title={<span><Iconfont type="shu" /> 岗位管理</span>}>
          <Table pagination={false} className="table-tree" size="small" columns={columnsOrg} dataSource={postData} rowKey="id" />
        </Card>
      </Col>
      <Col span={14} offset={1}>
        <Card title={<span><Iconfont type="shenfen-tianchong" /> 角色列表</span>} extra={isLimited(currentLevel, 'create') && <Button loading={loading} type="primary" onClick={saveRoleLimit.bind(this, 'add')}>保存岗位角色</Button>}>
          <Table columns={columnsRole} dataSource={roleData} size="small" rowSelection={rowSelection} pagination={false} rowKey="id" />
        </Card>
      </Col>
      <PostModal {...post} handleModal={handlePostModal.bind(this)} dispatch={dispatch} />
    </Row>
  )
}

Post.propTypes = {
  form: PropTypes.isRequired,
  post: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  app: PropTypes.object,
}

export default connect(({ post, app }) => ({ post, app }))(Post)
