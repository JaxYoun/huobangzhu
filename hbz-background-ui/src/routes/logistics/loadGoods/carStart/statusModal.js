import React from 'react'
import { Modal, Form, Col, Row, Input, Select, Button } from 'antd'
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
const StatusModalForm = ({
  visible,
  form,
  dispatch,
  firmData,
  loading,
  transitStateData,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'carStart/updatefirmModal', payload: { loading: true } })
      formValue.id = firmData.id
      dispatch({ type: 'carStart/updateStatus', payload: formValue })
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'carStart/cleanStatusModal' })
    form.resetFields()
  }
  return (
    <div>
      <Modal
        title={'发车状态修改'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={600}
        footer={null}
      >
        <Form>
          <Row>
            <Col>
              <FormItem label="在途状态" {...formItemLayout}>
                {getFieldDecorator('transitState', {
                  initialValue: firmData && firmData.transitState,
                  rules: [{ required: true, message: '请编辑状态', max: 50 }],
                })(
                  <Select style={{ width: '100%' }}>
                    {transitStateData && Object.keys(transitStateData).map(key => <Option key={key}>{transitStateData[key]}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <FormItem label="备注" {...formItemLayout}>
                {getFieldDecorator('remarks', {
                  initialValue: firmData && firmData.remarks,
                  rules: [{ required: true, message: '请填写备注', max: 50 }],
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>修改确认</Button>
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
StatusModalForm.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  loading: PropTypes.bool,
  transitStateData: PropTypes.array,
}
const StatusModal = Form.create()(StatusModalForm)
export default StatusModal
