import React from 'react'
import { Modal, Form, Input } from 'antd'
import PropTypes from 'prop-types'
const FormItem = Form.Item

const menuForm = ({
  dispatch,
  menuModal,
  form,
}) => {
  const { getFieldDecorator } = form
  const { ModalTitle, visible, confirmLoading, values } = menuModal
  const { menuName, pId, id, menuUrl, menuIcon, orderCode } = values
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
        type: 'role/saveMenuModal',
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
        <FormItem label="菜单名称">
          {getFieldDecorator('menuName', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: menuName,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="父级菜单">
          {getFieldDecorator('pId', {
            initialValue: pId,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="菜单链接">
          {getFieldDecorator('menuUrl', {
            initialValue: menuUrl,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="菜单图标">
          {getFieldDecorator('menuIcon', {
            initialValue: menuIcon,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="排序编号">
          {getFieldDecorator('orderCode', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: orderCode,
          })(
            <Input type="number" />
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

const menuModal = Form.create()(menuForm)

menuForm.propTypes = {
  form: PropTypes.obj,
  ModalTitle: PropTypes.string,
  handleModal: PropTypes.func,
  menuModal: PropTypes.obj,
  queryMenuList: PropTypes.func,
  dispatch: PropTypes.func,
}

export default menuModal
