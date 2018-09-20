import React from 'react'
import { Form, Input, Row, Col, Button, Card, TreeSelect, Select } from 'antd'
import PropTypes from 'prop-types'
import styles from '../common/index.less'
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
  lg: 6,
  xl: 4,
}
const ExchangeForm = ({
  form,
  dispatch,
  exchangeModal,
  wareTypeData,
}) => {
  let { brandData } = exchangeModal
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'scoreExchange/updateExchange', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  return (
    <div>
      <Card title="商品配置管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="商品上架编号" {...formItemLayout}>
              {getFieldDecorator('productNo', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="兑换订单编号" {...formItemLayout}>
              {getFieldDecorator('orderNo', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="送货地址" {...formItemLayout}>
              {getFieldDecorator('destAddr', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="兑换人" {...formItemLayout}>
              {getFieldDecorator('link', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('telephone', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="发货状态" {...formItemLayout}>
              {getFieldDecorator('state', {
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={1} >未发货</Option>
                  <Option key={2} >已发货</Option>
                  <Option key={3} >收货完</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="商品品牌" {...formItemLayout}>
              {getFieldDecorator('brandId', {
              })(
                <Select style={{ width: '100%' }}>
                    {brandData && brandData.map(item => <Option key={item.id}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="商品分类" {...formItemLayout}>
              {getFieldDecorator('typeId', {
              })(
                <TreeSelect
                  style={{ width: '100%' }}
                  treeData={wareTypeData}
                />
              )}
            </FormItem>
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
ExchangeForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  wareTypeData: PropTypes.array,
  exchangeModal: PropTypes.array,
}
const ExchangeQuery = Form.create()(ExchangeForm)
export default ExchangeQuery
