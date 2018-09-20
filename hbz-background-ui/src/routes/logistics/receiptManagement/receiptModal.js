import React from 'react'
import { Modal, Form, Col, Row, Input, Button, Select, InputNumber, Radio, DatePicker } from 'antd'
import PropTypes from 'prop-types'
import moment from 'moment'
import { AreaSelect } from 'components'
import styles from './index.less'
import GoodsModal from './goodsModal'
import UserModal from './userModal'
const FormItem = Form.Item
const RadioGroup = Radio.Group
const { TextArea } = Input
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
// 要计算的参数
let premium = 0
let transall = 0
const ReceiptModal = ({
  visible,
  firmData,
  form,
  dispatch,
  packageData,
  statusData,
  paymentMethodData,
  ratioData,
  totalCost,
  loading,
  provinceFromData,
  cityFromData,
  countyFromData,
  provinceToData,
  cityToData,
  countyToData,
  serviceData,
  goodsModal,
  goodsMessage,
  userModal,
  getuserMessage,
  senduserMessage,
}) => {
  const { getFieldDecorator } = form
  const queryFirm = (type) => {
    dispatch({ type: 'receiptManagement/updateuserModal', payload: { visible: true, type } })
  }
  // 选择货物
  const queryGoods = () => {
    dispatch({ type: 'receiptManagement/updategoodsModal', payload: { visible: true } })
  }
  const handleSubmit = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.receiptDate = new Date(formValue.receiptDate).getTime()
      dispatch({ type: 'receiptManagement/updatefirmModal', payload: { loading: true } })
      if (firmData.id) {
        formValue.id = firmData.id
        formValue.shipperUserId = firmData.shipperUserId
        formValue.receiverUserId = firmData.receiverUserId
        formValue.feeId = firmData.feeId
        dispatch({ type: 'receiptManagement/updateMessage', payload: formValue })
      } else {
        dispatch({ type: 'receiptManagement/addMessage', payload: formValue })
      }
    })
  }
  // 关闭模态框
  const hideModal = () => {
    dispatch({ type: 'receiptManagement/cleanModal' })
    form.resetFields()
  }
  // 重置
  const cleanFirm = () => {
    form.resetFields()
    dispatch({ type: 'receiptManagement/update', payload: { goodsMessage: {},
      senduserMessage: {},
      getuserMessage: {},
    },
    })
  }
  // 获取保费
  let a = form.getFieldValue('declareValue') || 0
  let b = form.getFieldValue('ratio') || 0
  b = b / 100
  premium = a * b
  // 运费计算
  let c = form.getFieldValue('billingWeight') || 0
  let d = form.getFieldValue('price') || 0
  let e = form.getFieldValue('volume') || 0
  let f = form.getFieldValue('amount') || 0
  let values = form.getFieldValue('billingMethod') || null
  if (values === '0') {
    transall = c * d
  } else if (values === '1') {
    transall = e * d
  } else if (values === '2') {
    transall = f * d
  } else {
    transall = 0
  }
  // 总费用计算
  let thf = form.getFieldValue('deliveryFee') || 0
  let shf = form.getFieldValue('deliveryCharges') || 0
  let bzf = form.getFieldValue('packagingFee') || 0
  let qtf = form.getFieldValue('otherFee') || 0
  totalCost = transall + thf + shf + premium + bzf + qtf


  const fromInfo = {
    province: {
      label: '发站城市',
      key: 'startCitys',
      outKey: 'originAreaCode',
      required: true,
    },
    updateData: (firmData && firmData.id) ? firmData.startCity : [],
    code: (firmData && firmData.id) ? firmData.originAreaCode : '',
    form,
    formItemLayout,
  }
  const toInfo = {
    province: {
      label: '到站城市',
      key: 'endCitys',
      outKey: 'destAreaCode',
      required: true,
    },
    updateData: (firmData && firmData.id) ? firmData.endCity : [],
    code: (firmData && firmData.id) ? firmData.destAreaCode : '',
    form,
    formItemLayout,
  }
  // 展示模态框公共部分
  const common = () => {
    return (
      <div className={styles.showpadding}>
        <Row>
          <Col span={6}>
            <FormItem label="收运来源" {...formItemLayout}>
              {getFieldDecorator('come', {
                initialValue: firmData.come && firmData.come,
                rules: [{ required: true, message: '请填写来源' }],
              })(
                <Select style={{ width: '100%' }}>
                  <Option key="0" >平台指定</Option>
                  <Option key="1" >网点自由</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="运单号" {...formItemLayout}>
              {getFieldDecorator('waybillNumber', {
                initialValue: firmData && firmData.waybillNumber,
                rules: [{ required: true, message: '请填写运单号' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="收运时间" {...formItemLayout}>
            {getFieldDecorator('receiptDate', {
              initialValue: (firmData && firmData.receiptDate) ? moment(firmData.receiptDate) : moment(new Date(), 'YYYY-MM-DD HH:MM:SS'),
              rules: [{ required: true, message: '请填写收运时间' }],
            })(
              <DatePicker
                showTime
                format="YYYY-MM-DD HH:mm:ss"
              />
            )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="收运类型" {...formItemLayout}>
              {getFieldDecorator('goodsType', {
                initialValue: firmData && firmData.goodsType,
                rules: [{ required: true, message: '请填写收运类型' }],
              })(
                <Select style={{ width: '100%' }}>
                  <Option key="0" >零担</Option>
                  <Option key="1" >整车</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>发站到站</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000', marginBottom: '10px' }} />
        <Row>
          <Col span={6}>
            <AreaSelect
              {...fromInfo}
              placeholder="请选择"
            />
          </Col>
          <Col span={6}>
            <AreaSelect
              {...toInfo}
              placeholder="请选择"
            />
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>托运方</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000', marginBottom: '10px' }} />
        <Row>
          <Col span={6}>
            <FormItem label="单位名称" {...formItemLayout}>
              {getFieldDecorator('shipperUserCompanyName', {
                initialValue: getuserMessage.id ? getuserMessage.companyName : firmData && firmData.shipperUserCompanyName,
                rules: [{ required: true, message: '请填写单位名称' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系人" {...formItemLayout}>
              {getFieldDecorator('shipperUserName', {
                initialValue: getuserMessage.id ? getuserMessage.userName : firmData && firmData.shipperUserName,
                rules: [{ required: true, message: '请填写联系人' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="电话或手机" {...formItemLayout}>
              {getFieldDecorator('shipperUserTelephone', {
                initialValue: getuserMessage.id ? getuserMessage.userTelephone : firmData && firmData.shipperUserTelephone,
                rules: [{ required: true, message: '请填写正确的手机号码', pattern: RegExp('^0\\d{2,3}-?\\d{7,8}$|^(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])\\d{8}$') }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="邮编" {...formItemLayout}>
              {getFieldDecorator('shipperUserZipCode', {
                initialValue: firmData.id && firmData.shipperUserZipCode,
                rules: [{ required: true, message: '请填写邮编' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="地址" {...formItemLayouts}>
              {getFieldDecorator('shipperUserAddress', {
                initialValue: getuserMessage.id ? getuserMessage.userAddress : firmData && firmData.shipperUserAddress,
                rules: [{ required: true, message: '请填写地址' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Button type="primary" onClick={queryFirm.bind(this, 'get')}>选择已有公司</Button>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>收货方</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000', marginBottom: '10px' }} />
        <Row>
          <Col span={6}>
            <FormItem label="单位名称" {...formItemLayout}>
              {getFieldDecorator('receiverUserCompanyName', {
                initialValue: senduserMessage.id ? senduserMessage.companyName : firmData && firmData.receiverUserCompanyName,
                rules: [{ required: true, message: '请填写单位名称' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="联系人" {...formItemLayout}>
              {getFieldDecorator('receiverUserName', {
                initialValue: senduserMessage.id ? senduserMessage.userName : firmData && firmData.receiverUserName,
                rules: [{ required: true, message: '请填写联系人' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="电话或手机" {...formItemLayout}>
              {getFieldDecorator('receiverUserTelephone', {
                initialValue: senduserMessage.id ? senduserMessage.userTelephone : firmData && firmData.receiverUserTelephone,
                rules: [{ required: true, message: '请填写正确的手机号码', pattern: RegExp('^0\\d{2,3}-?\\d{7,8}$|^(13[0-9]|15[0-9]|18[0-9]|14[0-9]|17[0-9])\\d{8}$') }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="邮编" {...formItemLayout}>
              {getFieldDecorator('receiverUserZipCode', {
                initialValue: firmData.id && firmData.receiverUserZipCode,
                rules: [{ required: true, message: '请填写邮编' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="地址" {...formItemLayouts}>
              {getFieldDecorator('receiverUserAddress', {
                initialValue: senduserMessage.id ? senduserMessage.userAddress : firmData && firmData.receiverUserAddress,
                rules: [{ required: true, message: '请填写地址' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Button type="primary" onClick={queryFirm.bind(this, 'send')}>选择已有公司</Button>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>货物信息</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000', marginBottom: '10px' }} />
        <Row>
          <Col span={6}>
            <FormItem label="货物编号" {...formItemLayout}>
              {getFieldDecorator('commodityNumber', {
                initialValue: goodsMessage.id ? goodsMessage.commodityNumber : firmData && firmData.commodityNumber,
                rules: [{ required: true, message: '请填写货物编号' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="货物名称" {...formItemLayout}>
              {getFieldDecorator('commodityName', {
                initialValue: goodsMessage.id ? goodsMessage.commodityName : firmData && firmData.commodityName,
                rules: [{ required: true, message: '请填写货物名称' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="包装单位" {...formItemLayout}>
              {getFieldDecorator('packageUnit', {
                initialValue: goodsMessage.id ? goodsMessage.packageUnit : firmData && firmData.packageUnit,
                rules: [{ required: true, message: '请填写包装单位' }],
              })(
                <Select style={{ width: '100%' }}>
                  {packageData && packageData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="计费重量(KG)" {...formItemLayout}>
              {getFieldDecorator('billingWeight', {
                initialValue: goodsMessage.id ? goodsMessage.billingWeight : firmData && firmData.billingWeight,
                rules: [{ required: true, message: '请填写计费重量' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="体积(m³)" {...formItemLayout}>
              {getFieldDecorator('volume', {
                initialValue: goodsMessage.id ? goodsMessage.volume : firmData && firmData.volume,
                rules: [{ required: true, message: '请填写体积(m³)' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="单价(元)" {...formItemLayout}>
              {getFieldDecorator('price', {
                initialValue: goodsMessage.id ? goodsMessage.price : firmData.price && firmData.price,
                rules: [{ required: true, message: '请填写单价(元)' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Button type="primary" onClick={queryGoods.bind(this)}>选择已有货物</Button>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="声明价值" {...formItemLayout}>
              {getFieldDecorator('declareValue', {
                initialValue: firmData && firmData.declareValue,
                rules: [{ required: false, message: '请填写声明价值' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="保率" {...formItemLayout}>
              {getFieldDecorator('ratio', {
                initialValue: firmData && firmData.ratio,
                rules: [{ required: true, message: '请填写保率' }],
              })(
                // <InputNumber />
                <Select style={{ width: '100%' }}>
                  {ratioData && ratioData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="实际重量(KG)" {...formItemLayout}>
              {getFieldDecorator('weight', {
                initialValue: firmData && firmData.weight,
                rules: [{ required: true, message: '请填写实际重量(KG)' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="数量" {...formItemLayout}>
              {getFieldDecorator('amount', {
                initialValue: firmData && firmData.amount,
                rules: [{ required: true, message: '请填写数量' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="包装状态" {...formItemLayout}>
              {getFieldDecorator('packagingStatus', {
                initialValue: firmData && firmData.packagingStatus,
                rules: [{ required: true, message: '请填写包装状态' }],
              })(
                <Select style={{ width: '100%' }}>
                  {statusData && statusData.map(item => <Option key={item.val}>{item.name}</Option>)}
                </Select>
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
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000', marginBottom: '10px' }} />
        <Row>
          <Col span={6}>
            <FormItem label="计费方式" {...formItemLayout}>
              {getFieldDecorator('billingMethod', {
                initialValue: firmData && firmData.billingMethod,
                rules: [{ required: true, message: '请填写计费方式' }],
              })(
                <Select style={{ width: '100%' }} >
                  <Option key="0" >重量*单价</Option>
                  <Option key="1" >体积*单价</Option>
                  <Option key="2" >数量*单价</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="运费" {...formItemLayout}>
              {getFieldDecorator('shippingCosts', {
                initialValue: transall,
                rules: [{ required: true, message: '请填写运费' }],
              })(
                <InputNumber disabled />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="提货费" {...formItemLayout}>
              {getFieldDecorator('deliveryFee', {
                initialValue: firmData && firmData.deliveryFee,
                rules: [{ required: false, message: '请填写提货费' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="送货费" {...formItemLayout}>
              {getFieldDecorator('deliveryCharges', {
                initialValue: firmData && firmData.deliveryCharges,
                rules: [{ required: false, message: '请填写送货费' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="保费" {...formItemLayout}>
              {getFieldDecorator('premium', {
                initialValue: premium,
                rules: [{ required: true, message: '请填写保费' }],
              })(
                <InputNumber disabled />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="包装费" {...formItemLayout}>
              {getFieldDecorator('packagingFee', {
                initialValue: firmData && firmData.packagingFee,
                rules: [{ required: false, message: '请填写包装费' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="其他费用" {...formItemLayout}>
              {getFieldDecorator('otherFee', {
                initialValue: firmData && firmData.otherFee,
                rules: [{ required: false, message: '请填写其他费用' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="付款方式" {...formItemLayouts}>
              {getFieldDecorator('paymentMethod', {
                initialValue: firmData && firmData.paymentMethod,
                rules: [{ required: true, message: '请填写付款方式' }],
              })(
                <RadioGroup>
                  {paymentMethodData && paymentMethodData.map(item => <Radio value={item.val}>{item.name}</Radio>)}
                </RadioGroup>
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold', color: 'red', marginBottom: '10px' }}>总运费：{totalCost} 元</span>
          </Col>
        </Row>
        <Row>
          <Col span={6}>
            <FormItem label="代收款" {...formItemLayout}>
              {getFieldDecorator('onCollection', {
                initialValue: firmData && firmData.onCollection,
                rules: [{ required: false, message: '请填写代收款' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="中转费" {...formItemLayout}>
              {getFieldDecorator('transferFee', {
                initialValue: firmData && firmData.transferFee,
                rules: [{ required: false, message: '请填写中转费' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="垫付货款" {...formItemLayout}>
              {getFieldDecorator('advancePayment', {
                initialValue: firmData && firmData.advancePayment,
                rules: [{ required: false, message: '请填写垫付货款' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000', marginBottom: '10px' }} />
        <Row>
          <Col span={8}>
            <span style={{ fontSize: '15px', fontWeight: 'bold' }}>其他信息</span>
          </Col>
        </Row>
        <hr style={{ height: '1px', border: 'none', 'border-top': '1px dashed #000' }} />
        <Row>
          <Col span={6}>
            <FormItem label="是否回签单" {...formItemLayout}>
              {getFieldDecorator('isReceipt', {
                initialValue: firmData && firmData.isReceipt,
                rules: [{ required: true, message: '请填写是否回单' }],
              })(
                <RadioGroup>
                  <Radio value="0">否</Radio>
                  <Radio value="1">是</Radio>
                </RadioGroup>
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="等话放货" {...formItemLayout}>
              {getFieldDecorator('isDelivery', {
                initialValue: firmData && firmData.isDelivery,
                rules: [{ required: true, message: '请填写是否等话放货' }],
              })(
                <RadioGroup>
                  <Radio value="0">否</Radio>
                  <Radio value="1">是</Radio>
                </RadioGroup>
              )}
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem label="服务方式" {...formItemLayouts}>
              {getFieldDecorator('serviceMethodType', {
                initialValue: firmData && firmData.serviceMethodType,
                rules: [{ required: true, message: '请填写服务方式' }],
              })(
                <RadioGroup>
                  {serviceData && Object.keys(serviceData).map(key => <Radio value={key}>{serviceData[key]}</Radio>)}
                </RadioGroup>
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="中转站" {...formItemLayout}>
              {getFieldDecorator('inWar', {
                initialValue: firmData && firmData.inWar,
                rules: [{ required: false, message: '中转站' }],
              })(
                <Input />
              )}
            </FormItem>
          </Col>
          <Col span={6}>
            <FormItem label="回单数" {...formItemLayout}>
              {getFieldDecorator('receiptNumber', {
                initialValue: firmData && firmData.receiptNumber,
                rules: [{ required: false, message: '回单数' }],
              })(
                <InputNumber />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <FormItem label="备注" {...formItemLayouts}>
              {getFieldDecorator('remarks', {
                initialValue: firmData && firmData.remarks,
                rules: [{ required: false, message: '备注' }],
              })(
                <TextArea rows={3} />
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
        title={firmData.id ? '修改收运订单' : '新增收运订单'}
        visible={visible}
        onOk={handleSubmit}
        maskClosable={false}
        onCancel={hideModal}
        width={1300}
        footer={null}
      >
        <Form>
          {common()}
          <Row className={styles.buttonPosition}>
            <Button type="primary" onClick={handleSubmit.bind(this)} loading={loading}>确定</Button>
            <Button type="primary" onClick={hideModal.bind(this)}>取消</Button>
            {firmData.id ? '' : <Button type="danger" onClick={cleanFirm.bind(this)} >重置</Button>}
          </Row>
        </Form>
        {goodsModal.visible && <GoodsModal {...goodsModal} dispatch={dispatch} formBig={form} />}
        {userModal.visible && <UserModal {...userModal} dispatch={dispatch} formBig={form} />}
      </Modal>
    </div>
  )
}
ReceiptModal.propTypes = {
  form: PropTypes.obj,
  visible: PropTypes.bool,
  firmData: PropTypes.object,
  hideModal: PropTypes.func,
  dispatch: PropTypes.func,
  packageData: PropTypes.array,
  statusData: PropTypes.array,
  paymentMethodData: PropTypes.array,
  totalCost: PropTypes.number,
  loading: PropTypes.bool,
  provinceFromData: PropTypes.array,
  cityFromData: PropTypes.array,
  countyFromData: PropTypes.array,
  provinceToData: PropTypes.array,
  cityToData: PropTypes.array,
  countyToData: PropTypes.array,
  serviceData: PropTypes.array,
  goodsModal: PropTypes.obj,
  goodsMessage: PropTypes.obj,
  userModal: PropTypes.obj,
  getuserMessage: PropTypes.obj,
  senduserMessage: PropTypes.obj,
  ratioData: PropTypes.array,
}
const ReceiptModalForm = Form.create()(ReceiptModal)
export default ReceiptModalForm
