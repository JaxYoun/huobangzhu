import React from 'react'
import { Form, Input, Row, Col, Modal } from 'antd'
import PropTypes from 'prop-types'
import { regex } from 'utils'
const FormItem = Form.Item
const formItemLayout = {
  labelCol: {
    xs: { span: 4 },
    sm: { span: 4 },
  },
  wrapperCol: {
    xs: { span: 16 },
    sm: { span: 16 },
  },
}
const queryCol = {
  span: 24,
}
const GuestForm = ({ form, dispatch, guestModal, loading }) => {
  const { getFieldDecorator } = form
  const { id, nickName, telephone, comments, avatar, login } = guestModal
  const onOk = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      let url = 'addGuest'
      if (formValue.id) {
        url = 'updateGuest'
      }
      dispatch({
        type: `guestManagement/${url}`,
        payload: formValue,
      })
    })
  }
  const onCancel = () => {
    dispatch({
      type: 'guestManagement/update',
      payload: { guestModal: { visible: false } },
    })
    form.resetFields()
  }
  return (
    <Modal
      title="客户管理"
      visible
      onCancel={onCancel}
      onOk={onOk}
      maskClosable={false}
      confirmLoading={loading.global}
    >
      <Row>
        <Col {...queryCol}>
          <FormItem label="登录名" {...formItemLayout}>
            {getFieldDecorator('login', {
              initialValue: login,
              rules: [{ required: true, message: '请填写此项' }],
            })(<Input />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="客户昵称" {...formItemLayout}>
            {getFieldDecorator('nickName', {
              initialValue: nickName,
              rules: [{ required: true, message: '请填写此项' }],
            })(<Input />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="电话号码" {...formItemLayout}>
            {getFieldDecorator('telephone', {
              initialValue: telephone,
              rules: [
                { required: true, message: '请填写此项' },
                { pattern: regex.phone, message: '请输入正确的电话号码' },
              ],
            })(<Input type="telephone" />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="客户头像" {...formItemLayout}>
            {getFieldDecorator('avatar', {
              initialValue: avatar,
            })(<Input />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="备注" {...formItemLayout}>
            {getFieldDecorator('comments', {
              initialValue: comments,
            })(<Input />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem>
            {getFieldDecorator('id', {
              initialValue: id,
            })(<Input type="hidden" />)}
          </FormItem>
        </Col>
      </Row>
    </Modal>
  )
}
GuestForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  guestModal: PropTypes.object,
  loading: PropTypes.bool,
}
const GuestModal = Form.create()(GuestForm)
export default GuestModal
