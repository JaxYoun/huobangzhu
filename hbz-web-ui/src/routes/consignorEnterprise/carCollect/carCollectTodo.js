import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Select } from 'antd'
import { Iconfont } from 'components'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option

const CarCollectTodo = ({
  dispatch,
  app,
  carCollectTodo,
  form,
}) => {
  const { tableData } = app
  const { driverType, capital, driverLevel, security } = tableData
  const { id } = carCollectTodo
  const { getFieldDecorator } = form
  const goPay = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.orderId = id
      dispatch({ type: 'carCollectTodo/saveCollect', payload: formValue })
    })
  }
  return (
    <div>
      <Form>
        <Card title={<div className={styles.form}><Iconfont type="honor" className={styles.icon} /><span>资质要求</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="司机类型">
                {getFieldDecorator('need', {
                  rules: [
                    {
                      required: true,
                      message: '请选择司机类型',
                    },
                  ],
                })(
                  <Select placeholder="请选择司机类型">
                    {
                      driverType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="注册资金">
                {getFieldDecorator('registryMoney', {
                  rules: [
                    {
                      required: true,
                      message: '请选择注册资金',
                    },
                  ],
                })(
                  <Select placeholder="请选择注册资金">
                    {
                      capital.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="baozhengjin" className={styles.icon} /><span>保证金要求</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="保证金额度">
                {getFieldDecorator('bond', {
                  rules: [
                    {
                      required: true,
                      message: '请选择保证金额度',
                    },
                  ],
                })(
                  <Select placeholder="请选择保证金额度">
                    {
                      security.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="xinyu" className={styles.icon} /><span>司机信誉</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="信誉等级">
                {getFieldDecorator('starLevel', {
                  rules: [
                    {
                      required: true,
                      message: '请选择信誉等级',
                    },
                  ],
                })(
                  <Select placeholder="请选择信誉等级">
                    {
                      driverLevel.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card>
          <Row className={styles['pay-button']}>
            <Col>
              <Button type="primary" onClick={goPay}>去 付 款</Button>
            </Col>
          </Row>
        </Card>
      </Form>
    </div>
  )
}
CarCollectTodo.propTypes = {
  form: PropTypes.isRequired,
  carCollectTodo: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const CarCollectTodoForm = Form.create()(CarCollectTodo)
export default connect(({ carCollectTodo, app }) => ({ carCollectTodo, app }))(CarCollectTodoForm)
