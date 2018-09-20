import React from 'react'
import { Modal, Form, Col, Row, Input, DatePicker, Button } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'
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

const AddExpressForEachModal = ({
  visible,
  firmData,
  form,
  dispatch,
  loading,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'expressOrder/updateAddExpressForEachModal', payload: { loading: true } })
      formValue.sendTime = new Date(formValue.sendTime).getTime()
      formValue.id = firmData.exId
      formValue.trackingNumber = firmData.trackingNumber
      dispatch({ type: 'expressOrder/addExpressForEach', payload: formValue })
      form.resetFields()
    })
  }
  // 关闭新增模态框
  const hideModal = () => {
    form.resetFields()
    dispatch({ type: 'expressOrder/updateAddExpressForEachModal', payload: { visible: false, firmData: [] } })
    dispatch({ type: 'expressOrder/clean' })
  }
  return (
    <div>
      <Modal
        title={'快递物流详情新增'}
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
              <FormItem label="发生时间" {...formItemLayout}>
                {getFieldDecorator('sendTime', {
                  rules: [{ required: true, message: '请选择发生时间' }],
                })(
                  <DatePicker
                    showTime
                    format="YYYY-MM-DD HH:mm:ss"
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="物流描述" {...formItemLayout}>
                {getFieldDecorator('commodityDesc', {
                  rules: [{ required: true, message: '请填写物流描述小于50个字符', max: 50 }],
                })(
                  <Input />
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
AddExpressForEachModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
}
const ExpressModalForEachForm = Form.create()(AddExpressForEachModal)
export default ExpressModalForEachForm
