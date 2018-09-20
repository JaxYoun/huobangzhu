import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'

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
const ExamineQueryForm = ({
  form,
  dispatch,
  registryData,
  registryProgressData,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'userExamine/update', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const selectDataGet = (data) => {
    let arrays = []
    for (let key in data) {
      arrays.push(<Option key={key}>{data[key]}</Option>)
    }
    return arrays
  }
  registryData = selectDataGet(registryData)
  registryProgressData = selectDataGet(registryProgressData)
  return (
    <div>
      <Card title="资质审核管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="昵称" {...formItemLayout}>
              {getFieldDecorator('nickName', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="代表名称" {...formItemLayout}>
              {getFieldDecorator('owerName', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="代表证件号" {...formItemLayout}>
              {getFieldDecorator('certificateNo', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="资质审核状态" {...formItemLayout}>
              {getFieldDecorator('registryProgress', {
              })(
                <Select style={{ width: '100%' }}>
                  {registryProgressData}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="资质分类" {...formItemLayout}>
              {getFieldDecorator('registryCode', {
              })(
                <Select style={{ width: '100%' }}>
                  {registryData}
                </Select>
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
  registryData: PropTypes.array,
  registryProgressData: PropTypes.array,
}
const ExamineQuery = Form.create()(ExamineQueryForm)
export default ExamineQuery
