import React from 'react'
import { Modal, Form, Col, Row, Input, Button } from 'antd'
import PropTypes from 'prop-types'
import styles from './index.less'
import LogisticsModal from '../common/logisticsModal'
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
const SendModal = ({
  visible,
  firmData,
  form,
  hideModal,
  dispatch,
  payData,
  teakUserData,
  platformData,
  logisticsModal,
  sendData,
}) => {
  // // 判断对象是否为空
  // const isEmptyObject = (obj) => {
  //   for (let key in obj) {
  //     return false
  //   }
  //   return true
  // }
  const showModals = () => {
    dispatch({ type: 'cityDistribution/updateLogisticsModal', payload: { logisticsData: [] } })
    dispatch({ type: 'cityDistribution/logisticsDataGet', payload: { id: firmData.id } })
    dispatch({ type: 'cityDistribution/updateLogisticsModal', payload: { visible: true } })
  }
  // 关闭模态框
  const hideModals = () => {
    dispatch({ type: 'cityDistribution/updateLogisticsModal', payload: { visible: false, logisticsData: [] } })
  }
  const { getFieldDecorator } = form
  // const handleSubmit = () => {
  //   form.validateFields((err, formValue) => {
  //     if (err) {
  //       return
  //     }
  //     dispatch({ type: 'firmManage/changeFirm', payload: formValue })
  //   })
  // }
  // 帮我送基础信息部分
  const send = () => {
    return (
      <div className={styles.showpadding}>
        <Row>
          <Col span={11} >
            <span style={{ fontSize: '18px', fontWeight: 'bold' }}>订单编号：{sendData.orderNo}</span>
          </Col>
          <Col span={6}>
            <span style={{ fontSize: '18px', fontWeight: 'bold' }}>订单状态：{sendData.orderTransValue}</span>
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
            <FormItem label="商品名称" {...formItemLayout}>
              {getFieldDecorator('commodityName', {
                initialValue: sendData.commodityName ? sendData.commodityName : '---',
              })(
                <Input disabled title={sendData.commodityName ? sendData.commodityName : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="预估重量" {...formItemLayout}>
              {getFieldDecorator('commodityWeight', {
                initialValue: sendData.commodityWeight ? sendData.commodityWeight : '---',
              })(
                <Input disabled title={sendData.commodityWeight ? sendData.commodityWeight : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="预估体积" {...formItemLayout}>
              {getFieldDecorator('commodityVolume', {
                initialValue: sendData.commodityVolume ? sendData.commodityVolume : '---',
              })(
                <Input disabled title={sendData.commodityVolume ? sendData.commodityVolume : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={18}>
            <FormItem label="货物描述" {...formItemLayouts}>
              {getFieldDecorator('commodityDesc', {
                initialValue: sendData.commodityDesc ? sendData.commodityDesc : '---',
              })(
                <Input disabled title={sendData.commodityDesc ? sendData.commodityDesc : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>取货信息</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="取货地址" {...formItemLayout}>
              {getFieldDecorator('originAddress', {
                initialValue: sendData.originAddress ? sendData.originAddress : '---',
              })(
                <Input disabled title={sendData.originAddress ? sendData.originAddress : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={18}>
            <FormItem label="具体地址" {...formItemLayouts}>
              {getFieldDecorator('destAddress', {
                initialValue: sendData.destAddress ? sendData.destAddress : '---',
              })(
                <Input disabled title={sendData.destAddress ? sendData.destAddress : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="联系人" {...formItemLayout}>
              {getFieldDecorator('linker', {
                initialValue: sendData.linker ? sendData.linker : '---',
              })(
                <Input disabled title={sendData.linker ? sendData.linker : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('linkTelephone', {
                initialValue: sendData.linkTelephone ? sendData.linkTelephone : '---',
              })(
                <Input disabled title={sendData.linkTelephone ? sendData.linkTelephone : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>配送时间</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="时间要求" {...formItemLayout}>
              {getFieldDecorator('timeLimitValue', {
                initialValue: sendData.timeLimitValue ? sendData.timeLimitValue : '---',
              })(
                <Input disabled title={sendData.timeLimitValue ? sendData.timeLimitValue : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="起送时间" {...formItemLayout}>
              {getFieldDecorator('startTime', {
                initialValue: sendData.startTime ? sendData.startTime : '---',
              })(
                <Input disabled title={sendData.startTime ? sendData.startTime : '---'} />
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
          {/* <Col span={6}>
            <FormItem label="商品费用" {...formItemLayout}>
              {getFieldDecorator('commodityAmount', {
                initialValue: buyData.commodityAmount ? buyData.commodityAmount : '---',
              })(
                <Input disabled title={buyData.commodityAmount ? buyData.commodityAmount : '---'} />
              )}
            </FormItem>
          </Col> */}
          <Col span={6}>
            <FormItem label="配送费用" {...formItemLayout}>
              {getFieldDecorator('amount', {
                initialValue: sendData.amount ? sendData.amount : '---',
              })(
                <Input disabled title={sendData.amount ? sendData.amount : '---'} />
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
                initialValue: sendData.createUser ? sendData.createUser : '---',
              })(
                <Input disabled title={sendData.createUser ? sendData.createUser : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('createUserTelephone', {
                initialValue: sendData.createUserTelephone ? sendData.createUserTelephone : '---',
              })(
                <Input disabled title={sendData.createUserTelephone ? sendData.createUserTelephone : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={18}>
            <FormItem label="所属公司" {...formItemLayouts}>
              {getFieldDecorator('organizationName', {
                initialValue: sendData && sendData.organizationName,
              })(
                <Input disabled title={sendData && sendData.organizationName} />
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
  // 接单人详情
  const person = () => {
    return (
      <div className={styles.showpadding}>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>接单人详情</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={6}>
            <FormItem label="接单人姓名" {...formItemLayout}>
              {getFieldDecorator('nickName', {
                initialValue: teakUserData.takeUser.nickName ? teakUserData.takeUser.nickName : '---',
              })(
                <Input disabled title={teakUserData.takeUser.nickName ? teakUserData.takeUser.nickName : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="接单人电话" {...formItemLayout}>
              {getFieldDecorator('telephone', {
                initialValue: teakUserData.takeUser.telephone ? teakUserData.takeUser.telephone : '---',
              })(
                <Input disabled titile={teakUserData.takeUser.telephone ? teakUserData.takeUser.telephone : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={18}>
            <FormItem label="所属公司" {...formItemLayouts}>
              {getFieldDecorator('ent', {
                initialValue: teakUserData.takeUser.ent ? teakUserData.takeUser.ent : '---',
              })(
                <Input disabled title={teakUserData.takeUser.ent ? teakUserData.takeUser.ent : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <Button type="success" onClick={showModals.bind(this)}>查看物流详情</Button>
          </Col>
        </Row>
      </div>
    )
  }
  // 平台付款详情
  const platform = () => {
    return (
      <div className={styles.showpadding}>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>平台付款详情</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={6}>
            <FormItem label="付款编号" {...formItemLayout}>
              {getFieldDecorator('paymentNumber', {
                initialValue: platformData.paymentNumber ? platformData.paymentNumber : '---',
              })(
                <Input disabled title={platformData.paymentNumber ? platformData.paymentNumber : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="账号类型" {...formItemLayout}>
              {getFieldDecorator('accountType', {
                initialValue: platformData.accountType ? platformData.accountType : '---',
              })(
                <Input disabled titile={platformData.accountType ? platformData.accountType : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="付款金额" {...formItemLayout}>
              {getFieldDecorator('paymentAmount', {
                initialValue: platformData.paymentAmount ? platformData.paymentAmount : '---',
              })(
                <Input disabled title={platformData.paymentAmount ? platformData.paymentAmount : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="付款账号" {...formItemLayout}>
              {getFieldDecorator('paymentAccount', {
                initialValue: platformData.paymentAccount ? platformData.paymentAccount : '---',
              })(
                <Input disabled title={platformData.paymentAccount ? platformData.paymentAccount : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="付款人账号名称" {...formItemLayout}>
              {getFieldDecorator('payerName', {
                initialValue: platformData.payerName ? platformData.payerName : '---',
              })(
                <Input disabled titile={platformData.payerName ? platformData.payerName : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="付款日期" {...formItemLayout}>
              {getFieldDecorator('paymentDate', {
                initialValue: platformData.paymentDate ? platformData.paymentDate : '---',
              })(
                <Input disabled title={platformData.paymentDate ? platformData.paymentDate : '---'} />
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
        title={firmData.type === 'buy' ? '市内配送-帮我买订单详情' : '市内配送-帮我送订单详情'}
        visible={visible}
        onOk={hideModal}
        maskClosable={false}
        onCancel={hideModal}
        width={1200}
      >
        <Form>
          {sendData.successInfo && send()}
          {payData.successInfo && payment()}
          {teakUserData.successInfo && person()}
          {platformData.successInfo && platform()}
        </Form>
      </Modal>
      <LogisticsModal hideModal={hideModals} {...logisticsModal} />
    </div>
  )
}
SendModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  payData: PropTypes.obj,
  teakUserData: PropTypes.obj,
  platformData: PropTypes.obj,
  logisticsModal: PropTypes.obj,
  sendData: PropTypes.obj,
}
const SendModalForm = Form.create()(SendModal)
export default SendModalForm
