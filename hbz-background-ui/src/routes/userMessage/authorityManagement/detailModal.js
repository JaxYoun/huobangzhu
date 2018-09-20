import React from 'react'
import { Form, Input, Row, Col, Modal } from 'antd'
import PropTypes from 'prop-types'
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
const RoleForm = ({ form, dispatch, detailModal, loading }) => {
  const { getFieldDecorator } = form
  const { id, authName, comments, details } = detailModal
  const onOk = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({
        type: 'authorityManagement/addAuthority',
        payload: formValue,
      })
    })
  }
  const onCancel = () => {
    dispatch({
      type: 'authorityManagement/update',
      payload: { detailModal: { visible: false } },
    })
    form.resetFields()
  }
  return (
    <Modal
      title="角色管理"
      visible
      onCancel={onCancel}
      onOk={onOk}
      maskClosable={false}
      confirmLoading={loading.global}
    >
      <Row>
        <Col {...queryCol}>
          <FormItem label="权限名称" {...formItemLayout}>
            {getFieldDecorator('details', {
              initialValue: details,
              rules: [{ required: true, message: '请填写此项' }],
            })(<Input />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="权限编码" {...formItemLayout}>
            {getFieldDecorator('authName', {
              initialValue: authName,
              rules: [{ required: true, message: '请填写此项' }],
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
RoleForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  detailModal: PropTypes.object,
  loading: PropTypes.bool,
}
const RoleModal = Form.create()(RoleForm)
export default RoleModal
