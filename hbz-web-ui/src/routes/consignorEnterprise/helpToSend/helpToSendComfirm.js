import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { message, Card, Form, Row, Col, Input, Button, Select, DatePicker, Radio } from 'antd'
import { Iconfont, AgreeModal } from 'components'
import moment from 'moment'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

const HelpToSendComfirm = ({
  dispatch,
  form,
  app,
  helpToSendComfirm,
}) => {
  const { tableData } = app
  const { timeLimit } = tableData
  const { orderDetail, id, referToPrice, timeLimitData, visible } = helpToSendComfirm
  const { getFieldDecorator } = form
  const timeLimitObj = []
  for (let key of Object.keys(timeLimitData)) {
    timeLimitObj.push(<Option value={key}>{timeLimitData[key]}</Option>)
  }
  const goPay = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      if (formValue.isAgree && formValue.isAgree === 1) {
        dispatch({
          type: 'helpToSendComfirm/confirmOrder',
          payload: {
            orderId: id,
            amount: formValue.payAmount,
          },
        })
      } else {
        message.error('请点击同意货帮主平台代购协议！')
      }
    })
  }
  const hideModal = () => {
    dispatch({
      type: 'helpToSendComfirm/changeStates',
      payload: {
        visible: false,
      },
    })
  }
  const showModal = () => {
    dispatch({
      type: 'helpToSendComfirm/changeStates',
      payload: {
        visible: true,
      },
    })
  }
  const modalProps = {
    title: '货帮主协议',
    visible,
    hideModal,
    content: '货帮主平台代购协议......',
  }
  return (
    <div>
      <Form>
        <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>货物详情</span></div>}>
          <Row>
            <Col span={11}>
              <Row>
                <Col>
                  <FormItem label="预估重量">
                    {getFieldDecorator('commodityWeight', {
                      initialValue: orderDetail && orderDetail.commodityWeight,
                    })(
                      <Input placeholder="请输入预估重量" disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col>
                  <FormItem label="预估体积">
                    {getFieldDecorator('commodityVolume', {
                      initialValue: orderDetail && orderDetail.commodityVolume,
                    })(
                      <Input placeholder="请输入预估体积" disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="货物描述">
                {getFieldDecorator('commodityDesc', {
                  initialValue: orderDetail && orderDetail.commodityDesc,
                })(
                  <TextArea autosize={{ minRows: 6, maxRows: 18 }} placeholder="货物描述" disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="huoche1" className={styles.icon} /><span>取货地址</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="联系人">
                {getFieldDecorator('originLinker', {
                  initialValue: orderDetail && orderDetail.originLinker,
                })(
                  <Input placeholder="请输入联系人" disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="联系电话">
                {getFieldDecorator('originLinkTelephone', {
                  initialValue: orderDetail && orderDetail.originLinkTelephone,
                })(
                  <Input placeholder="请输入联系电话" disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="联系地址">
                {getFieldDecorator('originInfo', {
                  initialValue: orderDetail && orderDetail.originInfo,
                })(
                  <Input placeholder="请输入联系地址" disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="门牌号">
                {getFieldDecorator('originAddress', {
                  initialValue: orderDetail && orderDetail.originAddress,
                })(
                  <Input placeholder="请输入门牌号" disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="取货时间">
          <Row>
            <Col span={11}>
              <FormItem label="时间要求">
                {getFieldDecorator('takeLimit', {
                  initialValue: orderDetail && orderDetail.takeLimit.toString(),
                })(
                  <Select disabled>
                    {
                      timeLimit.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="起送时间">
                {getFieldDecorator('orderTakeTime', {
                  initialValue: orderDetail && orderDetail.orderTakeTime ? moment(orderDetail.orderTakeTime) : '',
                })(
                  <DatePicker
                    format="YYYY-MM-DD HH:mm"
                    placeholder="请输入起送时间"
                    style={{ width: '100%' }}
                    disabled
                  />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="huoche" className={styles.icon} /><span>配送地址</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="联系人">
                {getFieldDecorator('linker', {
                  initialValue: orderDetail && orderDetail.linker,
                })(
                  <Input placeholder="请输入联系人" disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="联系电话">
                {getFieldDecorator('linkTelephone', {
                  initialValue: orderDetail && orderDetail.linkTelephone,
                })(
                  <Input placeholder="请输入联系电话" disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="联系地址">
                {getFieldDecorator('destInfo', {
                  initialValue: orderDetail && orderDetail.destInfo,
                })(
                  <Input placeholder="请输入联系地址" disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="门牌号">
                {getFieldDecorator('destAddress', {
                  initialValue: orderDetail && orderDetail.destAddress,
                })(
                  <Input placeholder="请输入门牌号" disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="配送时间">
          <Row>
            <Col span={11}>
              <FormItem label="时间要求">
                {getFieldDecorator('timeLimit', {
                  initialValue: orderDetail && orderDetail.timeLimit,
                })(
                  <Select disabled>
                    {
                      timeLimitObj
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="起送时间">
                {getFieldDecorator('startTime', {
                  initialValue: orderDetail && moment(orderDetail.startTime),
                })(
                  <DatePicker
                    format="YYYY-MM-DD HH:mm"
                    placeholder="请输入起送时间"
                    style={{ width: '100%' }}
                    disabled
                  />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="费用构成">
          <Row>
            <Col span={11}>
              <FormItem label="送货预估费用">
                {getFieldDecorator('amount', {
                  initialValue: orderDetail && orderDetail.amount,
                })(
                  <Input placeholder="请输入送货预估费用" disabled addonAfter={<span>元</span>} />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="平台参考费用">
                {getFieldDecorator('referToPrice', {
                  initialValue: referToPrice,
                })(
                  <Input placeholder="平台参考费用" disabled addonAfter={<span>元</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="支付费用">
                {getFieldDecorator('payAmount', {
                  rules: [
                    {
                      required: true,
                      message: '请输入支付费用',
                    },
                  ],
                })(
                  <Input placeholder="请输入支付费用" addonAfter={<span>元</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <p className={styles.warning}>* 配送预估费用如低于平台计算的配送费用将影响您的接单速度甚至造成订单作废</p>
            </Col>
          </Row>
          <Row style={{ marginTop: '20px' }}>
            <Col>
              <FormItem>
                {getFieldDecorator('isAgree', {
                })(
                  <Radio.Group onChange={showModal}>
                    <Radio value={1}>点击同意货帮主平台代购协议</Radio>
                  </Radio.Group>
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Form>
      <Card>
        <Row className={styles['creat-button']}>
          <Col>
            <Button type="primary" onClick={goPay}>去 支 付</Button>
          </Col>
        </Row>
      </Card>
      <AgreeModal {...modalProps} />
    </div>
  )
}

HelpToSendComfirm.propTypes = {
  form: PropTypes.isRequired,
  helpToSendComfirm: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  visible: PropTypes.bool,
}

const HelpToSendComfirmForm = Form.create()(HelpToSendComfirm)
export default connect(({ helpToSendComfirm, app }) => ({ helpToSendComfirm, app }))(HelpToSendComfirmForm)
