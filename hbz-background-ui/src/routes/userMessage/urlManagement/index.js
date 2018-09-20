import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Button, Switch, Popconfirm } from 'antd'
import { Ttable } from 'components'
import Filter from './filter'
import DetailModal from './detailModal'
import AuthorityModaL from './authorityModal'

const UrlManagement = ({ loading, dispatch, urlManagement }) => {
  const { fields, tableTime, detailModal, authorityModal } = urlManagement
  const switchActive = (id, checked) => {
    dispatch({
      type: 'urlManagement/updateRecord',
      payload: { id, state: checked ? 1 : 0 },
    })
  }
  const editModal = (record) => {
    console.log(record)
    dispatch({
      type: 'urlManagement/editModal',
      payload: record,
    })
  }
  const deleteRecord = record => {
    dispatch({
      type: 'urlManagement/deleteRecord',
      payload: { id: record.id },
    })
  }
  let tableProps = {
    columns: [
      {
        title: '编号',
        dataIndex: 'id',
      },
      {
        title: '资源名称',
        dataIndex: 'urlLabel',
      },
      {
        title: '资源包路径',
        dataIndex: 'pack',
      },
      {
        title: '资源URL地址',
        dataIndex: 'urlPattern',
      },
      {
        title: '资源状态',
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
              <Button onClick={editModal.bind(this, record)}>
                编辑
              </Button>
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
    sourceUrl: '/URL/queryPage',
    fields: fields || {},
    tableTime,
  }
  const authorityModalProps = {
    dispatch,
    loading,
    authorityModal,
  }
  const detailModalProps = {
    detailModal,
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
      {authorityModal.visible && <AuthorityModaL {...authorityModalProps} />}
    </div>
  )
}

UrlManagement.propTypes = {
  dispatch: PropTypes.func.isRequired,
  urlManagement: PropTypes.obj,
  loading: PropTypes.bool,
}
export default connect(({ urlManagement, loading }) => ({
  urlManagement,
  loading,
}))(UrlManagement)
