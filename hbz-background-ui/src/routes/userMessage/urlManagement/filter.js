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
  sm: 6,
  md: 6,
  lg: 6,
  xl: 6,
}
const FilterForm = ({ form, dispatch }) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({
        type: 'urlManagement/update',
        payload: { fields: formValue, tableTime: new Date().getTime() },
      })
    })
  }
  const addRole = () => {
    dispatch({
      type: 'urlManagement/update',
      payload: { detailModal: { visible: true } },
    })
  }
  const reset = () => {
    form.resetFields()
  }
  return (
    <div>
      <Card title="资源管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="资源名称" {...formItemLayout}>
              {getFieldDecorator('urlLabel', {})(
                <Input onPressEnter={queryFirm.bind(this)} />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="资源状态" {...formItemLayout}>
              {getFieldDecorator('state', {})(
                <Select style={{ width: '100%' }}>
                  <Option value={1}>正常</Option>
                  <Option value={0}>禁用</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={addRole}>
            新增
          </Button>
          <Button type="primary" onClick={queryFirm.bind(this)}>
            查询
          </Button>
          <Button type="primary" onClick={reset.bind(this)}>
            重置
          </Button>
        </Row>
      </Card>
    </div>
  )
}
FilterForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
}
const Filter = Form.create()(FilterForm)
export default Filter
