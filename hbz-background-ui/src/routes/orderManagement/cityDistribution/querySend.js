import React from 'react'
import PropTypes from 'prop-types'
import moment from 'moment'
import { Card, Select, Form, DatePicker, Row, Col, Input, Button, Icon } from 'antd'
import styles from './index.less'
const { RangePicker } = DatePicker
const QuerySendForm = ({
  form,
  queryLineProps,
  dispatch,
}) => {
  const {
    provinceDataSend,
    cityDataSend,
    countyDataSend,
    selectData,
    sendCollapse,
  } = queryLineProps
  let {
    OrderTrans,
  } = selectData

  const FormItem = Form.Item
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
  const formItemLayoutDate = {
    labelCol: {
      xs: { span: 4 },
      sm: { span: 4 },
    },
    wrapperCol: {
      xs: { span: 20 },
      sm: { span: 20 },
    },
  }
  const queryCol = {
    sm: 8,
    md: 8,
    lg: 6,
    xl: 4,
  }
  const queryColdate = {
    sm: 16,
    md: 16,
    lg: 12,
    xl: 8,
  }
  const dateFormat = 'YYYY-MM-DD HH:mm:ss'
  const { getFieldDecorator } = form
  // 表单操作
  const queryScenes = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      for (let key in formValue) {
        if (key === 'dateTime') {
          if (formValue[key]) {
            formValue.smallTime = new Date(formValue[key][0].format('YYYY-MM-DD 00:00:00')).getTime()
            formValue.bigTime = new Date(formValue[key][1].format('YYYY-MM-DD 23:59:59')).getTime()
          }
        }
      }
      dispatch({ type: 'cityDistribution/update', payload: { sendfields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
    dispatch({ type: 'cityDistribution/update', payload: { cityData: '', countyData: '' } })
  }

  // 取货省市联动查询
  const handleProvinceFromChange = (value) => {
    // 判断是不是直辖市
    if (value === '1' || value === '2' || value === '3' || value === '4') {
      dispatch({ type: 'cityDistribution/updateCountySend', payload: { parentId: value } })
    } else {
      dispatch({ type: 'cityDistribution/updateCitySend', payload: { parentId: value } })
    }
    dispatch({ type: 'cityDistribution/update', payload: { cityData: '', countyData: '' } })
    form.resetFields(['cityId', 'countyId'])
  }
  const handleCityFromChange = (value) => {
    dispatch({ type: 'cityDistribution/update', payload: { countyData: '' } })
    form.resetFields(['countyId'])
    dispatch({ type: 'cityDistribution/updateCountySend', payload: { parentId: value } })
  }
  const selectDataGet = (data) => {
    let arrays = []
    for (let key in data) {
      arrays.push(<Option key={key}>{data[key]}</Option>)
    }
    return arrays
  }
  OrderTrans = selectDataGet(OrderTrans)
  // 查询样式
  const changeCollapse = () => {
    dispatch({ type: 'cityDistribution/update', payload: { sendCollapse: !sendCollapse } })
  }
  return (
    <div>
      <Card title="订单查询">
        <Form>
          <Row>
            <Col {...queryCol}>
              <FormItem label="订单编号" {...formItemLayout}>
                {getFieldDecorator('orderNo', {
                })(
                  <Input
                    onPressEnter={queryScenes.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="订单归属公司" {...formItemLayout}>
                {getFieldDecorator('organizationName', {
                })(
                  <Input
                    onPressEnter={queryScenes.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="订单创建人" {...formItemLayout}>
                {getFieldDecorator('createUser', {
                })(
                  <Input
                    onPressEnter={queryScenes.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="订单创建人电话" {...formItemLayout}>
                {getFieldDecorator('createUserTelephone', {
                })(
                  <Input
                    onPressEnter={queryScenes.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="订单状态" {...formItemLayout}>
                {getFieldDecorator('orderTrans', {
                })(
                  <Select>
                    {OrderTrans}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="接单人" {...formItemLayout}>
                {getFieldDecorator('takeUser', {
                })(
                  <Input
                    onPressEnter={queryScenes.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            {sendCollapse && <div>
              <Col {...queryCol}>
                <FormItem label="接单电话" {...formItemLayout}>
                  {getFieldDecorator('takeUserTelephone', {
                  })(
                    <Input
                      onPressEnter={queryScenes.bind(this)}
                    />
                  )}
                </FormItem>
              </Col>
              <Col {...queryCol}>
                <FormItem label="订单归属省" {...formItemLayout}>
                  {getFieldDecorator('provinceId', {
                  })(
                    <Select onChange={handleProvinceFromChange}>
                    {provinceDataSend && provinceDataSend.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                    </Select>
                  )}
                </FormItem>
              </Col>
              <Col  {...queryCol}>
                <FormItem label="归属市" {...formItemLayout}>
                  {getFieldDecorator('cityId', {
                  })(
                    <Select onChange={handleCityFromChange}>
                      {cityDataSend && cityDataSend.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                    </Select>
                  )}
                </FormItem>
              </Col>
              <Col {...queryCol}>
                <FormItem label="归属区" {...formItemLayout}>
                  {getFieldDecorator('countyId', {
                  })(
                    <Select>
                      {countyDataSend && countyDataSend.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                    </Select>
                  )}
                </FormItem>
              </Col>
              <Col {...queryColdate}>
                <FormItem label="创建时间范围" {...formItemLayoutDate}>
                  {getFieldDecorator('dateTime', {
                  })(
                    <RangePicker format={dateFormat} style={{ width: '100%' }} />
                  )}
                </FormItem>
              </Col>
            </div>}
          </Row>
          <Row>
            <Col className={styles.buttonPosition}>
              <Button onClick={changeCollapse.bind(this)}>{sendCollapse ? '折叠' : '展开'}<Icon type={sendCollapse ? 'up' : 'down'} /></Button>
              <Button type="primary" onClick={queryScenes.bind(this)}>查询</Button>
              <Button type="primary" onClick={reset.bind(this)}>重置</Button>
            </Col>
          </Row>
        </Form>
      </Card>
    </div>
  )
}

QuerySendForm.propTypes = {
  form: PropTypes.object,
  queryLineProps: PropTypes.obj,
  dispatch: PropTypes.func,
  selectData: PropTypes.object,
}
const QuerySend = Form.create()(QuerySendForm)
export default QuerySend
