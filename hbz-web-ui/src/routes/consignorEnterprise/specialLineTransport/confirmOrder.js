import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, Radio, message } from 'antd'
import { Iconfont } from 'components'
import { browserHistory } from 'react-router'
import { address } from 'utils'

import styles from './index.less'

const RadioGroup = Radio.Group
const RadioButton = Radio.Button
const FormItem = Form.Item
const { TextArea } = Input

const FslOrder = ({ dispatch, specialLineTransport, form }) => {
  const { orderDetail, SettlementType, SettlementTypeObj } = specialLineTransport
  const { getFieldDecorator } = form

  const selected = () => {
    if (SettlementType) {
      dispatch({ type: 'specialLineTransport/selectedTlementType' })
    } else {
      message.warn('请选择结算方式')
      return
    }
  }
  const onChange = (e) => {
    dispatch({
      type: 'specialLineTransport/changeStates',
      payload: { SettlementType: e.target.value },
    })
  }
  const getArea = (area) => {
    let areaName = ''
    if (orderDetail[area].level === 2) {
      areaName = `${orderDetail[area].level1Name} / ${orderDetail[area].level2Name}`
    } else {
      areaName = `${orderDetail[area].level1Name} / ${orderDetail[area].level2Name} / ${orderDetail[area].level3Name}`
    }
    return areaName
  }
  return (<Form className={styles.form}>
    <Row>
      <Col>
        <Card className={styles['order-title-card']}>
          <h2 className={styles['confirm-order-title']}>
            订单编号：{orderDetail.orderNo}
          </h2>
        </Card>
      </Col>
      <Col>
        <Card title="货物或详情">
          <Row gutter="48">
            <Col span="12">
              <FormItem label="货物名称">
                {getFieldDecorator('commodityName', {
                  initialValue: orderDetail.commodityName,
                })(
                  <Input disabled placeholder="请输入货物名称" />
                )}
              </FormItem>
              <Row gutter={0}>
                <Col span="24">
                  <FormItem label="预估重量">
                    {getFieldDecorator('commodityWeight', {
                      initialValue: orderDetail.commodityWeight,
                    })(
                      <Input disabled placeholder="请输入预估重量" addonAfter={<span>吨</span>} />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col span="24">
                  <FormItem label="预估体积">
                    {getFieldDecorator('commodityVolume', {
                      initialValue: orderDetail.commodityVolume,
                    })(
                      <Input disabled placeholder="请输入预估体积" addonAfter={<span>m³</span>} />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Col>
            <Col span="12">
              <FormItem label="货物描述">
                {getFieldDecorator('commodityDescribe', {
                  initialValue: orderDetail.commodityDescribe,
                })(
                  <TextArea disabled autosize={{ minRows: 11, maxRows: 18 }} placeholder="货物描述" />
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
                  initialValue: orderDetail.transType || 'UNLIMITED',
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <Row gutter={0}>
                <Col span="24">
                  <FormItem label="最低载重">
                    {getFieldDecorator('maxLoad', {
                      initialValue: orderDetail.maxLoad,
                    })(
                      <Input disabled placeholder="请输入最低载重" addonAfter={<span>吨</span>} />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Col>
          </Row>
        </Card>
      </Col>
      <Col>
        <Card title="费用">
          <Row gutter="48">
            <Col span="12">
              <FormItem label="单价">
                {getFieldDecorator('unitPrice', {
                  initialValue: orderDetail.unitPrice,
                })(
                  <Input disabled placeholder="请输入单价" />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="预估总价">
                {getFieldDecorator('amount', {
                  initialValue: orderDetail.amount,
                })(
                  <Input disabled placeholder="请输入预估总价" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Col>
      <Col>
        <Card
          title="取货信息"
        >
          <Row gutter="48">
            <Col span="12">
              <FormItem label="取货联系人">
                {getFieldDecorator('linkMan', {
                  initialValue: orderDetail.linkMan,
                })(
                  <Input disabled maxLength="30" placeholder="请输入取货联系人" />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="取货电话">
                {getFieldDecorator('linkTelephone', {
                  initialValue: orderDetail.linkTelephone,
                })(
                  <Input disabled placeholder="请输入取货电话" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="取货地址">
                {getFieldDecorator('originAreaCode', {
                  initialValue: getArea('originArea'),
                })(<Input disabled placeholder="请输入取货地址" />)}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="取货详细地址">
                {getFieldDecorator('originAddress', {
                  initialValue: orderDetail.originAddress,
                })(<Input disabled placeholder="请输入取货详细地址" />)}
              </FormItem>
            </Col>
          </Row>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="接运时间">
                {getFieldDecorator('orderTakeStart', {
                  initialValue: orderDetail.orderTakeStart,
                })(
                  <Input disabled placeholder="请输入接运时间" />
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
                  initialValue: orderDetail.destLinker,
                })(
                  <Input disabled maxLength="30" placeholder="请输入收货联系人" />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="收货电话">
                {getFieldDecorator('destTelephone', {
                  initialValue: orderDetail.destTelephone,
                })(
                  <Input disabled placeholder="请输入收货电话" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="收货地址">
                {getFieldDecorator('destAreaCode', {
                  initialValue: getArea('destArea'),
                })(<Input disabled placeholder="请输入收货地址" />)}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="收货详细地址">
                {getFieldDecorator('destAddress', {
                  initialValue: orderDetail.destAddress,
                })(
                  <Input disabled placeholder="请输入详细地址" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="送达时间">
                {getFieldDecorator('destLimit', {
                  initialValue: orderDetail.destLimit,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Col>
      <Col>
        <Card title="订单人信息">
          <Row gutter="48">
            <Col >
              <FormItem label="补充说明">
                {getFieldDecorator('linkRemark', {
                  initialValue: orderDetail.linkRemark,
                })(
                  <TextArea disabled autosize={{ minRows: 2, maxRows: 6 }} placeholder="补充说明" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Col>
      <Col>
        <Card className={styles['select-style-pay']} >
          <div className="style-pay-title">
            付款方式选择：{SettlementTypeObj[SettlementType]}
          </div>
          <FormItem>
            {getFieldDecorator('payType', {
              initialValue: 1,
            })(
              <RadioGroup defaultValue="a" size="large" onChange={onChange}>
                {Object.keys(SettlementTypeObj).map(key => {
                  return (<RadioButton value={key} className={SettlementType === key ? styles.choice : ''}>
                    {SettlementTypeObj[key]}
                    {
                      SettlementType === key ?
                        <Icon type="check" className={styles.choiceIcon} /> : null
                    }
                  </RadioButton>)
                })}
              </RadioGroup>
            )}
          </FormItem>
        </Card>
      </Col>
      <Col>
        <Card className={styles['bottem-card']}>
          <Col className={styles['bottem-btns']} >
            <Button type="primary" size="large" className={styles['style-pay']} onClick={selected} >付款方式确认</Button>
          </Col>
        </Card>
      </Col>
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
