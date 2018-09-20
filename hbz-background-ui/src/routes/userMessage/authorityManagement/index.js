import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Button, Switch, Popconfirm } from 'antd'
import { Ttable } from 'components'
import Filter from './filter'
import DetailModal from './detailModal'
import AuthorityModaL from './authorityModal'
import UrlModal from './urlModal'

const RoleManagement = ({ loading, dispatch, authorityManagement }) => {
  const {
    tableTime,
    detailModal,
    authorityModal,
    urlModal,
    selectedUrls,
  } = authorityManagement
  const makeAuthorities = record => {
    dispatch({
      type: 'authorityManagement/update',
      payload: {
        authorityModal: {
          visible: true,
          ...record,
        },
      },
    })
    dispatch({
      type: 'authorityManagement/queryUrl',
      payload: { authId: record.id },
    })
  }
  const switchActive = (id, checked) => {
    dispatch({
      type: 'authorityManagement/updateRecord',
      payload: { id, state: checked ? 1 : 0 },
    })
  }
  const deleteRecord = record => {
    dispatch({
      type: 'authorityManagement/deleteRecord',
      payload: { id: record.id },
    })
  }
  const editRecord = record => {
    dispatch({
      type: 'authorityManagement/update',
      payload: { detailModal: { visible: true, ...record } },
    })
  }
  const checkUrl = record => {
    dispatch({
      type: 'authorityManagement/update',
      payload: { urlModal: { visible: true, ...record } },
    })
  }
  let tableProps = {
    columns: [
      {
        title: '编号',
        dataIndex: 'id',
      },
      {
        title: '权限名称',
        dataIndex: 'details',
      },
      {
        title: '权限编码',
        dataIndex: 'authName',
      },
      {
        title: '权限状态',
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
                绑定资源
              </Button>
              <Button onClick={checkUrl.bind(this, record)}>查看资源</Button>
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
    sourceUrl: '/authority/queryPage',
    tableTime,
  }
  const authorityModalProps = {
    dispatch,
    loading,
    authorityModal,
    tableTime,
    selectedUrls,
  }
  const detailModalProps = {
    detailModal,
    dispatch,
    loading,
  }
  const urlModalProps = {
    urlModal,
    dispatch,
    loading,
  }
  return (
    <div>
      <Filter dispatch={dispatch} />
      <Card>
        <Ttable {...tableProps} />
      </Card>
      {detailModal.visible && <DetailModal {...detailModalProps} />}
      {urlModal.visible && <UrlModal {...urlModalProps} />}
      {authorityModal.visible && <AuthorityModaL {...authorityModalProps} />}
    </div>
  )
}

RoleManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  authorityManagement: PropTypes.obj,
  loading: PropTypes.bool,
}
export default connect(({ authorityManagement, loading }) => ({
  authorityManagement,
  loading,
}))(RoleManagement)
