import React from 'react'
import PropTypes from 'prop-types'
import { Modal, Icon, Col, Row, Button, message } from 'antd'
import TransportOrderList from './transportOrderList'
import TransportOrderQueryForm from './transportOrderQuery'
import CarQuery from './carQuery'
import CarList from './carList'
import ChooseModalForm from './chooseModal'
import UnChooseModalForm from './unchooseModal'
const HalfwayLoadModal = ({
  visible,
  form,
  dispatch,
  firmData,
  transportOrderHalfway,
  chooseHalfModal,
  unchooseHalfModal,
  carHalfway,
}) => {
  const { chooseData } = transportOrderHalfway
  const { alreadyChooseData } = carHalfway
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'carStart/cleanHalfModal' })
  }
  // 装车的方法
  const loadGoods = () => {
    if (chooseData[0]) {
      dispatch({ type: 'carStart/updateChooseHalfModal', payload: { visible: true } })
    } else {
      message.error('请先选择要添加的数据')
    }
  }
  // 取消的方法
  const unloadGoods = () => {
    if (alreadyChooseData[0]) {
      dispatch({ type: 'carStart/updateunChooseHalfModal', payload: { visible: true } })
    } else {
      message.error('请先选择要取消的数据')
    }
  }
  return (
    <div>
      <Modal
        title={'中途装车'}
        visible={visible}
        maskClosable={false}
        onCancel={hideModal}
        width={'100%'}
        footer={null}
      >
        <Row>
          <Col span={11} >
            <TransportOrderQueryForm {...transportOrderHalfway} dispatch={dispatch} />
            <TransportOrderList {...transportOrderHalfway} dispatch={dispatch} loadGoods={loadGoods} />
          </Col>
          <Col span={2} style={{ textAlign: 'center' }}>
            <Row style={{ marginTop: '35vh' }}>
              <Icon type="arrow-right" style={{ fontSize: 20 }} />
            </Row>
            <Row style={{ marginTop: '3vh' }}>
              <Button type="primary" onClick={loadGoods.bind(this)}>装车</Button>
            </Row>
            <Row style={{ marginTop: '5vh' }}>
              <Icon type="arrow-left" style={{ fontSize: 20 }} />
            </Row>
            <Row style={{ marginTop: '3vh' }}>
              <Button type="primary" onClick={unloadGoods.bind(this)}>取消</Button>
            </Row>
          </Col>
          <Col span={11} >
            <CarQuery {...carHalfway} dispatch={dispatch} />
            <CarList {...carHalfway} dispatch={dispatch} />
          </Col>
        </Row>
      </Modal>
      {chooseHalfModal.visible && <ChooseModalForm {...chooseHalfModal} {...transportOrderHalfway} dispatch={dispatch} />}
      {unchooseHalfModal.visible && <UnChooseModalForm {...unchooseHalfModal} {...carHalfway} dispatch={dispatch} />}
    </div>
  )
}

HalfwayLoadModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  transportOrderHalfway: PropTypes.obj,
  loading: PropTypes.bool,
  carHalfway: PropTypes.obj,
  chooseHalfModal: PropTypes.obj,
  unchooseHalfModal: PropTypes.obj,
}
export default HalfwayLoadModal
