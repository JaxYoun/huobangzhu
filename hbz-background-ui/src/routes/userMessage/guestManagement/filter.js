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
      console.log(formValue)
      dispatch({
        type: 'guestManagement/update',
        payload: { fields: formValue },
      })
    })
  }
  const addGuest = () => {
    dispatch({
      type: 'guestManagement/update',
      payload: { guestModal: { visible: true } },
    })
  }
  const reset = () => {
    form.resetFields()
  }
  return (
    <div>
      <Card title="客户管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="客户昵称" {...formItemLayout}>
              {getFieldDecorator('nickName', {})(
                <Input onPressEnter={queryFirm.bind(this)} />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="电话号码" {...formItemLayout}>
              {getFieldDecorator('telephone', {})(
                <Input onPressEnter={queryFirm.bind(this)} />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="客户状态" {...formItemLayout}>
              {getFieldDecorator('activated', {})(
                <Select style={{ width: '100%' }}>
                  <Option value={true}>正常</Option>
                  <Option value={false}>禁用</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={addGuest}>
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
  registryData: PropTypes.array,
  registryProgressData: PropTypes.array,
}
const Filter = Form.create()(FilterForm)
export default Filter
