import React from 'react'
import { Modal, Form, Col, Row, Input, DatePicker, Select } from 'antd'
import PropTypes from 'prop-types'

const FormItem = Form.Item
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

const ExpressModal = ({
  visible,
  addData,
  form,
  dispatch,
  selectData,
}) => {
  let { expressCompanyType } = selectData
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.id = addData.id
      formValue.sendTime = new Date(formValue.sendTime).getTime()
      dispatch({ type: 'expressOrder/addExpress', payload: formValue })
      form.resetFields()
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'expressOrder/updateAddExpressModal', payload: { visible: false, addData: [] } })
    form.resetFields()
  }
  // 遍历数据
  const selectDataGet = (data) => {
    let arrays = []
    for (let key in data) {
      arrays.push(<Option key={key}>{data[key]}</Option>)
    }
    return arrays
  }
  expressCompanyType = selectDataGet(expressCompanyType)
  return (
    <div>
      <Modal
        title={'快递订单-快递详情'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={600}
      >
        <Form>
          <Row>
            <Col span={24}>
              <FormItem label="快递公司" {...formItemLayout}>
                {getFieldDecorator('expressCompanyType', {
                  rules: [{ required: true, message: '请选择快递公司' }],
                })(
                  <Select>
                    {expressCompanyType}
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="快递单号" {...formItemLayout}>
                {getFieldDecorator('trackingNumber', {
                  rules: [{ required: true, message: '请填写快递单号' }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="发件时间" {...formItemLayout}>
                {getFieldDecorator('sendTime', {
                  rules: [{ required: true, message: '请选择快递发件时间' }],
                })(
                  <DatePicker
                    showTime
                    format="YYYY-MM-DD HH:mm:ss"
                  />
                )}
              </FormItem>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
ExpressModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  selectData: PropTypes.obj,
  addData: PropTypes.obj,
}
const ExpressModalForm = Form.create()(ExpressModal)
export default ExpressModalForm
