import React from 'react'
import { Modal, Form, Col, Row, Input, Button, DatePicker } from 'antd'
import { Map, Marker, ScaleControl, NavigationControl } from 'react-bmap'
import PropTypes from 'prop-types'
import moment from 'moment'
import styles from '../index.less'
const FormItem = Form.Item
const formItemLayout = {
  labelCol: {
    xs: { span: 6 },
    sm: { span: 6 },
  },
  wrapperCol: {
    xs: { span: 18 },
    sm: { span: 18 },
  },
}
const formItemLayouts = {
  labelCol: {
    xs: { span: 3 },
    sm: { span: 3 },
  },
  wrapperCol: {
    xs: { span: 21 },
    sm: { span: 21 },
  },
}
const { TextArea } = Input
const ExamineModalDetail = ({
  visible,
  firmData,
  form,
  dispatch,
}) => {
  const { getFieldDecorator } = form
  // 关闭模态框
  const hideModal = () => {
    form.resetFields()
    dispatch({ type: 'storageExamine/cleanDetailModal' })
  }
  return (
    <div>
      <Modal
        title={'仓储审核'}
        visible={visible}
        onOk={hideModal}
        maskClosable={false}
        onCancel={hideModal}
        width={900}
        footer={null}
        className={styles.detail}
      >
        <Form>
          <Row style={{ fontSize: '18px', fontWeight: 'bold', textAlign: 'center' }} >
            {firmData.id && <span>{firmData.name}</span>}
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="仓库容量(平方米)" {...formItemLayout}>
                {getFieldDecorator('capacity', {
                  initialValue: firmData && firmData.capacity,
                  rules: [{ required: false, message: '仓库容量' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="租赁价格(元/平)" {...formItemLayout}>
                {getFieldDecorator('unitPrice', {
                  initialValue: firmData && firmData.unitPrice,
                  rules: [{ required: true, message: '租赁价格' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="租赁时间" {...formItemLayout}>
                {getFieldDecorator('minRentTime', {
                  initialValue: firmData && firmData.minRentTime,
                  rules: [{ required: false, message: '租赁时间' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="发布时间" {...formItemLayout}>
                {getFieldDecorator('publishDate', {
                  initialValue: (firmData && firmData.publishDate) ? moment(Number(firmData.publishDate)) : moment(new Date(), 'YYYY-MM-DD HH:MM:SS'),
                  rules: [{ required: false, message: '请填写发布时间' }],
                })(
                  <DatePicker
                    showTime
                    format="YYYY-MM-DD HH:mm:ss"
                    disabled
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="联系人" {...formItemLayout}>
                {getFieldDecorator('ownerName', {
                  initialValue: firmData && firmData.ownerName,
                  rules: [{ required: false, message: '联系人' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="联系电话" {...formItemLayout}>
                {getFieldDecorator('telephone', {
                  initialValue: firmData && firmData.telephone,
                  rules: [{ required: true, message: '联系电话' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="仓库地址" {...formItemLayouts}>
                {getFieldDecorator('address', {
                  initialValue: firmData && firmData.address,
                  rules: [{ required: false, message: '仓库地址' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Map center={{ lng: firmData.coordX, lat: firmData.coordY }} zoom="13">
              <Marker
                position={{ lng: firmData.coordX, lat: firmData.coordY }}
                icon="loc_red"
              />
              <ScaleControl />
              <NavigationControl />
            </Map>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="备注" {...formItemLayouts}>
                {getFieldDecorator('warehouseDescribe', {
                  initialValue: firmData && firmData.warehouseDescribe,
                  rules: [{ required: false, message: '备注' }],
                })(
                  <TextArea row={4} disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={hideModal.bind(this)}>确定</Button>
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
ExamineModalDetail.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  loading: PropTypes.bool,
  type: PropTypes.string,
}
const ExamineModalDetailForm = Form.create()(ExamineModalDetail)
export default ExamineModalDetailForm
