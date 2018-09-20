import React from 'react'
import PropTypes from 'prop-types'
import { Modal, Form, Input, message } from 'antd'
import { request } from 'utils'

const urlObj = {
  changeUserPassword: '/system/user/changeUserPassword',
}
const FormItem = Form.Item
const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 14 },
  },
}

class ModifyPwd extends React.Component {
  state = {
    maskClosable: false,
    confirmLoading: false,
    value: undefined,
  }
  onCancel = () => {
    let { handleModal, form } = this.props
    form.resetFields()
    handleModal('hide')
  }
  onSubmit = () => {
    let { handleModal, form, userId } = this.props
    form.validateFields((errors) => {
      if (errors) { return }
      let sendData = {
        ...form.getFieldsValue(),
      }
      sendData.id = userId
      this.setState({
        confirmLoading: true,
      })
      request(urlObj.changeUserPassword, {
        body: JSON.stringify(sendData),
      }).then((data) => {
        setTimeout(() => {
          data.success && handleModal('hide')
          this.setState({
            confirmLoading: false,
          })
        }, 100)
      })
    })
  }
  handleConfirmPassword = (rule, value, callback) => {
    const { getFieldValue } = this.props.form
    if (value && value !== getFieldValue('newPassword')) {
      callback('两次输入不一致！')
    }
    // Note: 必须总是返回一个 callback，否则 validateFieldsAndScroll 无法响应
    callback()
  }
  render () {
    const { visible, form: { getFieldDecorator } } = this.props
    let modalProps = {
      title: '修改密码',
      maskClosable: false,
      onOk: this.onSubmit,
      onCancel: this.onCancel,
      okText: '提交',
      cancelText: '取消',
      confirmLoading: this.state.confirmLoading,
      visible,
      key: visible,
      width: 400,
    }
    return (
      <div>
        <Modal {...modalProps} >
          <Form layout="horizontal">
            <FormItem label="旧密码:" hasFeedback {...formItemLayout}>
                {getFieldDecorator('oldPassword', {
                  rules: [
                    { required: true, message: '请输入旧密码' },
                  ], initialValue: '' })(<Input type="password" placeholder="旧密码" />)}
            </FormItem>
            <FormItem label="新密码:" hasFeedback {...formItemLayout}>
                {getFieldDecorator('newPassword', {
                  rules: [
                    { required: true, message: '请输入新密码' },
                    { min: 6, message: '新密码不能小于6位' },
                    { max: 16, message: '新密码不能大于16位' },
                  ], initialValue: '' })(<Input type="password" placeholder="新密码" />)}
            </FormItem>
            <FormItem label="确认密码:" hasFeedback {...formItemLayout}>
                {getFieldDecorator('confirmPassword', {
                  rules: [
                    { required: true, message: '请输入确认密码' },
                    { min: 6, message: '新密码不能小于6位' },
                    { max: 16, message: '重复密码字数不能大于16位' },
                    { validator: this.handleConfirmPassword },
                  ], initialValue: '' })(<Input type="password" placeholder="确认密码" />)}
            </FormItem>
          </Form>
        </Modal>
      </div>
    )
  }
}

const ModifyPwdForm = Form.create()(ModifyPwd)

ModifyPwd.propTypes = {
  visible: PropTypes.bool,
  modalData: PropTypes.object,
  form: PropTypes.object,
  modalType: PropTypes.string,
  handleModal: PropTypes.func,
  treeArr: PropTypes.object,
  userId: PropTypes.string,
}
export default ModifyPwdForm
