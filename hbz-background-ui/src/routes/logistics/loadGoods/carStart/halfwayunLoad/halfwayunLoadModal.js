import React from 'react'
import PropTypes from 'prop-types'
import { Modal, Icon, Col, Row, Button, message } from 'antd'
import CarQuery from './carQuery'
import CarList from './carList'
import UnLoadList from './unLoadList'
const HalfwayunLoadModal = ({
  visible,
  form,
  dispatch,
  unLoadList,
  carHalfwayUnLoad,
}) => {
  const { chooseData } = unLoadList
  const { alreadyChooseData, firmData } = carHalfwayUnLoad
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'carStart/cleanUnLoadModal' })
  }
  // 卸货的方法
  const loadGoods = () => {
    if (alreadyChooseData[0]) {
      dispatch({ type: 'carStart/HalfwayGoodsUnLoad', payload: {} })
    } else {
      message.error('请先选择要卸货的数据')
    }
  }
  // 重置的方法
  const unloadGoods = () => {
    dispatch({ type: 'carStart/HalfwayGoodsReset', payload: { id: firmData.id } })
  }
  return (
    <div>
      <Modal
        title={'卸货管理'}
        visible={visible}
        maskClosable={false}
        onCancel={hideModal}
        width={'100%'}
        footer={null}
      >
        <Row>
          <Col span={11} >
            <UnLoadList {...unLoadList} dispatch={dispatch} />
          </Col>
          <Col span={2} style={{ textAlign: 'center' }}>
            <Row style={{ marginTop: '35vh' }}>
              <Icon type="arrow-left" style={{ fontSize: 20 }} />
            </Row>
            <Row style={{ marginTop: '3vh' }}>
              <Button type="primary" onClick={loadGoods.bind(this)}>整单卸货</Button>
            </Row>
            <Row style={{ marginTop: '5vh' }}>
              <Icon type="arrow-right" style={{ fontSize: 20 }} />
            </Row>
            <Row style={{ marginTop: '3vh' }}>
              <Button type="primary" onClick={unloadGoods.bind(this)}>重置</Button>
            </Row>
          </Col>
          <Col span={11} >
            <CarQuery {...carHalfwayUnLoad} dispatch={dispatch} />
            <CarList {...carHalfwayUnLoad} dispatch={dispatch} />
          </Col>
        </Row>
      </Modal>
    </div>
  )
}

HalfwayunLoadModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  transportOrderHalfway: PropTypes.obj,
  loading: PropTypes.bool,
  carHalfwayUnLoad: PropTypes.obj,
  chooseHalfModal: PropTypes.obj,
  unchooseHalfModal: PropTypes.obj,
  unLoadList: PropTypes.obj,
  firmData: PropTypes.obj,
}
export default HalfwayunLoadModal
