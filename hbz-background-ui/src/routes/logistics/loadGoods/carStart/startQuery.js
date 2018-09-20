import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select, Icon, DatePicker } from 'antd'
import PropTypes from 'prop-types'
import { AreaSelect } from 'components'
import styles from './index.less'
const FormItem = Form.Item
const { RangePicker } = DatePicker
const dateFormat = 'YYYY-MM-DD'
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
const queryColdate = {
  sm: 16,
  md: 16,
  lg: 12,
  xl: 8,
}
const StartQueryForm = ({
  form,
  dispatch,
  collapse,
  carTypeData,
  transitStateData,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.bigDate = new Date(formValue.bigDate).getTime()
      formValue.smallDate = new Date(formValue.smallDate).getTime()
      dispatch({ type: 'carStart/update', payload: { fields: formValue } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const add = () => {
    dispatch({ type: 'carStart/updatefirmModal', payload: { visible: true } })
  }
  // 查询样式
  const changeCollapse = () => {
    dispatch({ type: 'carStart/update', payload: { collapse: !collapse } })
  }
  const fromInfo = {
    province: {
      label: '发站城市',
      key: 'startCitys',
      outKey: 'originAreaCode',
      required: false,
    },
    updateData: [],
    code: '',
    form,
    formItemLayout,
  }
  const toInfo = {
    province: {
      label: '到站城市',
      key: 'endCitys',
      outKey: 'destAreaCode',
      required: false,
    },
    updateData: [],
    code: '',
    form,
    formItemLayout,
  }
  return (
    <div>
      <Card title="发车单管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="发车编号" {...formItemLayout}>
              {getFieldDecorator('startNumber', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="车牌号" {...formItemLayout}>
              {getFieldDecorator('numberPlate', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="司机名称" {...formItemLayout}>
              {getFieldDecorator('driverName', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="司机电话" {...formItemLayout}>
              {getFieldDecorator('driverTelephone', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="车辆类型" {...formItemLayout}>
              {getFieldDecorator('paymentMethod', {
              })(
                <Select style={{ width: '100%' }}>
                  {carTypeData && carTypeData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="在途状态" {...formItemLayout}>
              {getFieldDecorator('isReceipt', {
              })(
                <Select style={{ width: '100%' }}>
                  {transitStateData && Object.keys(transitStateData).map(key => <Option key={key}>{transitStateData[key]}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          {collapse && <div>
            <Col {...queryColdate}>
              <FormItem label="发车时间" {...formItemLayoutDate}>
              {getFieldDecorator('dateTime', {
              })(
                <RangePicker format={dateFormat} style={{ width: '100%' }} />
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
            <Col {...queryCol}>
              <AreaSelect
                {...fromInfo}
                placeholder="请选择"
              />
            </Col>
            <Col {...queryCol}>
              <AreaSelect
                {...toInfo}
                placeholder="请选择"
              />
            </Col>
          </div>}
        </Row>
        <Row className={styles.buttonPosition}>
          <Button onClick={changeCollapse.bind(this)}>{collapse ? '折叠' : '展开'}<Icon type={collapse ? 'up' : 'down'} /></Button>
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
          <Button type="warning" onClick={add.bind(this)}>新增</Button>
        </Row>
      </Card>
    </div>
  )
}
StartQueryForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  collapse: PropTypes.bool,
  carTypeData: PropTypes.array,
  transitStateData: PropTypes.array,
}
const StartQuery = Form.create()(StartQueryForm)
export default StartQuery
