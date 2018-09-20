import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Form, Row, Col, Input, Button, Select, InputNumber, DatePicker } from 'antd'
import { AreaSelect } from 'components'
import { regex } from 'utils'
import moment from 'moment'

import AddrsModal from './addrsModal'
import styles from './index.less'

const Option = Select.Option
const FormItem = Form.Item
const { TextArea } = Input

const telephoneChecked = (rule, value, callback) => {   // 验证手机号码是否合法
  if (value && !new RegExp(regex.phone).test(value)) {
    callback('请输入正确的手机格式')
    return
  }
  callback()
}

const FslOrder = ({ dispatch, specialLineTransport, form }) => {
  const {
    transTypes,
    visible,
    confirmLoading,
    userAddrsList,
    WeightUnits,  // 重量单位
    VolumeUnits,   // 体积单位
    CommodityTypes,
    showCountPrice,
    priceData,
    publishUnitPrice,
    saveLoading,
    goPayLoading,
  } = specialLineTransport
  const { getFieldDecorator, validateFieldsAndScroll, setFieldsValue } = form
  const transTypeObj = []
  const WeightUnitArr = []
  const VolumeUnitArr = []
  const CommodityTypeObj = []
  for (let key of Object.keys(transTypes)) {
    transTypeObj.push(<Option value={key}>{transTypes[key]}</Option>)
  }
  for (let key of Object.keys(WeightUnits)) {
    WeightUnitArr.push(<Option value={key}>{WeightUnits[key]}</Option>)
  }
  for (let key of Object.keys(VolumeUnits)) {
    VolumeUnitArr.push(<Option value={key}>{VolumeUnits[key]}</Option>)
  }
  for (let key of Object.keys(CommodityTypes)) {
    CommodityTypeObj.push(<Option value={key}>{CommodityTypes[key]}</Option>)
  }
  const options = [{
    value: 'zhejiang',
    label: 'Zhejiang',
    children: [{
      value: 'hangzhou',
      label: 'Hangzhou',
      children: [{
        value: 'xihu',
        label: 'West Lake',
      }],
    }],
  }, {
    value: 'jiangsu',
    label: 'Jiangsu',
    children: [{
      value: 'nanjing',
      label: 'Nanjing',
      children: [{
        value: 'zhonghuamen',
        label: 'Zhong Hua Men',
      }],
    }],
  }]
  const addAddr = () => {
    dispatch({ type: 'specialLineTransport/addrsModal' })
  }
  const onSubmit = (type) => {
    validateFieldsAndScroll((err, formValues) => {
      console.log(formValues)
      if (err) {
        return
      }
      formValues.orderTakeStart = formValues.orderTakeStart.format('YYYY-MM-DD HH:mm')
      formValues.destLimit = formValues.destLimit.format('YYYY-MM-DD HH:mm')
      formValues.commodityClass = 'DEFAULT'
      if (formValues.commodityType !== 'Other') {
        formValues.unitPrice = parseFloat(formValues.unitPrice)
        delete formValues.distance
        delete formValues.terraceUnitPrice
        delete formValues.price
      }
      dispatch({
        type: 'specialLineTransport/changeStates',
        payload: {
          [`${type}Loading`]: true,
        },
      })
      dispatch({
        type: 'specialLineTransport/createOrder',
        payload: { body: formValues, type },
      })
    })
  }
  const onChangeOriginArea = (value, selectedOptions) => {
    let formatAddr = selectedOptions.map(o => o.label).join(' ')
    setFieldsValue({ originArea: formatAddr })
  }
  const countPrice = (data) => {
    dispatch({
      type: 'specialLineTransport/countPrice',
      payload: data,
    })
  }
  const TypeChange = () => {
    const formValue = form.getFieldsValue()
    console.log('formValue', formValue)
    let {
      commodityType,
      commodityWeight,
      commodityVolume,
      originAreaCode,
      destAreaCode,
      originAddress,
      destAddress,
    } = formValue
    if (commodityType && commodityWeight && commodityVolume && originAreaCode && destAreaCode && originAddress && destAddress) {
      if (commodityType === 'Light' || commodityType === 'Heavy') {
        const obj = {
          commodityType,
          commodityWeight,
          commodityVolume,
          originAreaCode,
          destAreaCode,
          originAddress,
          destAddress,
        }
        countPrice(obj)
      }
    }
  }
  const countUnitPrice = () => {
    const formValue = form.getFieldsValue()
    let unitPrice = 0
    let {
      commodityType,
      commodityWeight,
      commodityVolume,
      distance,
      amount,
    } = formValue
    if (commodityType && commodityWeight && commodityVolume && distance && amount) {
      if (commodityType === 'Light') {
        unitPrice = `${(amount / (commodityVolume * parseFloat(distance))).toFixed(2)}元/m³/KM`
      } else {
        unitPrice = `${(amount / (commodityWeight * parseFloat(distance))).toFixed(2)}元/KG/KM`
      }
      console.log('distance', parseFloat(distance))
      console.log('unitPrice', unitPrice)
      dispatch({
        type: 'specialLineTransport/changeStates',
        payload: {
          publishUnitPrice: unitPrice,
        },
      })
    }
  }
  // const props = {
  //   province: {
  //     label: '取货地址',
  //     key: 'ccc',
  //     outKey: 'outCode',
  //     required: true,
  //   },
  //   form,
  // }
  const addrProps = {
    visible,
    dispatch,
    confirmLoading,
    userAddrsList,
  }
  const numberProps = {
    max: 99999999,
    min: 0.00,
    precision: 2,
  }
  const getInfo = {
    province: {
      label: '取货地址',
      key: 'quProvince',
      outKey: 'originAreaCode',
      required: true,
    },
    form,
    TypeChange,
    // province: {
    //   key: 'quProvince',
    //   label: '取货地址(省)',
    // },
    // city: {
    //   key: 'quCity',
    //   label: '市',
    // },
    // area: {
    //   key: 'originAreaCode',
    //   label: '区',
    // },
    // form,
    // TypeChange,
  }
  const shouInfo = {
    province: {
      label: '收货地址',
      key: 'shouProvince',
      outKey: 'destAreaCode',
      required: true,
    },
    form,
    TypeChange,
    // province: {
    //   key: 'shouProvince',
    //   label: '收货地址(省)',
    // },
    // city: {
    //   key: 'shouCity',
    //   label: '市',
    // },
    // area: {
    //   key: 'destAreaCode',
    //   label: '区',
    // },
    // form,
    // TypeChange,
  }
  const isShowPrice = (value) => {
    if (value === 'Other') {
      dispatch({
        type: 'specialLineTransport/changeStates',
        payload: {
          showCountPrice: false,
        },
      })
    } else {
      dispatch({
        type: 'specialLineTransport/changeStates',
        payload: {
          showCountPrice: true,
        },
      })
    }
  }
  return (<Form className={styles.form}>
    <Row>
      <Col>
        <Card title="货物或详情">
          <Row gutter="48">
            <Col span="12">
              <FormItem label="货物名称">
                {getFieldDecorator('commodityName', {
                  rules: [
                    {
                      required: true,
                      message: '请输入货物名称',
                    },
                  ],
                })(
                  <Input maxLength="30" placeholder="请输入货物名称" />
                )}
              </FormItem>
              <FormItem label="货物类型">
                {getFieldDecorator('commodityType', {
                  rules: [
                    {
                      required: true,
                      message: '请选择货物名称',
                    },
                  ],
                })(
                  <Select placeholder="请选择货物类型" onBlur={TypeChange} onChange={isShowPrice}>
                    {
                      CommodityTypeObj
                    }
                  </Select>
                )}
              </FormItem>
              <Row gutter={0}>
                <Col span="24">
                  <FormItem label="预估重量">
                    {getFieldDecorator('commodityWeight', {
                      rules: [
                        {
                          required: true,
                          message: '请输入预估重量',
                        },
                        { pattern: regex.number, message: '请输入正确的重量' },
                      ],
                    })(<Input {...numberProps} placeholder="请输入预估重量" addonAfter={<span>吨</span>} onBlur={TypeChange} />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col span="24">
                  <FormItem label="预估体积">
                  {getFieldDecorator('commodityVolume', {
                    rules: [
                      {
                        required: true,
                        message: '请输入预估体积',
                      },
                      { pattern: regex.number, message: '请输入正确的体积' },
                    ],
                  })(
                    <Input {...numberProps} addonAfter={<span>m³</span>} placeholder="请输入预估体积" onBlur={TypeChange} />
                  )}
                  </FormItem>
                </Col>
              </Row>
            </Col>
            <Col span="12">
              <FormItem label="货物描述">
                {getFieldDecorator('commodityDescribe', {
                  rules: [
                    {
                      required: true,
                      message: '请输入货物描述',
                    },
                  ],
                })(
                  <TextArea maxLength="100" autosize={{ minRows: 11, maxRows: 18 }} placeholder="货物描述" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Col>
      <Col>
        <Card title="汽车要求">
          <Row gutter="48">
            <Col span="12">
              <FormItem label="车辆类型">
                {getFieldDecorator('transType', {
                  initialValue: 'UNLIMITED',
                  rules: [
                    {
                      required: true,
                      message: '请选择车辆类型',
                    },
                  ],
                })(
                  <Select
                    placeholder="请选择车辆类型"
                  >
                    {transTypeObj}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <Row gutter={0}>
                <Col span="24">
                  <FormItem label="最低载重">
                    {getFieldDecorator('maxLoad', {
                      rules: [
                        {
                          required: true,
                          message: '请输入最低载重',
                        },
                        { pattern: regex.number, message: '请输入正确的重量' },
                      ],
                    })(
                      <Input {...numberProps} placeholder="请输入最低载重" addonAfter={<span>吨</span>} />
                    )}
                  </FormItem>
                </Col>
                {/* <Col span="8">
                  <FormItem label=" " colon={false}>
                    {getFieldDecorator('WeightUnit', {
                      initialValue: 'T',
                    })(
                      <Select defaultValue="T" >
                        {WeightUnitArr}
                      </Select>
                    )}
                  </FormItem>
                </Col> */}
              </Row>
            </Col>
          </Row>
        </Card>
      </Col>
      {/* <Col>
        <Card title="费用">
          <Row gutter="48">
            <Col span="12">
              <FormItem label="单价">
                {getFieldDecorator('unitPrice', {
                  rules: [
                    {
                      required: true,
                      message: '请输入单价',
                    },
                  ],
                })(
                  <InputNumber {...numberProps} placeholder="请输入单价" />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="预估总价">
                {getFieldDecorator('amount', {
                  rules: [
                    {
                      required: true,
                      message: '请输入预估总价',
                    },
                  ],
                })(
                  <InputNumber {...numberProps} placeholder="请输入预估总价" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Col> */}
      <Col>
        <Card
          title="取货信息"
        >
          <Row gutter="48">
            <Col span="12">
              <FormItem label="取货联系人">
                {getFieldDecorator('linkMan', {
                  rules: [
                    {
                      required: true,
                      message: '请输入取货联系人',
                    },
                  ],
                })(
                  <Input maxLength="30" placeholder="请输入取货联系人" />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="取货电话">
                {getFieldDecorator('linkTelephone', {
                  rules: [
                    {
                      required: true,
                      message: '请输入取货电话',
                    },
                    { validator: telephoneChecked },
                  ],
                })(
                  <InputNumber placeholder="请输入取货电话" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row gutter="48">
            <Col span="12">
              <AreaSelect {...getInfo} placeholder="请选择取货地址" />
            </Col>
            <Col span="12">
              <FormItem label="取货详细地址">
                {getFieldDecorator('originAddress', {
                  rules: [
                    {
                      required: true,
                      message: '请输入取货详细地址',
                    },
                  ],
                })(
                  <Input maxLength="60" placeholder="请输入详细地址" onBlur={TypeChange} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="12">
              <FormItem label="接运时间">
                {getFieldDecorator('orderTakeStart', {
                  rules: [
                    {
                      required: true,
                      message: '请输入接运时间',
                    },
                  ],
                })(
                  <DatePicker showTime format="YYYY-MM-DD HH:mm" placeholder="请输入接运时间" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Col>
      <Col>
        <Card
          title="送货信息"
        >
          <Row gutter="48">
            <Col span="12">
              <FormItem label="收货联系人">
                {getFieldDecorator('destLinker', {
                  rules: [
                    {
                      required: true,
                      message: '请输入收货联系人',
                    },
                  ],
                })(
                  <Input maxLength="30" placeholder="请输入收货联系人" />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="收货电话">
                {getFieldDecorator('destTelephone', {
                  rules: [
                    {
                      required: true,
                      message: '请输入收货电话',
                    },
                    { validator: telephoneChecked },
                  ],
                })(
                  <InputNumber placeholder="请输入收货电话" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row gutter="48">
            <Col span="12">
              <AreaSelect {...shouInfo} placeholder="请选择收货地址" />
            </Col>
            <Col span="12">
              <FormItem label="送货详细地址">
                {getFieldDecorator('destAddress', {
                  rules: [
                    {
                      required: true,
                      message: '请输入送货详细地址',
                    },
                  ],
                })(
                  <Input maxLength="60" placeholder="请输入详细地址" onBlur={TypeChange} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span="12">
              <FormItem label="送达时间">
                {getFieldDecorator('destLimit', {
                  rules: [
                    {
                      required: true,
                      message: '请输入送达时间',
                    },
                  ],
                })(
                  <DatePicker showTime format="YYYY-MM-DD HH:mm" placeholder="请输入送达时间" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Col>
      <Col>
        <Card title="其他信息">
          <Row gutter="48">
            <Col >
              <FormItem label="补充说明">
                {getFieldDecorator('linkRemark', {
                  rules: [
                    {
                      required: true,
                      message: '请输入补充说明',
                    },
                  ],
                })(
                  <TextArea autosize={{ minRows: 2, maxRows: 6 }} placeholder="补充说明" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Col>
      {
        showCountPrice ? (
          <Col>
            <Card title="费用参考及选择">
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="平台参考距离">
                    {getFieldDecorator('distance', {
                      initialValue: priceData && priceData.distance,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="平台参考总价">
                    {getFieldDecorator('price', {
                      initialValue: priceData && priceData.price,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="平台参考单价">
                    {getFieldDecorator('terraceUnitPrice', {
                      initialValue: priceData && priceData.unitPrice,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="发布总价">
                    {getFieldDecorator('amount', {
                      rules: [
                        {
                          required: true,
                          message: '请输入补充说明',
                        },
                      ],
                    })(
                      <InputNumber placeholder="请输入发布总价" onBlur={countUnitPrice} />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="发布单价">
                    {getFieldDecorator('unitPrice', {
                      initialValue: publishUnitPrice || '',
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <div style={{ padding: '15px' }}>
                <span style={{ fontSize: '18px', fontWeight: 'bold' }}>温馨提示</span>
                <div style={{ padding: '15px' }}>
                  <p>系统参考价格,按以下规则进行计算</p>
                  <p>1.重货:按KG/KM计价</p>
                  <p>2.轻货:按m³/KM计价</p>
                  <p>3.特殊商品:由于情况复杂,平台不做价格计算由客户自定义总价</p>
                </div>
              </div>
            </Card>
          </Col>
        ) : (
          <Card title="费用">
            <Row gutter="48">
              <Col span="12">
                <FormItem label="发布总价">
                  {getFieldDecorator('amount', {
                    rules: [
                      {
                        required: true,
                        message: '请输入补充说明',
                      },
                    ],
                  })(
                    <InputNumber placeholder="请输入发布总价" />
                  )}
                </FormItem>
              </Col>
            </Row>
          </Card>
        )
      }
      <Col>
        <Card className={styles['bottem-card']}>
          <Col className={styles['bottem-btns']} >
            <Button type="primary" loading={confirmLoading} className={styles.save} onClick={onSubmit.bind(this, 'save')} loading={saveLoading}>保 存</Button>
            <Button type="primary" loading={confirmLoading} className={styles['go-pay']} onClick={onSubmit.bind(this, 'goPay')} loading={goPayLoading}>去 支 付</Button>
          </Col>
        </Card>
      </Col>
      {visible && <AddrsModal {...addrProps} />}
    </Row>
  </Form>)
}

FslOrder.propTypes = {
  form: PropTypes.isRequired,
  specialLineTransport: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const FslOrderForm = Form.create()(FslOrder)
export default connect(({ specialLineTransport }) => ({ specialLineTransport }))(FslOrderForm)
