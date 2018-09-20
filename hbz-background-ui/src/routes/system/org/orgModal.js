import React from 'react'
import { Modal, Form, Input, TreeSelect, InputNumber } from 'antd'
import PropTypes from 'prop-types'
import { changeKeyNames } from 'utils'

const FormItem = Form.Item

const OrgForm = ({
  orgModal,
  currentOrg,
  orgData,
  handleModal,
  dispatch,
  form,
}) => {
  const { ModalTitle, visible, values = {}, confirmLoading } = orgModal
  const { getFieldDecorator } = form
  const { orgName, orderCode, id, orgLevel } = values
  orgData = changeKeyNames(orgData, { value: 'id', key: 'id', label: 'orgName' })
  for (let key of orgData.keys()) {
    if (orgData[key].id === id) {
      orgData[key].disabled = true
    }
  }
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
        payload: 'orgModal',
      })
      dispatch({ type: 'org/saveOrg', payload: { values: formValues, form } })
    })
  }
  return (
    <Modal
      title={ModalTitle}
      visible={visible}
      maskClosable={false}
      confirmLoading={confirmLoading}
      onOk={handleOk}
      onCancel={handleCancel}
    >
      <Form layout="vertical">
        <FormItem label="机构名称">
          {getFieldDecorator('orgName', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: orgName,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="上级机构">
          {getFieldDecorator('pId', {
            initialValue: currentOrg,
          })(
            <TreeSelect
              showSearch
              treeDataSimpleMode
              treeNodeFilterProp="label"
              treeData={orgData}
            />
          )}
        </FormItem>
        <FormItem label="机构级别">
          {getFieldDecorator('orgLevel', {
            initialValue: orgLevel,
          })(
            <Input type="number" />
          )}
        </FormItem>
        <FormItem label="排序编号">
          {getFieldDecorator('orderCode', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: orderCode,
          })(
            <InputNumber min={0} />
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

const OrgModal = Form.create()(OrgForm)

OrgForm.propTypes = {
  form: PropTypes.obj,
  ModalTitle: PropTypes.string,
  handleModal: PropTypes.func,
  orgModal: PropTypes.obj,
  queryOrgList: PropTypes.func,
  orgDataSimple: PropTypes.array,
  currentOrg: PropTypes.string,
  orgData: PropTypes.array,
  confirmLoading: PropTypes.bool,
  dispatch: PropTypes.func,
}

export default OrgModal
