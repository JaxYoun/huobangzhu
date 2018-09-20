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
const ClassForm = ({
  form,
  dispatch,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'basicData/updateClassification', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const addClass = () => {
    // 获取树
    dispatch({ type: 'basicData/getTreeData', payload: { } })
    dispatch({ type: 'basicData/updateClassificationModal', payload: { visible: true } })
  }
  return (
    <div>
      <Card title="商品分类配置">
        <Row>
          <Col {...queryCol}>
            <FormItem label="分类编号" {...formItemLayout}>
              {getFieldDecorator('typeNo', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="分类名称" {...formItemLayout}>
              {getFieldDecorator('name', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="分类级别" {...formItemLayout}>
              {getFieldDecorator('level', {
              })(
                <Select style={{ width: '100%' }}>
                  <Option key={1} >第1级</Option>
                  <Option key={2} >第2级</Option>
                  <Option key={3} >第3级</Option>
                  <Option key={4} >第4级</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
          <Button type="warning" onClick={addClass.bind(this)}>新增</Button>
        </Row>
      </Card>
    </div>
  )
}
ClassForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  classificationModal: PropTypes.obj,
  wareTypeData: PropTypes.array,
}
const ClassificationQuery = Form.create()(ClassForm)
export default ClassificationQuery
