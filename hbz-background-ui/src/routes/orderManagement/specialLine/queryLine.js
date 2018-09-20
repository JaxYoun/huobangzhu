import React from 'react'
import PropTypes from 'prop-types'
import moment from 'moment'
import { Card, Select, Form, DatePicker, Row, Col, Input, Button, Icon } from 'antd'
import styles from './index.less'
const { RangePicker } = DatePicker
const QueryLineForm = ({
  form,
  queryLineProps,
  dispatch,
}) => {
  const {
    provinceFromData,
    cityFromData,
    countyFromData,
    provinceToData,
    cityToData,
    countyToData,
    selectData,
    collapse,
  } = queryLineProps
  let {
    SettlementType,
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
  const dateFormat = 'YYYY-MM-DD'
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
      dispatch({ type: 'specialLine/update', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
    dispatch({ type: 'specialLine/update', payload: { cityFromData: '', countyFromData: '', cityToData: '', countyToData: '' } })
  }

  // 取货省市联动查询
  const handleProvinceFromChange = (value) => {
    // 判断是不是直辖市
    if (value === '1' || value === '18' || value === '791' || value === '2234') {
      dispatch({ type: 'specialLine/updateFromCounty', payload: { parentId: value } })
    } else {
      dispatch({ type: 'specialLine/updateFromCity', payload: { parentId: value } })
    }
    dispatch({ type: 'specialLine/update', payload: { cityFromData: '', countyFromData: '' } })
    form.resetFields(['cityId', 'countyId'])
  }
  const handleCityFromChange = (value) => {
    dispatch({ type: 'specialLine/update', payload: { countyFromData: '' } })
    form.resetFields(['countyId'])
    dispatch({ type: 'specialLine/updateFromCounty', payload: { parentId: value } })
  }
  // 送货省市区联动
  const handleProvinceToChange = (value) => {
    if (value === '1' || value === '18' || value === '791' || value === '2234') {
      dispatch({ type: 'specialLine/updateToCounty', payload: { parentId: value } })
    } else {
      dispatch({ type: 'specialLine/updateToCity', payload: { parentId: value } })
    }
    dispatch({ type: 'specialLine/update', payload: { cityToData: '', countyToData: '' } })
    form.resetFields(['cityToId', 'countyToId'])
  }
  const handleCityToChange = (value) => {
    dispatch({ type: 'specialLine/update', payload: { countyToData: '' } })
    form.resetFields(['countyToId'])
    dispatch({ type: 'specialLine/updateToCounty', payload: { parentId: value } })
  }
  const selectDataGet = (data) => {
    let arrays = []
    for (let key in data) {
      arrays.push(<Option key={key}>{data[key]}</Option>)
    }
    return arrays
  }
  SettlementType = selectDataGet(SettlementType)
  OrderTrans = selectDataGet(OrderTrans)
  // 查询样式
  const changeCollapse = () => {
    dispatch({ type: 'specialLine/update', payload: { collapse: !collapse } })
  }
  return (
    <div>
      <Card title="订单查询">
        <Form>
          <Row>
            <Col {...queryCol} >
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
                {getFieldDecorator('org', {
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
                {getFieldDecorator('createUsertelephone', {
                })(
                  <Input
                    onPressEnter={queryScenes.bind(this)}
                  />
                )}
              </FormItem>
            </Col>
            <Col {...queryCol}>
              <FormItem label="付款方式" {...formItemLayout}>
                {getFieldDecorator('settlementType', {
                })(
                  <Select>
                     {SettlementType}
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
           {collapse && <div>
             <Col {...queryCol}>
               <FormItem label="取货省" {...formItemLayout}>
                {getFieldDecorator('provinceId', {
                })(
                  <Select onChange={handleProvinceFromChange}>
                  {provinceFromData && provinceFromData.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                  </Select>
                )}
               </FormItem>
             </Col>
             <Col {...queryCol} >
               <FormItem label="取货市" {...formItemLayout}>
                {getFieldDecorator('cityId', {
                })(
                  <Select onChange={handleCityFromChange}>
                    {cityFromData && cityFromData.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                  </Select>
                )}
               </FormItem>
             </Col>
             <Col {...queryCol} >
               <FormItem label="取货区" {...formItemLayout}>
                {getFieldDecorator('countyId', {
                })(
                  <Select>
                    {countyFromData && countyFromData.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                  </Select>
                )}
               </FormItem>
             </Col>
             <Col {...queryCol}>
              <FormItem label="送货省" {...formItemLayout}>
                {getFieldDecorator('provinceToId', {
                })(
                  <Select onChange={handleProvinceToChange}>
                      {provinceToData && provinceToData.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                  </Select>
                )}
              </FormItem>
             </Col>
             <Col {...queryCol}>
               <FormItem label="送货市" {...formItemLayout}>
                {getFieldDecorator('cityToId', {
                })(
                  <Select onChange={handleCityToChange}>
                    {cityToData && cityToData.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                  </Select>
                )}
               </FormItem>
             </Col>
             <Col {...queryCol}>
               <FormItem label="送货区" {...formItemLayout}>
                {getFieldDecorator('countyToId', {
                })(
                  <Select>
                    {countyToData && countyToData.map(item => <Option key={item.id}>{item.areaName}</Option>)}
                  </Select>
                )}
               </FormItem>
             </Col>
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
               <FormItem label="订单状态" {...formItemLayout}>
                {getFieldDecorator('orderTrans', {
                })(
                  <Select>
                    {OrderTrans}
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
          <Row className={styles.buttonPosition}>
            <Button onClick={changeCollapse.bind(this)}>{collapse ? '折叠' : '展开'}<Icon type={collapse ? 'up' : 'down'} /></Button>
            <Button type="primary" onClick={queryScenes.bind(this)}>查询</Button>
            <Button type="primary" onClick={reset.bind(this)}>重置</Button>
          </Row>
        </Form>
      </Card>
    </div>
  )
}

QueryLineForm.propTypes = {
  form: PropTypes.object,
  queryLineProps: PropTypes.obj,
  dispatch: PropTypes.func,
  selectData: PropTypes.object,
}
const QueryLine = Form.create()(QueryLineForm)
export default QueryLine
