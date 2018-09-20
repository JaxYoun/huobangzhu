import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Table, Card, Row, Col, Button, Icon, message } from 'antd'
import { arrayToTree, baseURL, request, isLimited } from 'utils'
import { Iconfont } from 'components'

const Limit = ({
  limit,
  app,
  dispatch,
}) => {
  const { currentLevel } = app
  let { postData, userData, userKeys, postId, loading, temp } = limit
  let postTree = arrayToTree(postData, 'id', 'pId')
  let userTree = arrayToTree(userData, 'id', 'pid')
  let isExpand = false
  if (userTree.length > 0 && postTree.length > 0) {
    isExpand = true
  }
  const columnsPost = [{
    title: '选择岗位',
    dataIndex: 'name',
    render: (text, record) => {
      const type = record.nodeType === 1 ? '' : 'user'
      let className = record.nodeType === 1 ? '' : 'table-tree-selectable'
      className = record.id === postId ? `${className} table-tree-selected` : className
      return <span className={className}><Icon type={type} /> {text}</span>
    },
    onCellClick: (record) => {
      dispatch({ type: 'limit/queryPostUsers', payload: record })
    },
  }]
  const columnsUser = [{
    title: '选择人员',
    dataIndex: 'name',
  },
  ]
  const rowSelection = {
    selectedRowKeys: userKeys,
    onChange: (selectedRowKeys, selectedRows) => {
      dispatch({ type: 'limit/onChangeUser', payload: { selectedRows } })
    },
    getCheckboxProps: record => ({
      disabled: record.nodeType === 1,    // 机构不可选择
    }),
  }
  const savePostUser = () => {
    if (postId.length < 1) {
      message.info('请先选择岗位！')
      return
    }

    const postid = postId.split('-')[1]
    let userIds = []
    for (let userid of userKeys) {
      userIds.push(userid.split('-')[1])
    }
    dispatch({ type: 'limit/savePostUser', payload: { postId: postid, userIds } })
  }
  return (
    <Row className="custom-style">
      <Col span={9}>
        <Card title={<span><Iconfont type="shu" /> 岗位列表</span>} >
          {isExpand ?
            <Table
              rowKey="id"
              className="table-tree"
              pagination={false}
              columns={columnsPost}
              dataSource={postTree}
              defaultExpandAllRows
            />
            : <div>暂无数据</div>
          }
        </Card>
      </Col>
      <Col span={14} offset={1}>
        <Card title={<span><Iconfont type="group_fill" /> 人员列表</span>} extra={
          isLimited(currentLevel, 'execute') &&
            <Button
              type="primary"
              loading={loading}
              onClick={savePostUser}
            >
            保存岗位人员
            </Button>
        }>
        {
          isExpand ?
            <Table
              rowKey="id"
              className="table-tree"
              columns={columnsUser}
              dataSource={userTree}
              rowSelection={rowSelection}
              pagination={false}
              defaultExpandAllRows
            />
          : <div>暂无数据</div>
        }
        </Card>
      </Col>
    </Row>
  )
}

Limit.propTypes = {
  limit: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  app: PropTypes.object,
}
export default connect(({ app, limit }) => ({ app, limit }))(Limit)
