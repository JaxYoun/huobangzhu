import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select, Icon, DatePicker } from 'antd'
import { AreaSelect } from 'components'
import PropTypes from 'prop-types'
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
const ReceiptQueryForm = ({
  form,
  dispatch,
  collapse,
  payData,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.bigDate = new Date(formValue.bigDate).getTime()
      formValue.smallDate = new Date(formValue.smallDate).getTime()
      dispatch({ type: 'receiptManagement/update', payload: { fields: formValue } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const add = () => {
    dispatch({ type: 'receiptManagement/updatefirmModal', payload: { visible: true } })
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
  // 查询样式
  const changeCollapse = () => {
    dispatch({ type: 'receiptManagement/update', payload: { collapse: !collapse } })
  }
  return (
    <div>
      <Card title="收运订单管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="运单号" {...formItemLayout}>
              {getFieldDecorator('waybillNumber', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="物流编号" {...formItemLayout}>
              {getFieldDecorator('trackingNumber', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="货物名称" {...formItemLayout}>
              {getFieldDecorator('commodityName', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="中转站" {...formItemLayout}>
              {getFieldDecorator('inWar', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="付款方式" {...formItemLayout}>
              {getFieldDecorator('paymentMethod', {
              })(
                <Select style={{ width: '100%' }}>
                  {payData && payData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="是否回签单" {...formItemLayout}>
              {getFieldDecorator('isReceipt', {
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={0} >否</Option>
                  <Option key={1} >是</Option>
                </Select>
              )}
            </FormItem>
          </Col>
      {collapse && <div>
        <Col {...queryCol}>
          <FormItem label="等话放货" {...formItemLayout}>
            {getFieldDecorator('isDelivery', {
            })(
              <Select style={{ width: '100%' }}>
                <Option key={0} >否</Option>
                <Option key={1} >是</Option>
              </Select>
            )}
          </FormItem>
        </Col>
        <Col {...queryCol}>
          <FormItem label="收件类型" {...formItemLayout}>
            {getFieldDecorator('goodsType', {
            })(
              <Select style={{ width: '100%' }}>
                <Option key={0} >零担</Option>
                <Option key={1} >整车</Option>
                <Option key={2} >快递</Option>
              </Select>
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
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
          <Button type="warning" onClick={add.bind(this)}>新增</Button>
        </Row>
      </Card>
    </div>
  )
}
ReceiptQueryForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  collapse: PropTypes.bool,
  provinceFromData: PropTypes.array,
  cityFromData: PropTypes.array,
  countyFromData: PropTypes.array,
  provinceToData: PropTypes.array,
  cityToData: PropTypes.array,
  countyToData: PropTypes.array,
  payData: PropTypes.array,
  deliveryData: PropTypes.array,
  receiptData: PropTypes.array,
}
const ReceiptQuery = Form.create()(ReceiptQueryForm)
export default ReceiptQuery
