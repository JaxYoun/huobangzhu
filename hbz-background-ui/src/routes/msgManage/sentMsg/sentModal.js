import React from 'react'
import { Modal, Form, Col, Row, Input, Select, DatePicker } from 'antd'
import PropTypes from 'prop-types'
import moment from 'moment'
import styles from './index.less'

const FormItem = Form.Item
const { TextArea } = Input

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
const formItemLayouts = {
  labelCol: {
    xs: { span: 3 },
    sm: { span: 3 },
  },
  wrapperCol: {
    xs: { span: 21 },
    sm: { span: 21 },
  },
}

const SentModal = ({
  visible,
  sendMsgDetali,
  hideModal,
  form,
}) => {
  const { getFieldDecorator } = form
  return (
    <div>
      <Modal
        title="消息详情"
        visible={visible}
        onCancel={hideModal}
        className={styles.addModal}
        width={900}
        onOk={hideModal}
      >
        <Form>
          <Row>
            <Col span={11}>
              <FormItem label="是否已发送" {...formItemLayout}>
                {getFieldDecorator('ifRead', {
                  initialValue: sendMsgDetali && sendMsgDetali.ifRead,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="消息阅读时间" {...formItemLayout}>
                {getFieldDecorator('lastUpdateDate', {
                  initialValue: sendMsgDetali && moment(sendMsgDetali.lastUpdateDate).format('YYYY-MM-DD HH:mm:ss'),
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="消息类型" {...formItemLayout}>
                {getFieldDecorator('messageType', {
                  initialValue: sendMsgDetali && sendMsgDetali.sitePushMessage.messageType.name,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="接收人设置" {...formItemLayout}>
                {getFieldDecorator('consumerType', {
                  initialValue: sendMsgDetali && sendMsgDetali.sitePushMessage.consumerType.name,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="接收人手机" {...formItemLayout}>
                {getFieldDecorator('consumerPhoneNo', {
                  initialValue: sendMsgDetali && sendMsgDetali.sitePushMessage.consumerPhoneNo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="消息发送时间" {...formItemLayout}>
                {getFieldDecorator('sendTime', {
                  initialValue: sendMsgDetali && moment(sendMsgDetali.sitePushMessage.sendTime).format('YYYY-MM-DD HH:mm:ss'),
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="消息小图片" {...formItemLayout}>
                {getFieldDecorator('imagePath', {
                  initialValue: sendMsgDetali && sendMsgDetali.sitePushMessage.imagePath,
                })(
                  <img src={sendMsgDetali.sitePushMessage.imagePath} alt="图片" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="消息详情" {...formItemLayouts}>
                {getFieldDecorator('content', {
                  initialValue: sendMsgDetali && sendMsgDetali.sitePushMessage.content,
                })(
                  <TextArea rows={6} disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('remark', {
                  initialValue: sendMsgDetali && sendMsgDetali.sitePushMessage.remark,
                })(
                  <TextArea rows={6} disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}

SentModal.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  visible: PropTypes.array,
  sendMsgDetali: PropTypes.object,
  hideModal: PropTypes.func,
}
const SentModalForm = Form.create()(SentModal)
export default SentModalForm
