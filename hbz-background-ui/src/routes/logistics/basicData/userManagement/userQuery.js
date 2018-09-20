import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select } from 'antd'
import PropTypes from 'prop-types'
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
  lg: 6,
  xl: 4,
}
const UserQueryForm = ({
  form,
  dispatch,
  userClassification,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'userManagement/update', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const addUser = () => {
    dispatch({ type: 'userManagement/updatefirmModal', payload: { visible: true } })
  }
  return (
    <div>
      <Card title="客户信息">
        <Row>
          <Col {...queryCol}>
            <FormItem label="单位名称" {...formItemLayout}>
              {getFieldDecorator('companyName', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('userTelephone', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="地址" {...formItemLayout}>
              {getFieldDecorator('userAddress', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="简拼" {...formItemLayout}>
              {getFieldDecorator('jianpin', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="客户分类" {...formItemLayout}>
              {getFieldDecorator('userClassification', {
              })(
                <Select style={{ width: '100%' }}>
                  {userClassification.map(item => <Option key={item.val} >{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="primary" onClick={queryFirm.bind(this)}>查询</Button>
          <Button type="primary" onClick={reset.bind(this)}>重置</Button>
          <Button type="warning" onClick={addUser.bind(this)}>新增</Button>
        </Row>
      </Card>
    </div>
  )
}
UserQueryForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  userClassification: PropTypes.array,
}
const UserQuery = Form.create()(UserQueryForm)
export default UserQuery
