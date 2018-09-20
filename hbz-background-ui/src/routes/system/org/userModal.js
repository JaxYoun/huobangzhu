import React from 'react'
import { Modal, Form, Input, TreeSelect, InputNumber } from 'antd'
import PropTypes from 'prop-types'
import { changeKeyNames, regex } from 'utils'

const FormItem = Form.Item

const userForm = ({
  userModal,
  currentOrg,
  orgData,
  handleModal,
  dispatch,
  form,
}) => {
  const { ModalTitle, visible, values = {}, confirmLoading } = userModal
  const { getFieldDecorator } = form
  const { id, userName, loginName, mobilePhone, email, type, orgId } = values
  orgData = changeKeyNames(orgData, { value: 'id', key: 'id', label: 'orgName' })

  const handleCancel = () => {
    form.resetFields()
    handleModal('hide')
  }
  const handleOk = () => {
    form.validateFields((err, formValues) => {
      if (err) {
        return
      }
      formValues.id = formValues.id || null
      dispatch({
        type: 'org/confirmLoading',
        payload: 'userModal',
      })
      dispatch({ type: 'org/saveUser', payload: { values: formValues, form } })
    })
  }

  return (
    <Modal
      title={ModalTitle}
      visible={visible}
      confirmLoading={confirmLoading}
      onCancel={handleCancel}
      onOk={handleOk}
    >
      <Form layout="vertical">
        <FormItem label="机构名称" >
          {getFieldDecorator('orgId', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: currentOrg || orgId,
          })(
            <TreeSelect
              showSearch
              treeDataSimpleMode
              treeNodeFilterProp="label"
              treeData={orgData}
            />
          )}
        </FormItem>
        <FormItem label="用户姓名" hasFeedback>
          {getFieldDecorator('userName', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: userName,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="登录账号" hasFeedback>
          {getFieldDecorator('loginName', {
            rules: [{ required: true, message: '请填写此项' },
            { pattern: regex.loginname, message: '只能输入以字母开头、可带数字、“_”、“.”的4-15位账号 ' }],
            initialValue: loginName,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="类型">
          {getFieldDecorator('type', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: type,
          })(
            <InputNumber min={0} />
          )}
        </FormItem>
        <FormItem label="邮箱账号" hasFeedback>
          {getFieldDecorator('email', {
            rules: [
              { required: true, message: '请填写此项' },
              { type: 'email', message: '请输入正确的邮箱地址' },
            ],
            initialValue: email,
          })(
            <Input type="email" />
          )}
        </FormItem>
        <FormItem label="电话号码" hasFeedback>
          {getFieldDecorator('mobilePhone', {
            rules: [{ pattern: regex.phone, message: '请输入正确的电话号码' }],
            initialValue: mobilePhone,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem className="hidden-input">
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

const UserModal = Form.create()(userForm)

userForm.propTypes = {
  form: PropTypes.obj,
  ModalTitle: PropTypes.string,
  handleModal: PropTypes.func,
  userModal: PropTypes.obj,
  dispatch: PropTypes.func,
  orgData: PropTypes.array,
  currentOrg: PropTypes.number,
}

export default UserModal
