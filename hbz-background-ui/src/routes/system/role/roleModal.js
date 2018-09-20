import React from 'react'
import { Modal, Form, Input } from 'antd'
import PropTypes from 'prop-types'
const FormItem = Form.Item

const roleForm = ({
  dispatch,
  roleModal,
  form,
}) => {
  const { getFieldDecorator } = form
  const { ModalTitle, visible, confirmLoading, values } = roleModal
  const { roleDesc, roleName, id } = values
  const handleCancel = () => {
    dispatch({
      type: 'role/handleModal',
      payload: 'hide',
    })
    form.resetFields()
  }
  const handleOk = () => {
    form.validateFields((err, value) => {
      if (err) {
        return
      }
      dispatch({
        type: 'role/save',
        payload: { values: value, form },
      })
    })
  }
  return (
    <Modal
      title={ModalTitle}
      visible={visible}
      maskClosable={false}
      confirmLoading={confirmLoading}
      onOk={handleOk.bind(this)}
      onCancel={handleCancel.bind(this)}
    >
      <Form layout="vertical">
        <FormItem label="角色名称">
          {getFieldDecorator('roleName', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: roleName,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="角色描述">
          {getFieldDecorator('roleDesc', {
            rules: [{ max: 100, message: '输入内容请限制在100字以内' }],
            initialValue: roleDesc,
          })(
            <Input type="textarea" />
          )}
        </FormItem>
        <FormItem>
          {getFieldDecorator('id', {
            initialValue: id,
          })(
            <Input type="hidden" />
          )}
        </FormItem>
      </Form>
    </Modal>
  )
}

const roleModal = Form.create()(roleForm)

roleForm.propTypes = {
  form: PropTypes.obj,
  ModalTitle: PropTypes.string,
  roleModal: PropTypes.obj,
  queryRoleList: PropTypes.func,
  dispatch: PropTypes.func,
}

export default roleModal
