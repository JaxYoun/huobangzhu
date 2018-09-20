import React from 'react'
import { Modal, Form, Col, Row, Input, Select, Button } from 'antd'
import PropTypes from 'prop-types'
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
const UserModalForm = ({
  visible,
  form,
  dispatch,
  firmData,
  getpackageData,
  loading,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'goodsManagement/updatefirmModal', payload: { loading: true } })
      if (firmData.id) {
        formValue.id = firmData.id
        formValue.commodityNumber = firmData.commodityNumber
        dispatch({ type: 'goodsManagement/updateGoods', payload: formValue })
      } else {
        dispatch({ type: 'goodsManagement/addGoods', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'goodsManagement/cleanModal' })
    form.resetFields()
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
  }

  return (
    <div>
      <Modal
        title={'货物信息'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        footer={null}
      >
        <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
          {firmData.id && <span>商品编号：{firmData.wareNo}</span>}
        </Row>
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="货物名称" {...formItemLayout}>
                {getFieldDecorator('commodityName', {
                  initialValue: firmData && firmData.commodityName,
                  rules: [{ required: true, message: '请填写货物名称', max: 30 }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="规格" {...formItemLayout}>
                {getFieldDecorator('specification', {
                  initialValue: firmData && firmData.specification,
                  rules: [{ required: true, message: '请填写正确的规格', pattern: RegExp('^[-+]?[0-9]+(\\.[0-9]+)?$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="包装单位" {...formItemLayout}>
                {getFieldDecorator('packageUnit', {
                  initialValue: firmData && firmData.packageUnit,
                  rules: [{ required: true, message: '请选择包装单位' }],
                })(
                  <Select>
                    {getpackageData.map(item => <Option key={item.val}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="体积" {...formItemLayout}>
                {getFieldDecorator('volume', {
                  initialValue: firmData && firmData.volume,
                  rules: [{ required: true, message: '请填写正确的体积', pattern: RegExp('^[-+]?[0-9]+(\\.[0-9]+)?$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="重量" {...formItemLayout}>
                {getFieldDecorator('weight', {
                  initialValue: firmData && firmData.weight,
                  rules: [{ required: true, message: '请填写正确的重量', pattern: RegExp('^[-+]?[0-9]+(\\.[0-9]+)?$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="价格" {...formItemLayout}>
                {getFieldDecorator('price', {
                  initialValue: firmData && firmData.price,
                  rules: [{ required: true, message: '请填写正确的价格', pattern: RegExp('^[-+]?[0-9]+(\\.[0-9]+)?$') }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="计费重量(KG)" {...formItemLayout}>
                {getFieldDecorator('billingWeight', {
                  initialValue: firmData && firmData.billingWeight,
                  rules: [{ required: true, message: '请填写计费重量' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="条码" {...formItemLayout}>
                {getFieldDecorator('barcode', {
                  initialValue: firmData && firmData.barcode,
                  rules: [{ required: true, message: '请填写条码' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="简拼" {...formItemLayout}>
                {getFieldDecorator('jianpin', {
                  initialValue: firmData && firmData.jianpin,
                  rules: [{ required: true, message: '请填写简拼' }],
                })(
                  <Input />
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
UserModalForm.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  getpackageData: PropTypes.array,
  loading: PropTypes.bool,
}
const UserModal = Form.create()(UserModalForm)
export default UserModal
