import React from 'react'
import { Form, Modal, Checkbox } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'

const CheckboxGroup = Checkbox.Group
const FormItem = Form.Item

const RoleModalForm = ({ form, dispatch, roleModal, loading }) => {
  const { id, nickName, telephone, roleList = [], userRole = [] } = roleModal
  const { getFieldDecorator } = form
  const onCancel = () => {
    dispatch({
      type: 'guestManagement/update',
      payload: { roleModal: { visible: false } },
    })
  }
  const onOk = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.id = id
      dispatch({
        type: 'guestManagement/makeRolesGuest',
        payload: formValue,
      })
    })
  }
  const checkboxOptions = roleList.map(item => ({
    label: item.roleName,
    value: item.id,
  }))
  const confirmLoading = () => {
    let state = false
    if (roleList.length < 1) {
      state = true
    } else {
      state = loading.global
    }
    return state
  }
  return (
    <Modal
      title="角色分配"
      visible
      maskClosable={false}
      onCancel={onCancel}
      onOk={onOk}
      className={styles.detailModal}
      confirmLoading={confirmLoading()}
    >
      <div className={styles.userinfo}>
        <p>昵称：{nickName}</p>
        <p>手机：{telephone}</p>
        <h2>选择角色：</h2>
        <FormItem>
          {getFieldDecorator('roleIds', {
            rules: [{ required: true, message: '请选择角色' }],
            initialValue: userRole,
          })(<CheckboxGroup options={checkboxOptions} className={styles.radioGroup} />)}
        </FormItem>
      </div>
    </Modal>
  )
}
RoleModalForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  roleModal: PropTypes.object,
  loading: PropTypes.object,
}
const RoleModal = Form.create()(RoleModalForm)
export default RoleModal
