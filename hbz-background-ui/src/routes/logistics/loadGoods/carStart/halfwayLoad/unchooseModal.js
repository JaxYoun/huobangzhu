import React from 'react'
import { Modal, Form, Col, Row, InputNumber, Button, Icon } from 'antd'
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

const UnChooseModal = ({
  visible,
  firmData,
  form,
  dispatch,
  loading,
  alreadyChooseData,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'carStart/updateunChooseHalfModal', payload: { loading: true } })
      dispatch({ type: 'carStart/unloadHalfwayGoods', payload: formValue })
    })
  }
  // 关闭模态框
  const hideModal = () => {
    form.resetFields()
    dispatch({ type: 'carStart/updateunChooseHalfModal', payload: { visible: false, firmData: [], loading: false } })
  }
  // 全选
  const chooseAll = () => {
    form.setFieldsValue({ cancellations: alreadyChooseData[0].waybillQuantity })
  }
  return (
    <div>
      <Modal
        title={'选择数量'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={600}
        footer={null}
      >
        <Form>
          <Row>
            <Col span={18}>
              <FormItem label="取消件数" {...formItemLayout}>
                {getFieldDecorator('cancellations', {
                  rules: [{ required: true, message: '请填写装车件数为正整数', min: 0, pattern: RegExp('^[1-9]*[1-9][0-9]*$') }],
                })(
                  <InputNumber />
                )}
              </FormItem>
            </Col>
            <Col span={2} offset={1} >
              <Icon type="plus" onClick={chooseAll.bind(this)} style={{ fontSize: 15, marginTop: '20%' }} />
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>确定</Button>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
UnChooseModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
  alreadyChooseData: PropTypes.array,
}
const UnChooseModalForm = Form.create()(UnChooseModal)
export default UnChooseModalForm
