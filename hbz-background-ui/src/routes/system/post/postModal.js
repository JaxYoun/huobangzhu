import React from 'react'
import { Modal, Form, Input } from 'antd'
import PropTypes from 'prop-types'
const FormItem = Form.Item

const postForm = ({
  postModal,
  postDesc,
  handleModal,
  dispatch,
  form,
}) => {
  const { getFieldDecorator } = form
  const { ModalTitle, visible, values = {}, confirmLoading } = postModal
  let { name, pId = '-', id = '' } = values
  id = id ? id.split('-')[1] : id
  pId = pId.split('-')[1]
  const handleCancel = () => {
    form.resetFields()
    dispatch({ type: 'post/updatePostDesc' })
    handleModal('hide')
  }
  const handleOk = () => {
    form.validateFields((err, formValues) => {
      if (err) {
        return
      }
      dispatch({
        type: 'post/confirmLoading',
        payload: 'postModal',
      })
      dispatch({ type: 'post/save', payload: { values: formValues, form } })
      dispatch({ type: 'post/updatePostDesc' })
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
            initialValue: name,
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
        <FormItem>
          {getFieldDecorator('id', {
            initialValue: id,
          })(
            <Input type="hidden" />
          )}
        </FormItem>
        <FormItem>
          {getFieldDecorator('orgId', {
            initialValue: pId,
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
