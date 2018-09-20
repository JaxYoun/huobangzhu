import React from 'react'
import { Form, Input, Row, Col, Modal } from 'antd'
import PropTypes from 'prop-types'
const FormItem = Form.Item
const formItemLayout = {
  labelCol: {
    xs: { span: 5 },
    sm: { span: 5 },
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
  const { id, urlPattern, comments, urlLabel, pack } = detailModal
  const onOk = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({
        type: 'urlManagement/addUrl',
        payload: formValue,
      })
    })
  }
  const onCancel = () => {
    dispatch({
      type: 'urlManagement/update',
      payload: { detailModal: { visible: false } },
    })
    form.resetFields()
  }
  return (
    <Modal
      title="资源管理"
      visible
      onCancel={onCancel}
      onOk={onOk}
      maskClosable={false}
      confirmLoading={loading.global}
    >
      <Row>
        <Col {...queryCol}>
          <FormItem label="资源名称" {...formItemLayout}>
            {getFieldDecorator('urlLabel', {
              initialValue: urlLabel,
              rules: [{ required: true, message: '请填写此项' }],
            })(<Input />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="资源包路径" {...formItemLayout}>
            {getFieldDecorator('pack', {
              initialValue: pack,
              rules: [{ required: true, message: '请填写此项' }],
            })(<Input />)}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="资源地址" {...formItemLayout}>
            {getFieldDecorator('urlPattern', {
              initialValue: urlPattern,
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
