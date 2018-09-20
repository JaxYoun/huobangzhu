import React from 'react'
import PropTypes from 'prop-types'
import { Modal, Form, Input } from 'antd'
import { request } from 'utils'
const urlObj = {
  addOpinionFeedback: '/workbench/addOpinionFeedback',
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

class Feedback extends React.Component {
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
    let { handleModal, form } = this.props
    form.validateFields((errors) => {
      if (errors) { return }
      let sendData = {
        ...form.getFieldsValue(),
      }

      this.setState({
        confirmLoading: true,
      })
      request(urlObj.addOpinionFeedback, {
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

  render () {
    const { visible, form: { getFieldDecorator } } = this.props
    let modalProps = {
      title: '意见反馈',
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
            <FormItem label="反馈标题:" hasFeedback {...formItemLayout}>
                {getFieldDecorator('feedbackName', {
                  rules: [
                    { required: true, message: '请输入反馈标题' },
                    { max: 20, message: '标题字数不能大于20' },
                  ], initialValue: '' })(<Input />)}
            </FormItem>
            <FormItem label="反馈详情:" hasFeedback {...formItemLayout}>
                {getFieldDecorator('feedbackDetail', {
                  rules: [
                    { required: true, message: '请输入反馈详情' },
                    { max: 200, message: '反馈字数不能大于200' },
                  ], initialValue: '' })(<Input type="textarea" autosize={{ minRows: 2, maxRows: 20 }} />)}
            </FormItem>
          </Form>
        </Modal>
      </div>
    )
  }
}

const FeedbackForm = Form.create()(Feedback)

Feedback.propTypes = {
  visible: PropTypes.bool,
  modalData: PropTypes.object,
  form: PropTypes.object,
  modalType: PropTypes.string,
  handleModal: PropTypes.func,
  treeArr: PropTypes.object,
}
export default FeedbackForm
