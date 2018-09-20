import React from 'react'
import { Modal, Form, Col, Row, Input, Select, Button, DatePicker, Card, Table } from 'antd'
import PropTypes from 'prop-types'
import styles from '../index.less'
import moment from 'moment'
import { AreaSelect } from 'components'
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
const StartSureModalForm = ({
  visible,
  form,
  dispatch,
  firmData,
  loading,
  dataSource,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.id = firmData.id
      dispatch({ type: 'carStart/updateSureModal', payload: { loading: true } })
      dispatch({ type: 'carStart/sure', payload: formValue })
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'carStart/cleanSureModal' })
    form.resetFields()
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
  // 列表参数
  const columnsData = [
    {
      title: '物流编号',
      dataIndex: 'trackingNumber',
      width: 120,
    },
    {
      title: '运单号',
      dataIndex: 'waybillNumber',
      width: 120,
    },
    {
      title: '收件类型',
      dataIndex: 'goodsType',
      width: 80,
      render: (text, record) => {
        if (record.goodsType === '0') {
          text = '零担'
        } else if (record.goodsType === '1') {
          text = '整车'
        } else if (record.goodsType === '2') {
          text = '快递'
        }
        return (
          <span>{text}</span>
        )
      },
    },
    {
      title: '发站城市',
      dataIndex: 'originArea',
      width: 100,
    },
    {
      title: '到站城市  ',
      dataIndex: 'destArea',
      width: 100,
    },
    {
      title: '货物名称',
      dataIndex: 'commodityName',
      width: 120,
    },
    {
      title: '装车数量',
      dataIndex: 'waybillQuantity',
      width: 120,
    },
    {
      title: '收货单位',
      dataIndex: 'receiverUserCompanyName',
      width: 120,
    },
    {
      title: '收货电话',
      dataIndex: 'receiverUserTelephone',
      width: 120,
    },
    {
      title: '包装',
      dataIndex: 'packageUnit',
      width: 120,
    },
    {
      title: '重量KG',
      dataIndex: 'weight',
      width: 120,
      render: (text, record) => {
        if (record.trackingNumber === '合计') {
          record.weight = record.weight
        } else {
          record.weight = record.singleWeight * record.waybillQuantity
        }
        return (
          <span>{record.weight}</span>
        )
      },
    },
    {
      title: '体积m³',
      dataIndex: 'volume',
      width: 120,
      render: (text, record) => {
        if (record.trackingNumber === '合计') {
          record.volume = record.volume
        } else {
          record.volume = record.singleVolume * record.waybillQuantity
        }
        return (
          <span>{record.volume}</span>
        )
      },
    },
    {
      title: '已付',
      dataIndex: 'alreadyPay',
      width: 80,
    },
    {
      title: '提付',
      dataIndex: 'putPay',
      width: 100,
    },
    {
      title: '回付',
      dataIndex: 'backPay',
      width: 80,
    },
    {
      title: '代收货款',
      dataIndex: 'onCollection',
      width: 100,
    },
    {
      title: '垫付费用',
      dataIndex: 'advancePayment',
      width: 120,
    },
    {
      title: '付款方式',
      dataIndex: 'paymentMethodName',
      width: 120,
    },
    {
      title: '备注',
      dataIndex: 'remarks',
      width: 120,
    },
  ]
  let tableProps = {
    columns: columnsData,
    dataSource,
  }
  return (
    <div>
      <Modal
        title={'发车确认'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={1300}
        footer={null}
      >
        <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
          {firmData.id && <span>发车编号：{firmData.wareNo}</span>}
        </Row>
        <Form>
          <Row>
            <Col span={6}>
              <FormItem label="司机姓名" {...formItemLayout}>
                {getFieldDecorator('driverName', {
                  initialValue: firmData.vehicleInformationDTO && firmData.vehicleInformationDTO.driverName,
                  rules: [{ required: false, message: '请填写司机姓名', max: 30 }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={6}>
              <FormItem label="司机电话" {...formItemLayout}>
                {getFieldDecorator('driverTelephone', {
                  initialValue: firmData.vehicleInformationDTO && firmData.vehicleInformationDTO.driverTelephone,
                  rules: [{ required: false, message: '请填写司机电话' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={6}>
              <FormItem label="车牌号" {...formItemLayout}>
                {getFieldDecorator('numberPlate', {
                  initialValue: firmData.vehicleInformationDTO && firmData.vehicleInformationDTO.numberPlate,
                  rules: [{ required: false, message: '请填写车牌号' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={6}>
              <FormItem label="车长" {...formItemLayout}>
                {getFieldDecorator('carLength', {
                  initialValue: firmData.vehicleInformationDTO && firmData.vehicleInformationDTO.carLength,
                  rules: [{ required: false, message: '请填写正确的车长' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={6}>
              <FormItem label="载重" {...formItemLayout}>
                {getFieldDecorator('cargoLoad', {
                  initialValue: firmData.vehicleInformationDTO && firmData.vehicleInformationDTO.cargoLoad,
                  rules: [{ required: false, message: '请填写正确的载重' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={6}>
              <FormItem label="发车时间" {...formItemLayout}>
                {getFieldDecorator('receiptDate', {
                  initialValue: (firmData && firmData.receiptDate) ? moment((firmData.receiptDate)) : '',
                  rules: [{ required: false, message: '请填写正确的时间' }],
                })(
                  <DatePicker style={{ width: '100%' }} showTime format="YYYY-MM-DD HH:mm:ss" disabled />
                )}
              </FormItem>
            </Col>
            <Col span={6}>
              <FormItem label="预计到站时间" {...formItemLayout}>
                {getFieldDecorator('receiptToDate', {
                  initialValue: (firmData && firmData.receiptToDate) ? moment((firmData.receiptToDate)) : '',
                  rules: [{ required: false, message: '请填写到站时间' }],
                })(
                  <DatePicker style={{ width: '100%' }} showTime format="YYYY-MM-DD HH:mm:ss" disabled />
                )}
              </FormItem>
            </Col>
            <Col span={6}>
              <AreaSelect
                {...fromInfo}
                placeholder="请选择"
                disabled
              />
            </Col>
          </Row>
          <Row>
            <Col span={6}>
              <AreaSelect
                {...toInfo}
                placeholder="请选择"
                disabled
              />
            </Col>
          </Row>
          <Row>
            <Card>
              <Table {...tableProps}
                scroll={{ x: 1800 }}
              />
            </Card>
          </Row>
          <Row className={styles.buttonPosition}>
            {(firmData.id && firmData.shippingStatus === 'NEW') && <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>发车确认</Button>}
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
StartSureModalForm.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  firmData: PropTypes.obj,
  loading: PropTypes.bool,
  dataSource: PropTypes.array,
}
const StartSureModal = Form.create()(StartSureModalForm)
export default StartSureModal
