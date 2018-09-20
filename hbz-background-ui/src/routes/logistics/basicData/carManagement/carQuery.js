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
const CarQueryForm = ({
  form,
  dispatch,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      dispatch({ type: 'carManagement/update', payload: { fields: formValue } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  const addCar = () => {
    dispatch({ type: 'carManagement/updatefirmModal', payload: { visible: true } })
  }
  return (
    <div>
      <Card title="车辆信息管理">
        <Row>
          <Col {...queryCol}>
            <FormItem label="车牌号" {...formItemLayout}>
              {getFieldDecorator('numberPlate', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="车主名称" {...formItemLayout}>
              {getFieldDecorator('ownersName', {
              })(
                <Input
                  onPressEnter={queryFirm.bind(this)}
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="司机名称" {...formItemLayout}>
              {getFieldDecorator('driverName', {
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
          <Button type="warning" onClick={addCar.bind(this)}>新增</Button>
        </Row>
      </Card>
    </div>
  )
}
CarQueryForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
}
const CarQuery = Form.create()(CarQueryForm)
export default CarQuery
