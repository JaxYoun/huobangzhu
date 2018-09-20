import React from 'react'
import { Card, Button, Popconfirm, Form, Row, Col, Select, Input } from 'antd'
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

const RuleQuery = ({
  dispatch,
  form,
  adjusts,
  states,
  types,
}) => {
  const { getFieldDecorator } = form
  console.log(adjusts, states, types)
  const queryRule = () => {
    const value = form.getFieldsValue()
    dispatch({
      type: 'ruleManage/changeStates',
      payload: {
        fields: value,
        parent: 'ruleConfigure',
      },
    })
  }
  const rest = () => {
    form.resetFields()
  }
  return (
    <div>
      <Card title="规则配置">
        <Form>
          <Row>
            <Col {...queryCol}>
              <FormItem label="规则名称" {...formItemLayout}>
                {getFieldDecorator('name', {
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="状态" {...formItemLayout}>
                {getFieldDecorator('formulaState', {
                })(
                  <Select>
                    {
                      states.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="规则类型" {...formItemLayout}>
                {getFieldDecorator('formulaType', {
                })(
                  <Select>
                    {
                      types.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="适应业务" {...formItemLayout}>
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
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={queryRule.bind(this)}>查询</Button>
            <Button type="primary" onClick={rest.bind(this)}>重置</Button>
          </Row>
        </Form>
      </Card>
    </div>
  )
}

RuleQuery.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  adjusts: PropTypes.array,
  states: PropTypes.array,
  types: PropTypes.array,
}
const RuleQueryForm = Form.create()(RuleQuery)
export default RuleQueryForm
