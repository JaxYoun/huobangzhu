import React from 'react'
import { Modal, Form, Icon, Row, Col, Input, InputNumber } from 'antd'
import PropTypes from 'prop-types'

const { TextArea } = Input
const FormTtem = Form.Item
const CodeModal = ({ visible, dispatch, form, formData, type }) => {
  let { getFieldDecorator, resetFields, validateFieldsAndScroll } = form
  // 点击取消
  const handleCancel = () => {
    resetFields()
    dispatch({ type: 'codeTable/hideModal' })
  }
  // 提交表单，根据按钮选择接口
  const handleSubmit = () => {
    validateFieldsAndScroll((errors, values) => {
      if (errors) {
        return
      }
      dispatch({ type: 'codeTable/saveCodeTable', payload: values })
    })
  }
  const formItemLayout = {
    labelCol: { span: 8 },
    wrapperCol: { span: 16 },
  }
  return (
    <Modal
      title={<div><Icon type="smile-o" />{ type === "add" ? '新增数据字典' : '编辑数据字典' }</div>}
      wrapClassName="vertical-center-modal"
      visible={visible}
      onCancel={handleCancel.bind(this)}
      onOk={handleSubmit.bind(this)}
      width={'700'}
    >
      <div>
        <Form>
          <Row>
            <Col span="12">
              <FormTtem {...formItemLayout} label="字典代码">
                {getFieldDecorator('dicCode', {
                  initialValue: formData && formData.dicCode,
                  rules: [
                    { required: true, message: '请输入字典代码!' },
                    { pattern: /\w+\.\w+\.\w+\.\d$/, message: '请正确输入字典编码!' },
                  ],
                })(
                  <Input placeholder="请输入字典代码" />
                )
                }
              </FormTtem>
            </Col>
            <Col span="12">
              <FormTtem {...formItemLayout} label="字典值">
                {getFieldDecorator('dicValue', {
                  initialValue: formData && formData.dicValue,
                  rules: [
                    { required: true, message: '请输入字典值!' },
                  ],
                })(
                  <Input placeholder="请输入字典值" />
                )
                }
              </FormTtem>
            </Col>
          </Row>
          <Row>
            <Col span="12">
              <FormTtem {...formItemLayout} label="排序值">
                {getFieldDecorator('orderCode', {
                  initialValue: formData && formData.orderCode,
                  rules: [{ required: true, message: '请输入排序值!' }],
                })(
                  <InputNumber placeholder="请输入排序值" min={0} />
                )
                }
              </FormTtem>
            </Col>
            <Col span="12">
              <FormTtem {...formItemLayout} label="字典备注">
                {getFieldDecorator('memo', {
                  initialValue: formData && formData.memo,
                  rules: [{ required: true, message: '请输入字典备注!' }],
                })(
                  <TextArea placeholder="请输入字典备注" autosize={{ minRows: 2, maxRows: 6 }} />
                )
                }
              </FormTtem>
            </Col>
          </Row>
        </Form>
      </div>
    </Modal>
  )
}
const CodeForm = Form.create()(CodeModal)
CodeModal.propTypes = {
  visible: PropTypes.bool.isRequired,
  formData: PropTypes.obj,
  type: PropTypes.obj,
  dispatch: PropTypes.func.isRequired,
  form: PropTypes.obj,
}
export default CodeForm
