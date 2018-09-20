import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select, Icon, DatePicker } from 'antd'
import PropTypes from 'prop-types'
import { AreaSelect } from 'components'
import styles from '../index.less'
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
const queryCol = {
  sm: 8,
  md: 8,
  lg: 8,
  xl: 8,
}
const TransportOrderQueryForm = ({
  form,
  dispatch,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'carStart/updateTransportOrder', payload: { fields: formValue } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const toInfo = {
    province: {
      label: '到站城市',
      key: 'endCitys',
      outKey: 'destAreaId',
      required: false,
    },
    updateData: [],
    code: '',
    form,
    formItemLayout,
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
            <FormItem label="货物编号" {...formItemLayout}>
              {getFieldDecorator('commodityNumber', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
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
              {...toInfo}
              placeholder="请选择"
            />
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
        </Row>
      </Card>
    </div>
  )
}
TransportOrderQueryForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
}
const TransportOrderQuery = Form.create()(TransportOrderQueryForm)
export default TransportOrderQuery
