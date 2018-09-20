import React from 'react'
import { Form, Input, Row, Col, Button, Card, Select, Icon, DatePicker } from 'antd'
import { AreaSelect } from 'components'
import PropTypes from 'prop-types'
import styles from '../index.less'
import moment from 'moment'
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
  lg: 8,
  xl: 8,
}
const CarQueryForm = ({
  form,
  dispatch,
  carData,
  chooseCar,
  firmData,
}) => {
  const { getFieldDecorator } = form
  const saveMessage = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      // formValue.id = chooseCar.id
      // if (formValue.originAreaCode === '') {
      //   formValue.originAreaCode = firmData.originAreaCode
      //   formValue.destAreaCode = firmData.destAreaCode
      // }
      // formValue.vehicleInformationId = chooseCar.id
      // formValue.receiptDate = new Date(formValue.receiptDate).getTime()
      // formValue.receiptToDate = new Date(formValue.receiptToDate).getTime()
      dispatch({ type: 'carStart/saveUnLoad', payload: { data: formValue } })
    })
  }
  const reset = () => {
    form.resetFields()
  }
  // 选择车辆
  const chooseCarOption = (value, option) => {
    dispatch({ type: 'carStart/updateCar', payload: { chooseCar: option.props.data } })
  }
  const fromInfo = {
    province: {
      label: '发站城市',
      key: 'startCitys',
      outKey: 'originAreaCode',
      required: false,
    },
    updateData: (firmData && firmData.id) ? firmData.startCity : [],
    code: (firmData && firmData.id) ? firmData.originAreaCode : '',
    form,
    formItemLayout,
  }
  const toInfo = {
    province: {
      label: '发站城市',
      key: 'endCitys',
      outKey: 'destAreaCode',
      required: false,
    },
    updateData: (firmData && firmData.id) ? firmData.endCity : [],
    code: (firmData && firmData.id) ? firmData.destAreaCode : '',
    form,
    formItemLayout,
  }
  return (
    <div>
      <Card title="在途车辆货物信息">
        <Row>
          <Col {...queryCol}>
            <FormItem label="车辆编号" {...formItemLayout}>
              {getFieldDecorator('vehicleNumber', {
                initialValue: chooseCar && chooseCar.vehicleNumber,
                rules: [{ required: false, message: '请选择车辆编号' }],
              })(
                <Select onSelect={chooseCarOption} style={{ width: '100%' }} disabled>
                  {carData && carData.map(item => <Option key={item.vehicleNumber} data={item} >{item.vehicleNumber}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="司机姓名" {...formItemLayout}>
              {getFieldDecorator('driverName', {
                initialValue: chooseCar && chooseCar.driverName,
              })(
                <Input
                  disabled
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="司机电话" {...formItemLayout}>
              {getFieldDecorator('driverTelephone', {
                initialValue: chooseCar && chooseCar.driverTelephone,
              })(
                <Input
                  disabled
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="车牌号" {...formItemLayout}>
              {getFieldDecorator('numberPlate', {
                initialValue: chooseCar && chooseCar.numberPlate,
              })(
                <Input
                  disabled
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="车长" {...formItemLayout}>
              {getFieldDecorator('carLength', {
                initialValue: chooseCar && chooseCar.carLength,
              })(
                <Input
                  disabled
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="载重" {...formItemLayout}>
              {getFieldDecorator('cargoLoad', {
                initialValue: chooseCar && chooseCar.cargoLoad,
              })(
                <Input
                  disabled
                />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="发车时间" {...formItemLayout}>
              {getFieldDecorator('receiptDate', {
                initialValue: (firmData && firmData.receiptDate) ? moment((firmData.receiptDate)) : '',
                rules: [{ required: false, message: '请填写发车时间' }],
              })(
                <DatePicker style={{ width: '100%' }} showTime format="YYYY-MM-DD HH:mm:ss" disabled />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <FormItem label="预计到站时间" {...formItemLayout}>
              {getFieldDecorator('receiptToDate', {
                initialValue: (firmData && firmData.receiptToDate) ? moment((firmData.receiptToDate)) : '',
                rules: [{ required: false, message: '请填写到站时间' }],
              })(
                <DatePicker style={{ width: '100%' }} showTime format="YYYY-MM-DD HH:mm:ss" disabled />
              )}
            </FormItem>
          </Col>
          <Col {...queryCol}>
            <AreaSelect
              {...fromInfo}
              placeholder="请选择"
              disabled
            />
          </Col>
          <Col {...queryCol}>
            <AreaSelect
              {...toInfo}
              placeholder="请选择"
              disabled
            />
          </Col>
        </Row>
        <Row className={styles.buttonPosition}>
          <Button type="warning" onClick={saveMessage.bind(this)}>确认卸货</Button>
        </Row>
      </Card>
    </div>
  )
}
CarQueryForm.propTypes = {
  form: PropTypes.obj,
  dispatch: PropTypes.func,
  carData: PropTypes.array,
  chooseCar: PropTypes.obj,
  firmData: PropTypes.obj,
}
const CarQuery = Form.create()(CarQueryForm)
export default CarQuery
