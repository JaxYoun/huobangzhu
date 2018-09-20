import React from 'react'
import { Form, Input, Row, Col, Modal, Select } from 'antd'
import PropTypes from 'prop-types'

const FormItem = Form.Item
const Option = Select.Option

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
const RoleForm = ({ form, dispatch, roleModal, roleCode, loading }) => {
  const { getFieldDecorator } = form
  const { id, role, comments, roleName } = roleModal
  const onOk = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({
        type: 'roleManagement/addRole',
        payload: formValue,
      })
    })
  }
  const onCancel = () => {
    dispatch({
      type: 'roleManagement/update',
      payload: { roleModal: { visible: false } },
    })
    form.resetFields()
  }
  const roleOptions = () => {
    return Object.keys(roleCode).map(key => (
      <Option value={key}>{roleCode[key]}</Option>
    ))
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
          <FormItem label="角色名称" {...formItemLayout}>
            {getFieldDecorator('roleName', {
              initialValue: roleName,
              rules: [{ required: true, message: '请填写此项' }],
            })(<Input />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="角色编码" {...formItemLayout}>
            {getFieldDecorator('role', {
              initialValue: role,
              rules: [{ required: true, message: '请填写此项' }],
            })(<Select style={{ width: '100%' }}>{roleOptions()}</Select>)}
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
  roleModal: PropTypes.object,
  loading: PropTypes.bool,
  roleCode: PropTypes.object,
}
const RoleModal = Form.create()(RoleForm)
export default RoleModal
