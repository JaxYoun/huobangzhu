import React from 'react'
import PropTypes from 'prop-types'
import { Modal, Icon, Col, Row, Button, message } from 'antd'
import TransportOrderList from './transportOrderList'
import TransportOrderQueryForm from './transportOrderQuery'
import CarQueryForm from './carQuery'
import CarList from './carList'
import ChooseModalForm from './chooseModal'
import UnChooseModalForm from './unchooseModal'
const StartModal = ({
  visible,
  form,
  dispatch,
  firmData,
  transportOrder,
  chooseModal,
  unchooseModal,
  car,
}) => {
  const { chooseData } = transportOrder
  const { alreadyChooseData } = car
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'carStart/cleanModal' })
  }
  // 装车的方法
  const loadGoods = () => {
    if (chooseData[0]) {
      dispatch({ type: 'carStart/updateChooseModal', payload: { visible: true } })
    } else {
      message.error('请先选择要添加的数据')
    }
  }
  // 取消的方法
  const unloadGoods = () => {
    if (alreadyChooseData[0]) {
      dispatch({ type: 'carStart/updateunChooseModal', payload: { visible: true } })
    } else {
      message.error('请先选择要取消的数据')
    }
  }
  return ( 
    <div>
      <Modal
        title={firmData.id ? '编辑发车单' : '新建发车单'}
        visible={visible}
        maskClosable={false}
        onCancel={hideModal}
        width={'100%'}
        footer={null}
      >
        <Row>
          <Col span={11} >
            <TransportOrderQueryForm {...transportOrder} dispatch={dispatch} />
            <TransportOrderList {...transportOrder} dispatch={dispatch} loadGoods={loadGoods} />
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
            <CarQueryForm {...car} dispatch={dispatch} />
            <CarList {...car} dispatch={dispatch} />
          </Col>
        </Row>
      </Modal>
      {chooseModal.visible && <ChooseModalForm {...chooseModal} {...transportOrder} dispatch={dispatch} />}
      {unchooseModal.visible && <UnChooseModalForm {...unchooseModal} {...car} dispatch={dispatch} />}
    </div>
  )
}

StartModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  transportOrder: PropTypes.obj,
  loading: PropTypes.bool,
  car: PropTypes.obj,
  chooseModal: PropTypes.obj,
  unchooseModal: PropTypes.obj,
}
export default StartModal
