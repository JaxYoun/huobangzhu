import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select } from 'antd'
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

const BannerForm = ({
  form,
  dispatch,
  locationData,
  skipTypeData,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'adManagement/updateBanner', payload: { fields: formValue } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const add = () => {
    dispatch({ type: 'adManagement/updateBannerModal', payload: { visible: true } })
  }
  return (
    <div>
      <Card title="Banner管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="名称" {...formItemLayout}>
              {getFieldDecorator('name', {
                rules: [{ required: false, message: '长度不超过100', max: 99 }],
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="Banner位置" {...formItemLayout}>
              {getFieldDecorator('location', {
              })(
                <Select style={{ width: '100%' }}>
                   {locationData && locationData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="跳转方式" {...formItemLayout}>
              {getFieldDecorator('skipType', {
              })(
                <Select style={{ width: '100%' }}>
                   {skipTypeData && skipTypeData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="Banner状态" {...formItemLayout}>
              {getFieldDecorator('ifEnable', {
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={0} >已禁用</Option>
                  <Option key={1} >可用</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="warning" onClick={add.bind(this)}>新增</Button>
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
        </Row>
      </Card>
    </div>
  )
}
BannerForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  locationData: PropTypes.array,
  skipTypeData: PropTypes.array,
}
const BannerQuery = Form.create()(BannerForm)
export default BannerQuery
