import React from 'react'
import { Card, Button, Popconfirm, Form, Row, Col, Select, Input, DatePicker } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option
const { RangePicker } = DatePicker

const formItemLayout = {
  labelCol: {
    xs: { span: 5 },
    sm: { span: 5 },
  },
  wrapperCol: {
    xs: { span: 19 },
    sm: { span: 19 },
  },
}

const dateFormat = 'YYYY-MM-DD HH:mm:ss'

const ReputationQuery = ({
  dispatch,
  adjusts,
  form,
}) => {
  const { getFieldDecorator } = form
  const queryReputation = () => {
    const value = form.getFieldsValue()
    if (value.time) {
      value.timeGT = value.time[0].format('YYYY-MM-DD HH:mm:ss')
      value.timeLT = value.time[0].format('YYYY-MM-DD HH:mm:ss')
      delete value.time
    }
    dispatch({
      type: 'ruleManage/changeStates',
      payload: {
        fields: value,
        parent: 'reputationQuery',
      },
    })
  }
  const rest = () => {
    form.resetFields()
  }
  return (
    <div>
      <Card title="积分记录查询">
        <Form>
          <Row>
            <Col span={7}>
              <FormItem label="规则名称" {...formItemLayout}>
                {getFieldDecorator('action', {
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col span={7} offset={1}>
              <FormItem label="适用业务" {...formItemLayout}>
                {getFieldDecorator('adjustType', {
                })(
                  <Select>
                    {
                      adjusts.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={7} offset={1}>
              <FormItem label="积分时间" {...formItemLayout}>
                {getFieldDecorator('time', {
                })(
                  <RangePicker format={dateFormat} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={queryReputation.bind(this)}>查询</Button>
            <Button type="primary" onClick={rest.bind(this)}>重置</Button>
          </Row>
        </Form>
      </Card>
    </div>
  )
}

ReputationQuery.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  adjusts: PropTypes.array,
}
const ReputationQueryForm = Form.create()(ReputationQuery)
export default ReputationQueryForm
