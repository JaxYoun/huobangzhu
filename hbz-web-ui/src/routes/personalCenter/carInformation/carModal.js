import React from 'react'
import { Modal, Form, Input, Select, Row, Col } from 'antd'
import PropTypes from 'prop-types'
import { regex } from 'utils'
import CarSizeModal from './carSizeModal'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

const CarModal = ({
  visible,
  carDetail,
  hideModal,
  form,
  transTypes,
  loading,
  dispatch,
  selectCar,
  carSizeModal,
}) => {
  const { getFieldDecorator } = form
  const transTypeObj = []
  for (let key of Object.keys(transTypes)) {
    transTypeObj.push(<Option value={key}>{transTypes[key]}</Option>)
  }
  const showCarSizeModal = () => {
    dispatch({
      type: 'carInformation/showCarSizeModal',
    })
    let car = []
    if (selectCar.length > 0) {
      car = [...selectCar]
    } else {
      if (carDetail) {
        car = [...carDetail.transSizes]
      }
    }
    dispatch({
      type: 'carInformation/changeStates',
      payload: {
        selectCar: car,
      },
    })
  }
  const hideCarSizeModal = () => {
    dispatch({
      type: 'carInformation/updateModal',
      payload: {
        visible: false,
      },
    })
    dispatch({
      type: 'carInformation/changeStates',
      payload: {
        selectCar: [],
      },
    })
  }
  const carSizeModalProps = {
    ...carSizeModal,
    hideCarSizeModal,
    dispatch,
    selectCar,
  }
  const showCarSelect = () => {
    let car = []
    if (selectCar.length > 0) {
      car = selectCar
    } else {
      if (carDetail) {
        car = carDetail.transSizes
      }
    }
    return car.join(',')
  }
  const submitCar = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.transSizes = formValue.transSizes.split(',')
      if (carDetail) {
        formValue.id = carDetail.id
      }
      dispatch({
        type: 'carInformation/addOrChange',
        payload: formValue,
      })
    })
  }
  const show = showCarSelect()
  return (
    <Modal
      title={carDetail ? '车辆信息编辑' : '新增车辆'}
      visible={visible}
      onCancel={hideModal}
      confirmLoading={loading.global}
      onOk={submitCar}
    >
      <Form>
        <Row>
          <Col>
            <FormItem label="车牌号">
              {getFieldDecorator('plateNumber', {
                rules: [
                  {
                    required: true,
                    message: '请输入车牌号',
                  },
                ],
                initialValue: carDetail && carDetail.plateNumber,
              })(
                <Input placeholder="请输入车牌号" />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormItem label="车辆类型">
              {getFieldDecorator('transType', {
                rules: [
                  {
                    required: true,
                    message: '请选择车辆类型',
                  },
                ],
                initialValue: carDetail && carDetail.transType,
              })(
                <Select placeholder="请选择车辆类型">
                  {transTypeObj}
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormItem label="载重">
              {getFieldDecorator('maxLoad', {
                rules: [
                  {
                    required: true,
                    message: '请填写车辆最大载重',
                  },
                  { pattern: regex.number, message: '请输入正确的金额' },
                ],
                initialValue: carDetail && `${carDetail.maxLoad}`,
              })(
                <Input addonAfter={<span>吨</span>} placeholder="请填写车辆最大载重(整数或保留小数点1到2位)" />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormItem label="车长选择">
              {getFieldDecorator('transSizes', {
                rules: [
                  {
                    required: true,
                    message: '请选择车长',
                  },
                ],
                initialValue: show,
              })(
                <Input placeholder="点击选择车长" onClick={showCarSizeModal} addonAfter={<span>米</span>} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormItem label="车辆描述">
              {getFieldDecorator('vehicleDescription', {
                rules: [
                  {
                    required: true,
                    message: '请输入车辆描述',
                  },
                ],
                initialValue: carDetail && carDetail.vehicleDescription,
              })(
                <TextArea rows={4} placeholder="请输入车辆描述" />
              )}
            </FormItem>
          </Col>
        </Row>
      </Form>
      {
        carSizeModal.visible && <CarSizeModal {...carSizeModalProps} />
      }
    </Modal>
  )
}

CarModal.propTypes = {
  visible: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
  hideModal: PropTypes.func,
  carDetail: PropTypes.object,
  form: PropTypes.isRequired,
  loading: PropTypes.object,
  transTypes: PropTypes.array,
  carSizeModal: PropTypes.object,
  selectCar: PropTypes.array,
  showCar: PropTypes.array,
}

const CarModalForm = Form.create()(CarModal)
export default CarModalForm
