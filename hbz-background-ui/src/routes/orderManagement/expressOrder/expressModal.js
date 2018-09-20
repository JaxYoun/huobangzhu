import React from 'react'
import { Modal, Form, Col, Row, Input, Button } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'
import LogisticsModal from '../common/logisticsModal'
import AddExpress from './addExpress'
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
    xs: { span: 2 },
    sm: { span: 2 },
  },
  wrapperCol: {
    xs: { span: 22 },
    sm: { span: 22 },
  },
}
const ExpressModal = ({
  visible,
  firmData,
  form,
  hideModal,
  dispatch,
  commonData,
  payData,
  logisticsModal,
  selectData,
  addExpressModal,
}) => {
  const showModals = () => {
    dispatch({ type: 'expressOrder/updateLogisticsModal', payload: { logisticsData: [] } })
    dispatch({ type: 'expressOrder/logisticsDataGet', payload: { id: firmData.exId } })
    dispatch({ type: 'expressOrder/updateLogisticsModal', payload: { visible: true } })
  }
  // 关闭模态框
  const hideModals = () => {
    dispatch({ type: 'expressOrder/updateLogisticsModal', payload: { visible: false, logisticsData: [] } })
  }
  const { getFieldDecorator } = form
  // 打开快递派件模态框
  const showModalforExpress = () => {
    dispatch({ type: 'expressOrder/updateAddExpressModal', payload: { visible: true, addData: { id: firmData.id } } })
  }
  // 是否可以点击快递派件
  let disabled = true
  if (!commonData.isNull) {
    disabled = false
  }
  // 快递详情基础信息部分
  const common = () => {
    return (
      <div className={styles.showpadding}>
        <Row>
          <Col span={11} >
            <span style={{ fontSize: '18px', fontWeight: 'bold' }}>订单编号：{commonData.orderNo}</span>
          </Col>
          <Col span={6}>
            <span style={{ fontSize: '18px', fontWeight: 'bold' }}>订单状态：{commonData.orderTransValue}</span>
          </Col>
          <Col span={6}>
            {commonData.orderTrans === 'ORDER_TO_BE_RECEIVED' && <Button disabled={disabled} type="primary" style={{ background: '#FF9900', borderColor: '#FF9900' }} onClick={showModalforExpress.bind(this)}>快递派件</Button>}
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>货物详情</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="货物类型" {...formItemLayout}>
              {getFieldDecorator('commodityClass', {
                initialValue: commonData.commodityClassValue ? commonData.commodityClassValue : '---',
              })(
                <Input disabled title={commonData.commodityClassValue ? commonData.commodityClassValue : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="预估重量" {...formItemLayout}>
              {getFieldDecorator('commodityWeight', {
                initialValue: commonData.commodityWeight ? commonData.commodityWeight : '---',
              })(
                <Input disabled title={commonData.commodityWeight ? commonData.commodityWeight : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="预估体积" {...formItemLayout}>
              {getFieldDecorator('commodityVolume', {
                initialValue: commonData.commodityVolume ? commonData.commodityVolume : '---',
              })(
                <Input disabled title={commonData.commodityVolume ? commonData.commodityVolume : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="上门取件时间" {...formItemLayout}>
              {getFieldDecorator('takeTime', {
                initialValue: commonData.takeTime ? commonData.takeTime : '---',
              })(
                <Input disabled title={commonData.takeTime ? commonData.takeTime : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={18}>
            <FormItem label="货物描述" {...formItemLayouts}>
              {getFieldDecorator('commodityDesc', {
                initialValue: commonData.commodityDesc ? commonData.commodityDesc : '---',
              })(
                <Input disabled title={commonData.commodityDesc ? commonData.commodityDesc : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>取件人信息</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="配送城市" {...formItemLayout}>
              {getFieldDecorator('originArea', {
                initialValue: commonData.originArea ? commonData.originArea : '---',
              })(
                <Input disabled title={commonData.originArea ? commonData.originArea : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={18}>
            <FormItem label="具体地址" {...formItemLayouts}>
              {getFieldDecorator('originAddr', {
                initialValue: commonData.originAddr ? commonData.originAddr : '---',
              })(
                <Input disabled title={commonData.originAddr ? commonData.originAddr : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="姓名" {...formItemLayout}>
              {getFieldDecorator('originLinker', {
                initialValue: commonData.originLinker ? commonData.originLinker : '---',
              })(
                <Input disabled title={commonData.originLinker ? commonData.originLinker : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('originTelephone', {
                initialValue: commonData.originTelephone ? commonData.originTelephone : '---',
              })(
                <Input disabled title={commonData.originTelephone ? commonData.originTelephone : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>收件人信息</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="配送城市" {...formItemLayout}>
              {getFieldDecorator('destArea', {
                initialValue: commonData.destArea ? commonData.destArea : '---',
              })(
                <Input disabled title={commonData.destArea ? commonData.destArea : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={18}>
            <FormItem label="具体地址" {...formItemLayouts}>
              {getFieldDecorator('destAddr', {
                initialValue: commonData.destAddr ? commonData.destAddr : '---',
              })(
                <Input disabled title={commonData.destAddr ? commonData.destAddr : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="姓名" {...formItemLayout}>
              {getFieldDecorator('linker', {
                initialValue: commonData.linker ? commonData.linker : '---',
              })(
                <Input disabled title={commonData.linker ? commonData.linker : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('telephone', {
                initialValue: commonData.telephone ? commonData.telephone : '---',
              })(
                <Input disabled title={commonData.telephone ? commonData.telephone : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>费用</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="配送费用" {...formItemLayout}>
              {getFieldDecorator('amount', {
                initialValue: commonData.amount ? commonData.amount : '---',
              })(
                <Input disabled title={commonData.amount ? commonData.amount : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>订单发布人信息</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="联系人" {...formItemLayout}>
              {getFieldDecorator('createUser', {
                initialValue: commonData.createUser ? commonData.createUser : '---',
              })(
                <Input disabled title={commonData.createUser ? commonData.createUser : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('createUserTelephone', {
                initialValue: commonData.createUserTelephone ? commonData.createUserTelephone : '---',
              })(
                <Input disabled title={commonData.createUserTelephone ? commonData.createUserTelephone : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={18}>
            <FormItem label="所属公司" {...formItemLayouts}>
              {getFieldDecorator('organizationName', {
                initialValue: commonData && commonData.organizationName,
              })(
                <Input disabled title={commonData && commonData.organizationName} />
              )}
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }
  // 支付详情
  const payment = () => {
    return (
      <div className={styles.showpadding}>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>支付详情</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={6}>
            <FormItem label="支付机构" {...formItemLayout}>
              {getFieldDecorator('payTypeValue', {
                initialValue: payData.payTypeValue ? payData.payTypeValue : '---',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="在线支付金额" {...formItemLayout}>
              {getFieldDecorator('fee', {
                initialValue: payData.fee ? payData.fee : '---',
              })(
                <Input disabled title={payData.fee ? payData.fee : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="支付终端" {...formItemLayout}>
              {getFieldDecorator('d', {
                initialValue: payData.d ? payData.d : '---',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="支付平台" {...formItemLayout}>
              {getFieldDecorator('e', {
                initialValue: payData.e ? payData.e : '---',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="是否抵扣" {...formItemLayout}>
              {getFieldDecorator('a', {
                initialValue: payData.a ? payData.a : '---',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="抵扣金额" {...formItemLayout}>
              {getFieldDecorator('b', {
                initialValue: payData.b ? payData.b : '---',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="抵扣券类型" {...formItemLayout}>
              {getFieldDecorator('c', {
                initialValue: payData.c ? payData.c : '---',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>

        </Row>
      </div>
    )
  }
  // 快递外包订单
  const outsource = () => {
    return (
      <div className={styles.showpadding}>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>快递外包订单</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={6}>
            <FormItem label="外包快递公司" {...formItemLayout}>
              {getFieldDecorator('expressCompanyTypeValue', {
                initialValue: commonData.expressCompanyTypeValue ? commonData.expressCompanyTypeValue : '---',
              })(
                <Input disabled title={commonData.expressCompanyTypeValue ? commonData.expressCompanyTypeValue : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="快递单号" {...formItemLayout}>
              {getFieldDecorator('trackingNumber', {
                initialValue: commonData.trackingNumber ? commonData.trackingNumber : '---',
              })(
                <Input disabled titile={commonData.trackingNumber ? commonData.trackingNumber : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="发件时间" {...formItemLayout}>
              {getFieldDecorator('sendTime', {
                initialValue: commonData.sendTime ? commonData.sendTime : '---',
              })(
                <Input disabled titile={commonData.sendTime ? commonData.sendTime : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <Button type="success" onClick={showModals.bind(this)}>查看快递物流详情</Button>
          </Col>
        </Row>
      </div>
    )
  }
  console.log('addExpressModal.addData,', addExpressModal.addData)
  return (
    <div>
      <Modal
        title={'快递订单-快递详情'}
        visible={visible}
        onOk={hideModal}
        maskClosable={false}
        onCancel={hideModal}
        width={1200}
      >
        <Form>
          {commonData.successInfo && common()}
          {payData.successInfo && payment()}
          {commonData.isNull && outsource()}

        </Form>
      </Modal>
      <LogisticsModal hideModal={hideModals} {...logisticsModal} />
      <AddExpress selectData={selectData} {...addExpressModal} dispatch={dispatch} />
    </div>
  )
}
ExpressModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  payData: PropTypes.obj,
  teakUserData: PropTypes.obj,
  platformData: PropTypes.obj,
  logisticsModal: PropTypes.obj,
  commonData: PropTypes.obj,
  selectData: PropTypes.obj,
  addExpressModal: PropTypes.obj,
}
const ExpressModalForm = Form.create()(ExpressModal)
export default ExpressModalForm
