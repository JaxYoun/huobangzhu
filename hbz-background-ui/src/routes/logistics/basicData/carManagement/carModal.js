import React from 'react'
import { Modal, Form, Col, Row, Input, Select, Button, DatePicker } from 'antd'
import PropTypes from 'prop-types'
import moment from 'moment'
import styles from '../index.less'
const FormItem = Form.Item
const { TextArea } = Input
const formItemLayout = {
  labelCol: {
    xs: { span: 6 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 18 },
    sm: { span: 18 },
  },
}
const formItemLayouts = {
  labelCol: {
    xs: { span: 3 },
    sm: { span: 3 },
  },
  wrapperCol: {
    xs: { span: 21 },
    sm: { span: 21 },
  },
}
const CarModalForm = ({
  visible,
  form,
  dispatch,
  firmData,
  carData,
  loading,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'carManagement/updatefirmModal', payload: { loading: true } })
      formValue.insuranceDate = new Date(formValue.insuranceDate).getTime()
      formValue.bigDate = new Date(formValue.bigDate).getTime()
      formValue.smallDate = new Date(formValue.smallDate).getTime()
      if (firmData.id) {
        formValue.id = firmData.id
        dispatch({ type: 'carManagement/updateCars', payload: formValue })
      } else {
        dispatch({ type: 'carManagement/addCars', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    form.resetFields()
    dispatch({ type: 'carManagement/cleanModal' })
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
  }
  return (
    <div>
      <Modal
        title={'车辆信息'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        footer={null}
      >
        <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
          {firmData.name && <span>商品编号：{firmData.wareNo}</span>}
        </Row>
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="车牌号" {...formItemLayout}>
                {getFieldDecorator('numberPlate', {
                  initialValue: firmData && firmData.numberPlate,
                  rules: [{ required: true,
                    message: '请填写正确的车牌号',
                    pattern: '^(([\u4e00-\u9fa5][a-zA-Z]|[\u4e00-\u9fa5]{2}\\d{2}|[\u4e00-\u9fa5]{2}[a-zA-Z])[-]?|([wW][Jj][\u4e00-\u9fa5]{1}[-]?)|([a-zA-Z]{2}))([A-Za-z0-9]{5}|[DdFf][A-HJ-NP-Za-hj-np-z0-9][0-9]{4}|[0-9]{5}[DdFf])$' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="车辆类型" {...formItemLayout}>
                {getFieldDecorator('vehicleType', {
                  initialValue: firmData && firmData.vehicleType,
                  rules: [{ required: true, message: '请选择车辆类型', max: 19 }],
                })(
                  <Select style={{ width: '100%' }} >
                  {carData && carData.map(item => <Option key={item.val}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="车主名称" {...formItemLayout}>
                {getFieldDecorator('ownersName', {
                  initialValue: firmData && firmData.ownersName,
                  rules: [{ required: true, message: '请填写车主名称' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="车主电话" {...formItemLayout}>
                {getFieldDecorator('ownersTelephone', {
                  initialValue: firmData && firmData.ownersTelephone,
                  rules: [{ required: true, message: '请填写正确的手机号码', pattern: RegExp('^0\\d{2,3}-?\\d{7,8}$|^(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])\\d{8}$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="车主证件号" {...formItemLayout}>
                {getFieldDecorator('ownerNumber', {
                  initialValue: firmData && firmData.ownerNumber,
                  rules: [{ required: true, message: '请填写正确的身份证号', pattern: RegExp('(^\\d{15}$)|(^\\d{17}([0-9]|X)$)') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="车主地址" {...formItemLayout}>
                {getFieldDecorator('ownerAddress', {
                  initialValue: firmData && firmData.ownerAddress,
                  rules: [{ required: true, message: '请填写地址' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="司机名称" {...formItemLayout}>
                {getFieldDecorator('driverName', {
                  initialValue: firmData && firmData.driverName,
                  rules: [{ required: true, message: '请填写司机名称' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="司机电话" {...formItemLayout}>
                {getFieldDecorator('driverTelephone', {
                  initialValue: firmData && firmData.driverTelephone,
                  rules: [{ required: true, message: '请填写正确的手机号码', pattern: RegExp('^0\\d{2,3}-?\\d{7,8}$|^(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])\\d{8}$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="司机证件号" {...formItemLayout}>
                {getFieldDecorator('driverNumber', {
                  initialValue: firmData && firmData.driverNumber,
                  rules: [{ required: true, message: '请填写正确的身份证号', pattern: RegExp('(^\\d{15}$)|(^\\d{17}([0-9]|X)$)') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="司机地址" {...formItemLayout}>
                {getFieldDecorator('driverAddress', {
                  initialValue: firmData && firmData.driverAddress,
                  rules: [{ required: true, message: '请填写地址' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="车长" {...formItemLayout}>
                {getFieldDecorator('carLength', {
                  initialValue: firmData && firmData.carLength,
                  rules: [{ required: true, message: '请填写车长' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="载重" {...formItemLayout}>
                {getFieldDecorator('cargoLoad', {
                  initialValue: firmData && firmData.cargoLoad,
                  rules: [{ required: true, message: '请填写载重' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="保险日期" {...formItemLayout}>
                {getFieldDecorator('insuranceDate', {
                  initialValue: (firmData && firmData.insuranceDate) ? moment(Number(firmData.insuranceDate)) : '',
                  rules: [{ required: true, message: '请填写保险日期' }],
                })(
                  <DatePicker style={{ width: '100%' }} />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="大检查日期" {...formItemLayout}>
                {getFieldDecorator('bigDate', {
                  initialValue: (firmData && firmData.bigDate) && moment(Number(firmData.bigDate)),
                  rules: [{ required: true, message: '请填写大检查日期' }],
                })(
                  <DatePicker style={{ width: '100%' }} />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="小验日期" {...formItemLayout}>
                {getFieldDecorator('smallDate', {
                  initialValue: (firmData && firmData.smallDate) && moment(Number(firmData.smallDate)),
                  rules: [{ required: true, message: '请填写小验日期' }],
                })(
                  <DatePicker style={{ width: '100%' }} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('remarks', {
                  initialValue: firmData && firmData.message,
                  rules: [{ required: false, message: '请填写商品简述' }],
                })(
                  <TextArea rows={2} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>确定</Button>
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
            {firmData.id ? '' : <Button type="danger" onClick={cleanFirm.bind(this)} >重置</Button>}
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
CarModalForm.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  carData: PropTypes.array,
  loading: PropTypes.bool,
}
const CarModal = Form.create()(CarModalForm)
export default CarModal

