import React from 'react'
import { Form, Input, Row, Col, Button, Card, DatePicker, Select } from 'antd'
import PropTypes from 'prop-types'
import styles from '../common/index.less'

const { RangePicker } = DatePicker
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
const dateFormat = 'YYYY-MM-DD HH:mm:ss'
const PicAndArtForm = ({
  form,
  dispatch,
  infoData,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      for (let key in formValue) {
        if (key === 'dateTime') {
          if (formValue[key]) {
            formValue.publishDateStart = formValue[key][0].format('YYYY-MM-DD HH:mm:ss')
            formValue.publishDateEnd = formValue[key][1].format('YYYY-MM-DD HH:mm:ss')
          }
        }
      }
      dispatch({ type: 'informationManagement/updatePicAndArt', payload: { fields: formValue } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const add = () => {
    dispatch({ type: 'informationManagement/updatePicAndArtModal', payload: { visible: true } })
  }
  return (
    <div>
      <Card title="图文信息管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="信息编号" {...formItemLayout}>
              {getFieldDecorator('newsNo', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="信息标题" {...formItemLayout}>
              {getFieldDecorator('title', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="信息类型" {...formItemLayout}>
              {getFieldDecorator('newsType', {
              })(
                <Select style={{ width: '100%' }}>
                  {infoData && infoData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="信息状态" {...formItemLayout}>
              {getFieldDecorator('status', {
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={0} >停用</Option>
                  <Option key={1} >可用</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="创建时间显示" {...formItemLayout}>
              {getFieldDecorator('displayPublishDate', {
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={0} >否</Option>
                  <Option key={1} >是</Option>
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
PicAndArtForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  infoData: PropTypes.array,
}
const PicAndArtQuery = Form.create()(PicAndArtForm)
export default PicAndArtQuery
