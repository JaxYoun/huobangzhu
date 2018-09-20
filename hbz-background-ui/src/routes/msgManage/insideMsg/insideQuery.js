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

const InsideQuery = ({
  dispatch,
  form,
  msgType,
  platType,
  sendType,
}) => {
  const { getFieldDecorator } = form
  const queryInsideMsg = () => {
    const value = form.getFieldsValue()
    dispatch({
      type: 'insideMsg/changeStates',
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
      <Card title="站内消息管理">
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
                {getFieldDecorator('ifSend', {
                })(
                  <Select>
                    <Option value="0">否</Option>
                    <Option value="1">是</Option>
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="接收平台" {...formItemLayout}>
                {getFieldDecorator('receivePlatformType', {
                })(
                  <Select>
                    {
                      platType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="发送方式" {...formItemLayout}>
                {getFieldDecorator('pushType', {
                })(
                  <Select>
                    {
                      sendType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={queryInsideMsg.bind(this)}>查询</Button>
            <Button type="primary" onClick={rest.bind(this)}>重置</Button>
          </Row>
        </Form>
      </Card>
    </div>
  )
}

InsideQuery.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  msgType: PropTypes.array,
  platType: PropTypes.array,
  sendType: PropTypes.array,
}
const InsideQueryForm = Form.create()(InsideQuery)
export default InsideQueryForm
