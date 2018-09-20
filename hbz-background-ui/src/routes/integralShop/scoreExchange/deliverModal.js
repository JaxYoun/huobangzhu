import React from 'react'
import { Modal, Form, Col, Row, Input, TreeSelect, Select, DatePicker, InputNumber, Button } from 'antd'
import PropTypes from 'prop-types'
import moment from 'moment'
import styles from '../common/index.less'


const FormItem = Form.Item
const { TextArea } = Input
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
const formItemLayoutforsend = {
  labelCol: {
    xs: { span: 9 },
    sm: { span: 9 },
  },
  wrapperCol: {
    xs: { span: 15 },
    sm: { span: 15 },
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
const DeliverModal = ({
  visible,
  form,
  dispatch,
  firmData,
  wareTypeData,
  brandData,
  transData,
  loading,
}) => {
  const { getFieldDecorator } = form
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.orderNo = firmData.orderNo
      let s = new Date(formValue.sendTime).getTime()
      s = moment(s).format('YYYY-MM-DD')
      formValue.sendTime = s
      dispatch({ type: 'scoreExchange/transGoods', payload: formValue })
      form.resetFields()
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'scoreExchange/cleanDeliverModal' })
    form.resetFields()
  }
  const sendData = () => {
    return (
      <div>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>发货信息</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #000' }} />
        <Row>
          <Col span={8}>
            <FormItem label="快递公司" {...formItemLayoutforsend}>
              {getFieldDecorator('expressCompanyType', {
                rules: [{ required: true, message: '请填写快递公司' }],
              })(
                <Select>
                  {transData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={8}>
            <FormItem label="快递单号" {...formItemLayoutforsend}>
              {getFieldDecorator('exNo', {
                rules: [{ required: true, message: '请填写单号' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={8}>
            <FormItem label="发件时间" {...formItemLayoutforsend}>
              {getFieldDecorator('sendTime', {
                rules: [{ required: true, message: '请填写发件时间' }],
              })(
                <DatePicker style={{ width: '100%' }} format="YYYY-MM-DD" />
              )}
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }

  return (
    <div>
      <Modal
        title={'兑换发货'}
        visible={visible}
        onOk={handleSubmit}
        okText="发货"
        maskClosable={false}
        onCancel={hideModal}
        width={800}
        className={styles.exchangeModal}
        footer={null}
      >
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>积分兑换订单</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #000' }} />
        <Form>
          <Row>
            <Col span={12}>
              <FormItem label="积分兑换单号" {...formItemLayout}>
                {getFieldDecorator('orderNo', {
                  initialValue: firmData && firmData.orderNo,
                  rules: [{ required: false, message: '请填写单号' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="兑换积分" {...formItemLayout}>
                {getFieldDecorator('score', {
                  initialValue: firmData && firmData.product.score,
                  rules: [{ required: false, message: '请填写兑换积分' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="兑换人" {...formItemLayout}>
                {getFieldDecorator('link', {
                  initialValue: firmData && firmData.link,
                  rules: [{ required: false, message: '请填写兑换人' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="联系电话" {...formItemLayout}>
                {getFieldDecorator('telephone', {
                  initialValue: firmData && firmData.telephone,
                  rules: [{ required: false, message: '请填写联系电话' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="兑换时间" {...formItemLayout}>
              {getFieldDecorator('createdDate', {
                initialValue: firmData && moment(firmData.createdDate),
                rules: [{ required: false, message: '请选择兑换时间!' }],
              })(
                <DatePicker style={{ width: '100%' }} disabled />
              )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="是否发货" {...formItemLayout}>
              {getFieldDecorator('state', {
                initialValue: firmData && `${firmData.state}`,
                rules: [{ required: true, message: '是否发货!' }],
              })(
                <Select style={{ width: '100%' }} disabled>
                  <Option key={1} >未发货</Option>
                  <Option key={2} >已发货</Option>
                  <Option key={3} >收货完</Option>
                </Select>
              )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="送货地址" {...formItemLayouts}>
              {getFieldDecorator('destAddr', {
                initialValue: firmData && firmData.destAddr,
                rules: [{ required: false, message: '请选择送货地址!' }],
              })(
                <Input title={firmData && firmData.destAddr} disabled />
              )}
              </FormItem>
            </Col>
          </Row>
          <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #000' }} />
          <Row>
            <Col span={8}>
              <span style={{ fontSize: '15px', fontWeight: 'bold' }}>商品详情</span>
            </Col>
          </Row>
          <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #000' }} />
          <Row>
            <Col span={12}>
              <FormItem label="兑换商品名称" {...formItemLayout}>
                {getFieldDecorator('marketAmount', {
                  initialValue: firmData && firmData.product.ware.name,
                  rules: [{ required: false, message: '兑换商品名称' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="商品分类" {...formItemLayout}>
                {getFieldDecorator('typeId', {
                  initialValue: (firmData && firmData.product.ware.type.id) && firmData.product.ware.type.id,
                  rules: [{ required: false, message: '请选择商品分类' }],
                })(
                  <TreeSelect
                    disabled
                    treeData={wareTypeData}
                  />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="商品小图" {...formItemLayout}>
              {getFieldDecorator('img', {
                rules: [{ required: false, message: '请选择图片!' }],
              })(
                <img src={(firmData && firmData.product.ware.header) && firmData.product.ware.header} />
              )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="商品品牌" {...formItemLayout}>
                {getFieldDecorator('brandId', {
                  initialValue: (firmData && firmData.product.ware.brand.id) && `${firmData.product.ware.brand.id}`,
                  rules: [{ required: false, message: '请填写商品品牌' }],
                })(
                  <Select style={{ width: '100%' }} disabled>
                      {brandData && brandData.map(item => <Option key={item.id}>{item.name}</Option>)}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={12}>
              <FormItem label="商品规格" {...formItemLayout}>
                {getFieldDecorator('specifications', {
                  initialValue: firmData && firmData.product.ware.specifications,
                  rules: [{ required: false, message: '请填写商品规格' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={12}>
              <FormItem label="参考售价" {...formItemLayout}>
                {getFieldDecorator('marketAmount', {
                  initialValue: firmData && firmData.product.ware.marketAmount,
                  rules: [{ required: false, message: '请填写参考售价为数字' }],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <FormItem label="商品简述" {...formItemLayouts}>
                {getFieldDecorator('summary', {
                  initialValue: firmData && firmData.product.ware.summary,
                  rules: [{ required: false, message: '请填写商品简述' }],
                })(
                  <TextArea rows={2} disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          {sendData()}
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>确定</Button>
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
          </Row>
        </Form>
      </Modal>
    </div>
  )
}
DeliverModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  wareTypeData: PropTypes.arry,
  transData: PropTypes.array,
  firmData: PropTypes.obj,
  brandData: PropTypes.array,
  loading: PropTypes.bool,
}
const DeliverModalForm = Form.create()(DeliverModal)
export default DeliverModalForm
