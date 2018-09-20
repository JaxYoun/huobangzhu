import React from 'react'
import { Modal } from 'antd'
import PropTypes from 'prop-types'
import { Ttable } from 'components'
import styles from './index.less'

const UrlModal = ({ dispatch, tableTime, urlModal, loading }) => {
  const { details, authName, id } = urlModal
  const onCancel = () => {
    dispatch({
      type: 'authorityManagement/update',
      payload: { urlModal: { visible: false } },
    })
  }
  const onOk = () => {
    dispatch({
      type: 'authorityManagement/update',
      payload: { urlModal: { visible: false } },
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
    tableTime,
    sourceUrl: '/URL/queryPage',
    fields: { authId: id },
  }
  return (
    <Modal
      title="查看资源详情"
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
        <h2>已绑定的资源：</h2>
        <Ttable {...tableProps} />
      </div>
    </Modal>
  )
}
UrlModal.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  urlModal: PropTypes.object,
  loading: PropTypes.object,
  tableTime: PropTypes.string,
  selectedUrls: PropTypes.array,
}
export default UrlModal
