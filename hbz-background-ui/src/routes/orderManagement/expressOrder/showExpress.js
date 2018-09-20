import React from 'react'
import { Modal, Row, Col, Popconfirm, Button } from 'antd'
import PropTypes from 'prop-types'


const ShowExpress = ({ hideModal, visible, expressShowData, dispatch }) => {

  const deleteData = (id) => {
    dispatch({ type: 'expressOrder/deleteData', payload: { id } })
  }
  const dataText = () => {
    let arr = []
    for (let key of expressShowData) {
      arr.push(<div>
        <Row>
          <Col span={20}>
            <span style={{ fontSize: '15px' }}>{key.information}</span>
          </Col>
          <Col span={4}>
            <Popconfirm title="确定要删除吗？" onConfirm={deleteData.bind(this, key.id)}>
              <Button type="warning">删除</Button>
            </Popconfirm>
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
      {expressShowData && dataText()}
      </div>
    </Modal>
  )
}
ShowExpress.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  expressShowData: PropTypes.array,
}
export default ShowExpress
