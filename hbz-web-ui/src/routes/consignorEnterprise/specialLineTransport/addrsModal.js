import React from 'react'
import { Modal, Form, Input, Select, InputNumber } from 'antd'
import PropTypes from 'prop-types'


const FormItem = Form.Item
const itemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15 },
}

const EditForm = (param) => {
  const { dispatch, visible, form, userAddrsList } = param
  const { getFieldDecorator, validateFields } = form
  const hideModal = () => {
    dispatch({ type: 'specialLineTransport/changeStates', payload: { visible: false } })
  }
  const handleCancel = () => {
    form.resetFields()
    hideModal()
  }
  const handleOk = () => {
    validateFields((err, formValues) => {
      if (err) {
        return
      }
      dispatch({ type: 'specialLineTransport/editVendorService', payload: { values: formValues, form } })
    })
  }

  const modalProps = {
    title: '地址',
    visible,
    maskClosable: false,
    confirmLoading: false,
    onOk: handleOk,
    onCancel: handleCancel,
  }
  return (
    <Modal {...modalProps} >
      <Form>
        <FormItem label="地址" {...itemLayout}>
          {getFieldDecorator('vendorServiceName', {
            rules: [{ required: true, message: '地址' }],
          })(
            <Input placeholder="地址" />
          )}
        </FormItem>
      </Form>
    </Modal>
  )
}

const EditModal = Form.create()(EditForm)

EditForm.propTypes = {
  form: PropTypes.obj,
  jobvendor: PropTypes.obj,
  dispatch: PropTypes.func,
  vendorId: PropTypes.number,
}

export default EditModal
