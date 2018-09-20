import React from 'react'
import { Card, Button, Form, Row, Col, Select, Input } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option

const formItemLayout = {
  labelCol: {
    xs: { span: 8 },
    sm: { span: 8 },
  },
  wrapperCol: {
    xs: { span: 16 },
    sm: { span: 16 },
  },
}
const queryCol = {
  sm: 8,
  md: 8,
  lg: 6,
  xl: 4,
}

const SentQuery = ({
  dispatch,
  form,
  msgType,
  receiveType,
}) => {
  const { getFieldDecorator } = form
  const querySendMsg = () => {
    const value = form.getFieldsValue()
    dispatch({
      type: 'sentMsg/changeStates',
      payload: {
        fields: value,
      },
    })
  }
  const rest = () => {
    form.resetFields()
  }
  return (
    <div>
      <Card>
        <Form>
          <Row>
            <Col {...queryCol}>
              <FormItem label="消息标题" {...formItemLayout}>
                {getFieldDecorator('title', {
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="消息类型" {...formItemLayout}>
                {getFieldDecorator('messageType', {
                })(
                  <Select>
                    {
                      msgType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="是否已发送" {...formItemLayout}>
                {getFieldDecorator('ifRead', {
                })(
                  <Select>
                    <Option value="0">未阅读</Option>
                    <Option value="1">已阅读</Option>
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="接收人设置" {...formItemLayout}>
                {getFieldDecorator('consumerType', {
                })(
                  <Select>
                    {
                      receiveType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={querySendMsg.bind(this)}>查询</Button>
            <Button type="primary" onClick={rest.bind(this)}>重置</Button>
          </Row>
        </Form>
      </Card>
    </div>
  )
}

SentQuery.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  msgType: PropTypes.array,
  receiveType: PropTypes.array,
}
const SentQueryForm = Form.create()(SentQuery)
export default SentQueryForm
