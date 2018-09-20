import React from 'react'
import { Form, Input, Row, Col, Button, Card } from 'antd'
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
const GoodsQueryForm = ({
  form,
  dispatch,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'goodsManagement/update', payload: { fields: formValue, tableTime: new Date().getTime() } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const addUser = () => {
    dispatch({ type: 'goodsManagement/updatefirmModal', payload: { visible: true } })
  }
  return (
    <div>
      <Card title="货物信息管理">
        <Row>
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
            <FormItem label="简拼" {...formItemLayout}>
              {getFieldDecorator('jianpin', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
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
GoodsQueryForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
}
const GoodsQuery = Form.create()(GoodsQueryForm)
export default GoodsQuery
