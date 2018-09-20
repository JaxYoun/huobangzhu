import React from 'react'
import { Modal } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import styles from './index.less'

const LimitModal = ({
  dispatch,
  tableTime,
  authorityModal,
  selectedUrls,
  loading,
}) => {
  const { id, details, authName, authorityUrl = [] } = authorityModal
  const onCancel = () => {
    dispatch({
      type: 'authorityManagement/update',
      payload: { authorityModal: { visible: false }, authorityUrl: null },
    })
  }
  const onOk = () => {
    dispatch({
      type: 'authorityManagement/makeAuth',
      payload: { id, urlIds: selectedUrls },
    })
  }
  const tableProps = {
    columns: [
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
    ],
    rowSelection: {
      selectedRowKeys: selectedUrls || authorityUrl,
      onChange: selectedRowKeys => {
        dispatch({
          type: 'authorityManagement/update',
          payload: { selectedUrls: selectedRowKeys },
        })
      },
    },
    tableTime,
    sourceUrl: '/URL/queryPage',
    rowKey: 'id',
  }
  console.log(authorityUrl, selectedUrls)
  return (
    <Modal
      title="配置权限"
      visible
      maskClosable={false}
      onCancel={onCancel}
      onOk={onOk}
      className={styles.detailModal}
      confirmLoading={loading.global}
    >
      <div className={styles.userinfo}>
        <p>权限名称：{details}</p>
        <p>权限编码：{authName}</p>
        <h2>选择资源：</h2>
        <Ttable {...tableProps} />
      </div>
    </Modal>
  )
}
LimitModal.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  authorityModal: PropTypes.object,
  loading: PropTypes.object,
  tableTime: PropTypes.string,
  selectedUrls: PropTypes.array,
}
export default LimitModal
