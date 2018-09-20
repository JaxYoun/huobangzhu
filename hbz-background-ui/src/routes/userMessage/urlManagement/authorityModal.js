import React from 'react'
import { Form, Modal, Checkbox } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'

const CheckboxGroup = Checkbox.Group
const FormItem = Form.Item

const LimitModalForm = ({ form, dispatch, authorityModal, loading }) => {
  const {
    id,
    roleName,
    role,
    authorityList = [],
    roleAuthority = [],
  } = authorityModal
  const { getFieldDecorator } = form
  const onCancel = () => {
    dispatch({
      type: 'urlManagement/update',
      payload: { authorityModal: { visible: false } },
    })
  }
  const onOk = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.id = id
      dispatch({
        type: 'urlManagement/makeAuthorities',
        payload: formValue,
      })
    })
  }
  const checkboxOptions = authorityList.map(item => ({
    label: item.details,
    value: item.id,
  }))
  const confirmLoading = () => {
    let state = false
    if (authorityList.length < 1) {
      state = true
    } else {
      state = loading.global
    }
    return state
  }
  return (
    <Modal
      title="配置权限"
      visible
      maskClosable={false}
      onCancel={onCancel}
      onOk={onOk}
      className={styles.detailModal}
      confirmLoading={confirmLoading()}
    >
      <div className={styles.userinfo}>
        <p>角色名称：{roleName}</p>
        <p>角色编码：{role}</p>
        <h2>选择权限：</h2>
        <FormItem>
          {getFieldDecorator('authIds', {
            rules: [{ required: true, message: '请选择权限' }],
            initialValue: roleAuthority,
          })(<CheckboxGroup options={checkboxOptions} />)}
        </FormItem>
      </div>
    </Modal>
  )
}
LimitModalForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  authorityModal: PropTypes.object,
  loading: PropTypes.object,
}
const LimitModal = Form.create()(LimitModalForm)
export default LimitModal
