import React from 'react'
import { Modal, Row, Col, Input } from 'antd'
import PropTypes from 'prop-types'


const LogisticsModal = ({ hideModal, visible, logisticsData }) => {
  const dataText = () => {
    let arr = []
    for (let key of logisticsData) {
      arr.push(<div>
        <Row>
          <Col span={20}>
            <span style={{ fontSize: '15px' }}>{key.information}</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
      </div>)
    }
    return arr
  }
  return (
    <Modal
      title={'物流详情'}
      visible={visible}
      onCancel={hideModal}
      onOk={hideModal}
      width={'700'}
      maskClosable={false}
    >
      <div>
      {logisticsData && dataText()}
      </div>
    </Modal>
  )
}
LogisticsModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  logisticsData: PropTypes.arry,
}
export default LogisticsModal
