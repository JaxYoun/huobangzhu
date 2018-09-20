import React from 'react'
import { Modal, Form, Input } from 'antd'
import PropTypes from 'prop-types'
const FormItem = Form.Item

const postForm = ({
  postModal,
  handleModal,
  dispatch,
  form,
}) => {
  const { getFieldDecorator } = form
  const { ModalTitle, visible, values = {}, confirmLoading } = postModal
  let { postName, postIds = [], id = '', postLevel, postDesc } = values
  const handleCancel = () => {
    form.resetFields()
    dispatch({ type: 'batchpost/updatePostDesc' })
    handleModal('hide')
  }
  const handleOk = () => {
    form.validateFields((err, formValues) => {
      if (err) {
        return
      }
      dispatch({
        type: 'batchpost/confirmLoading',
        payload: 'postModal',
      })
      dispatch({ type: 'batchpost/save', payload: { values: formValues, form } })
      dispatch({ type: 'batchpost/updatePostDesc' })
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
        <FormItem label="岗位名称">
          {getFieldDecorator('postName', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: postName,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="岗位描述">
          {getFieldDecorator('postDesc', {
            initialValue: postDesc,
          })(
            <Input />
          )}
        </FormItem>
        <FormItem label="岗位级别">
          {getFieldDecorator('postLevel', {
            rules: [{ required: true, message: '请填写此项' }],
            initialValue: postLevel,
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
        <FormItem>
          {getFieldDecorator('postIds', {
            initialValue: postIds,
          })(
            <Input type="hidden" />
          )}
        </FormItem>
      </Form>
    </Modal>
  )
}
const PostModal = Form.create()(postForm)
postForm.propTypes = {
  form: PropTypes.obj,
  postDesc: PropTypes.string,
  handleModal: PropTypes.func,
  postModal: PropTypes.obj,
  dispatch: PropTypes.func,
}

export default PostModal
