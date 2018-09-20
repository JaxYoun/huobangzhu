import React from 'react'
import { Modal, Form, Col, Row, Input, Button } from 'antd'
import PropTypes from 'prop-types'
import styles from '../index.less'
const FormItem = Form.Item
const formItemLayout = {
  labelCol: {
    xs: { span: 6 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 18 },
    sm: { span: 18 },
  },
}
const { TextArea } = Input
const ExamineModal = ({
  visible,
  firmData,
  form,
  dispatch,
  loading,
  type,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'storageExamine/updatefirmModal', payload: { loading: true } })
      formValue.id = firmData.id
      formValue.type = type
      dispatch({ type: 'storageExamine/updateExamine', payload: formValue })
      form.resetFields()
    })
  }
  // 关闭模态框
  const hideModal = () => {
    form.resetFields()
    dispatch({ type: 'storageExamine/cleanModal' })
  }
  return (
    <div>
      <Modal
        title={'仓储审核'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={600}
        footer={null}
      >
        <Form>
          <Row>
            <Col span={24}>
              <FormItem label="审核备注" {...formItemLayout}>
                {getFieldDecorator('recordComment', {
                  rules: [{ required: true, message: '请填写审核备注小于100个字符', max: 100 }],
                })(
                  <TextArea row={4} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>确定</Button>
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
ExamineModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
  type: PropTypes.string,
}
const ExamineModalForm = Form.create()(ExamineModal)
export default ExamineModalForm
