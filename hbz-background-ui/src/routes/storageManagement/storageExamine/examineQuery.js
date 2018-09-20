import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select, DatePicker } from 'antd'
import PropTypes from 'prop-types'
import styles from '../index.less'

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
const ExamineQueryForm = ({
  form,
  dispatch,
  typeData,
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
      dispatch({ type: 'storageExamine/update', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }

  return (
    <div>
      <Card title="仓储审核管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="仓库名称" {...formItemLayout}>
              {getFieldDecorator('name', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="审核状态" {...formItemLayout}>
              {getFieldDecorator('type', {
              })(
                <Select style={{ width: '100%' }}>
                    {typeData && typeData.map(item => <Option key={item.val}>{item.name}</Option>)}
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
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
        </Row>
      </Card>
    </div>
  )
}
ExamineQueryForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  typeData: PropTypes.array,
}
const ExamineQuery = Form.create()(ExamineQueryForm)
export default ExamineQuery
