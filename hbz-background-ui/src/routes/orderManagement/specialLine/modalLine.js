import React from 'react'
import { Modal, Form, Col, Row, Input, Button, Table, Tooltip } from 'antd'
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
    xs: { span: 16 },
    sm: { span: 16 },
  },
}
const formItemLayouts = {
  labelCol: {
    xs: { span: 3 },
    sm: { span: 3 },
  },
  wrapperCol: {
    xs: { span: 20 },
    sm: { span: 20 },
  },
}
const ModalLine = ({
  visible,
  firmData,
  form,
  hideModal,
  dispatch,
  collectionData,
  payData,
  teakUserData,
  recruitData,
  logisticsModal,
}) => {
  const showModals = () => {
    dispatch({ type: 'specialLine/updateLogisticsModal', payload: { logisticsData: [] } })
    dispatch({ type: 'specialLine/logisticsDataGet', payload: { id: firmData.id } })
    dispatch({ type: 'specialLine/updateLogisticsModal', payload: { visible: true } })
  }
  // 关闭模态框
  const hideModals = () => {
    dispatch({ type: 'specialLine/updateLogisticsModal', payload: { visible: false, logisticsData: [] } })
  }
  const { getFieldDecorator } = form

  // 展示模态框公共部分
  const common = () => {
    return (
      <div className={styles.showpadding}>
        <Row>
          <Col span={11} >
            <span style={{ fontSize: '18px', fontWeight: 'bold' }}>订单编号：{firmData.orderNo}</span>
          </Col>
          {/* <Col span={6}>
            <span style={{ fontSize: '18px', fontWeight: 'bold' }}>订单类型：{firmData.orderTypeValue}</span>
          </Col> */}
          <Col span={6}>
            <span style={{ fontSize: '18px', fontWeight: 'bold' }}>订单状态：{firmData.orderTransValue}</span>
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
            <FormItem label="货物名称" {...formItemLayout}>
              {getFieldDecorator('commodityName', {
                initialValue: firmData.commodityName ? firmData.commodityName : '---',
              })(
                <Input disabled title={firmData ? firmData.commodityName : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="预估重量" {...formItemLayout}>
              {getFieldDecorator('commodityWeight', {
                initialValue: firmData.commodityWeight ? firmData.commodityWeight : '---',
              })(
                <Input disabled title={firmData ? firmData.commodityWeight : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="预估体积" {...formItemLayout}>
              {getFieldDecorator('commodityVolume', {
                initialValue: firmData.commodityVolume ? firmData.commodityVolume : '---',
              })(
                <Input disabled title={firmData ? firmData.commodityVolume : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="接运时间" {...formItemLayout}>
              {getFieldDecorator('orderTakeStart', {
                initialValue: firmData.orderTakeStart ? firmData.orderTakeStart : '---',
              })(
                <Input disabled title={firmData ? firmData.orderTakeStart : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="货物描述" {...formItemLayouts}>
              {getFieldDecorator('commodityDescribe', {
                initialValue: firmData.commodityDescribe ? firmData.commodityDescribe : '---',
              })(
                <Input disabled title={firmData ? firmData.commodityDescribe : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>车辆要求</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="车辆类型" {...formItemLayout}>
              {getFieldDecorator('transType', {
                initialValue: firmData.transTypeValue ? firmData.transTypeValue : '---',
              })(
                <Input disabled title={firmData ? firmData.transTypeValue : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="最低载重" {...formItemLayout}>
              {getFieldDecorator('maxLoad', {
                initialValue: firmData.maxLoad ? firmData.maxLoad : '---',
              })(
                <Input disabled title={firmData ? firmData.maxLoad : '---'} />
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
              {getFieldDecorator('originArea', {
                initialValue: firmData.originArea ? firmData.originArea : '---',
              })(
                <Input disabled title={firmData ? firmData.originArea : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="具体地址" {...formItemLayouts}>
              {getFieldDecorator('originAddress', {
                initialValue: firmData.originAddress ? firmData.originAddress : '---',
              })(
                <Input disabled title={firmData ? firmData.originAddress : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="联系人" {...formItemLayout}>
              {getFieldDecorator('linkMan', {
                initialValue: firmData.linkMan ? firmData.linkMan : '---',
              })(
                <Input disabled title={firmData ? firmData.linkMan : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('linkTelephone', {
                initialValue: firmData.linkTelephone ? firmData.linkTelephone : '---',
              })(
                <Input disabled title={firmData ? firmData.linkTelephone : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>送货信息</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="送货地址" {...formItemLayout}>
              {getFieldDecorator('destArea', {
                initialValue: firmData.destArea ? firmData.destArea : '---',
              })(
                <Input disabled title={firmData ? firmData.destArea : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="具体地址" {...formItemLayouts}>
              {getFieldDecorator('destAddress', {
                initialValue: firmData.destAddress ? firmData.destAddress : '---',
              })(
                <Input disabled title={firmData ? firmData.destAddress : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="联系人" {...formItemLayout}>
              {getFieldDecorator('destLinker', {
                initialValue: firmData.destLinker ? firmData.destLinker : '---',
              })(
                <Input disabled title={firmData ? firmData.destLinker : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('destTelephone', {
                initialValue: firmData.destTelephone ? firmData.destTelephone : '---',
              })(
                <Input disabled title={firmData ? firmData.destTelephone : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>付款方式</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="付款方式" {...formItemLayout}>
              {getFieldDecorator('settlementTypeValue', {
                initialValue: firmData.settlementTypeValue ? firmData.settlementTypeValue : '---',
              })(
                <Input disabled title={firmData ? firmData.settlementTypeValue : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="补充说明" {...formItemLayouts}>
              {getFieldDecorator('linkRemark', {
                initialValue: firmData.linkRemark ? firmData.linkRemark : '---',
              })(
                <Input disabled title={firmData ? firmData.linkRemark : '---'} />
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
            <FormItem label="单价" {...formItemLayout}>
              {getFieldDecorator('unitPrice', {
                initialValue: firmData.unitPrice ? firmData.unitPrice : '---',
              })(
                <Input disabled title={firmData ? firmData.unitPrice : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="预估总价" {...formItemLayout}>
              {getFieldDecorator('amount', {
                initialValue: firmData.amount ? firmData.amount : '---',
              })(
                <Input disabled title={firmData ? firmData.amount : '---'} />
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
                initialValue: firmData.createUser ? firmData.createUser : '---',
              })(
                <Input disabled title={firmData ? firmData.createUser : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系电话" {...formItemLayout}>
              {getFieldDecorator('createUsertelephone', {
                initialValue: firmData.createUsertelephone ? firmData.createUsertelephone : '---',
              })(
                <Input disabled title={firmData ? firmData.createUsertelephone : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="所属公司" {...formItemLayouts}>
              {getFieldDecorator('org', {
                initialValue: firmData.org ? firmData.org : '---',
              })(
                <Input disabled title={firmData ? firmData.org : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
      </div>
    )
  }
  // 征集条件
  const collection = () => {
    return (
      <div className={styles.showpadding}>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>征集条件</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={6}>
            <FormItem label="司机类型" {...formItemLayout}>
              {getFieldDecorator('need', {
                initialValue: collectionData.need ? collectionData.need : '---',
              })(
                <Input disabled title={collectionData.need ? collectionData.need : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="注册资金要求" {...formItemLayout}>
              {getFieldDecorator('registryMoney', {
                initialValue: collectionData.registryMoney && collectionData.registryMoney,
              })(
                <Input disabled title={collectionData.registryMoney && collectionData.registryMoney} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="保证金额度" {...formItemLayout}>
              {getFieldDecorator('bond', {
                initialValue: collectionData.bond ? collectionData.bond : '---',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="信誉等级" {...formItemLayout}>
              {getFieldDecorator('bond', {
                initialValue: collectionData.starLevel && collectionData.starLevel,
              })(
                <Input disabled />
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
              {getFieldDecorator('payTypeValue', {
                initialValue: teakUserData.teakUser ? teakUserData.teakUser : '---',
              })(
                <Input disabled title={teakUserData.teakUser ? teakUserData.teakUser : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="接单人电话" {...formItemLayout}>
              {getFieldDecorator('teakUserTelephone', {
                initialValue: teakUserData.teakUserTelephone ? teakUserData.teakUserTelephone : '---',
              })(
                <Input disabled titile={teakUserData.teakUserTelephone ? teakUserData.teakUserTelephone : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="所属公司" {...formItemLayouts}>
              {getFieldDecorator('org', {
                initialValue: teakUserData.org ? teakUserData.org : '---',
              })(
                <Input disabled title={teakUserData.org ? teakUserData.org : '---'} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="接单车牌号" {...formItemLayout}>
              {getFieldDecorator('licensePlateNumber', {
                initialValue: teakUserData.licensePlateNumber ? teakUserData.licensePlateNumber : '---',
              })(
                <Input disabled title={teakUserData.licensePlateNumber ? teakUserData.licensePlateNumber : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="接单车型" {...formItemLayout}>
              {getFieldDecorator('transTypeValue', {
                initialValue: teakUserData.transTypeValue ? teakUserData.transTypeValue : '---',
              })(
                <Input disabled title={teakUserData.transTypeValue ? teakUserData.transTypeValue : '---'} />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="接单车长" {...formItemLayout}>
              {getFieldDecorator('c', {
                initialValue: teakUserData.c ? teakUserData.c : '---',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="载重" {...formItemLayout}>
              {getFieldDecorator('d', {
                initialValue: teakUserData.d ? teakUserData.d : '---',
              })(
                <Input disabled />
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
  // 参与征集司机
  const columns = [
    {
      title: '接单人姓名',
      dataIndex: 'teakUser',
      width: 120,
    },
    {
      title: '接单人电话',
      dataIndex: 'teakUserTelephone',
      width: 120,
    },
    {
      title: '所属公司',
      dataIndex: 'organizationName',
      width: 120,
    },
    {
      title: '信誉等级',
      dataIndex: 'starLevel',
      width: 120,
    },
    {
      title: '参与时间',
      dataIndex: 'teakUserTime',
      width: 120,
    },
  ]
  const recruit = () => {
    return (
      <div>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>参与征集司机{recruitData.length}/10</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px solid  #ff9900' }} />
        <Table
          columns={columns}
          fields={{ orderId: firmData && firmData.id }}
          dataSource={recruitData}
        />
      </div>
    )
  }
  // 标题
  const titleText = {
    onlinePayment: '专线运输-订单详情-在线支付',
    monthlySettlemrnt: '专线运输-订单详情-月结',
    levyOnlinePaymentWc: '专线运输-订单详情-车辆征集-征集完成',
    levyOnlinePayment: '专线运输-订单详情-车辆征集-征集中',
  }
  return (
    <div>
      <Modal
        title={titleText[firmData.type]}
        visible={visible}
        onOk={hideModal}
        maskClosable={false}
        onCancel={hideModal}
        width={1200}
      >
        <Form>
          {common()}
          {collectionData.successInfo && collection()}
          {payData.successInfo && payment()}
          {teakUserData.successInfo && person()}
          {recruitData.successInfo ? recruit() : ''}

        </Form>
      </Modal>
      <LogisticsModal hideModal={hideModals} {...logisticsModal} />
    </div>
  )
}
ModalLine.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  collectionData: PropTypes.obj,
  payData: PropTypes.obj,
  teakUserData: PropTypes.obj,
  recruitData: PropTypes.array,
  logisticsModal: PropTypes.obj,
}
const ModalLineForm = Form.create()(ModalLine)
export default ModalLineForm
