import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Select, DatePicker, InputNumber } from 'antd'
import { Iconfont, Address } from 'components'
import { regex } from 'utils'
import moment from 'moment'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

const HelpToSend = ({
  dispatch,
  form,
  app,
  helpToSend,
}) => {
  const numberProps = {
    max: 99999999,
    min: 0.00,
    precision: 2,
  }
  const { tableData } = app
  const { timeLimit } = tableData
  const { defaultStartTime, defaultTakeTime, timeLimitData, loading } = helpToSend
  const { getFieldDecorator } = form
  const timeLimitObj = []
  for (let key of Object.keys(timeLimitData)) {
    timeLimitObj.push(<Option value={key}>{timeLimitData[key]}</Option>)
  }
  const creatOrder = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.orderTakeTime = formValue.orderTakeTime.format('YYYY-MM-DD HH:mm:ss')
      formValue.startTime = formValue.startTime.format('YYYY-MM-DD HH:mm:ss')
      formValue.amount = Number(formValue.amount)
      dispatch({
        type: 'helpToSend/changeStates',
        payload: {
          loading: true,
        },
      })
      dispatch({
        type: 'helpToSend/creatOrder',
        payload: formValue,
      })
    })
  }
  const quHuoProps = {
    address: {
      key: 'originInfo',
      value: '',
    },
    label: '取货地址',
    lng: {
      key: 'originX',
      value: '',
    },
    lat: {
      key: 'originY',
      value: '',
    },
    form,
  }
  const shouHuoProps = {
    address: {
      key: 'destInfo',
      value: '',
    },
    label: '配送地址',
    lng: {
      key: 'destX',
      value: '',
    },
    lat: {
      key: 'destY',
      value: '',
    },
    form,
  }
  const changeStartLimit = (value) => {
    let time
    if (value === '1') {
      time = new Date().getTime()
      console.log('time', time)
    } else {
      time = ''
    }
    dispatch({
      type: 'helpToSend/changeStates',
      payload: {
        defaultStartTime: time,
      },
    })
  }
  const changeTakeLimit = (value) => {
    let time
    if (value === 'Immediately') {
      time = new Date().getTime()
    } else {
      time = ''
    }
    dispatch({
      type: 'helpToSend/changeStates',
      payload: {
        defaultTakeTime: time,
      },
    })
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
                      rules: [
                        {
                          required: true,
                          message: '请输入货物预估重量',
                        },
                      ],
                    })(
                      <InputNumber
                        {...numberProps}
                        placeholder="请输入预估重量"
                      />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col>
                  <FormItem label="预估体积">
                    {getFieldDecorator('commodityVolume', {
                      rules: [
                        {
                          required: true,
                          message: '请输入货物预估体积',
                        },
                      ],
                    })(
                      <InputNumber {...numberProps} placeholder="请输入预估体积" />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="货物描述">
                {getFieldDecorator('commodityDesc', {
                  rules: [
                    {
                      required: true,
                      message: '请输入货物描述',
                    },
                  ],
                })(
                  <TextArea autosize={{ minRows: 6, maxRows: 18 }} placeholder="货物描述" />
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
                  rules: [
                    {
                      required: true,
                      message: '请输入取货联系人',
                    },
                  ],
                })(
                  <Input placeholder="请输入联系人" />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="联系电话">
                {getFieldDecorator('originLinkTelephone', {
                  rules: [
                    {
                      required: true,
                      message: '请输入取货联系电话',
                    },
                    { pattern: regex.phone, message: '请输入正确的手机号码!' },
                  ],
                })(
                  <Input placeholder="请输入联系电话" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              {/* <FormItem label="联系地址">
                {getFieldDecorator('originInfo', {
                  rules: [
                    {
                      required: true,
                      message: '请输入取货地址',
                    },
                  ],
                })(
                  // <Input placeholder="请输入联系地址" />
                )}
              </FormItem> */}
              <Address placeholder="请输入取货地址" {...quHuoProps} />
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="门牌号">
                {getFieldDecorator('originAddress', {
                  rules: [
                    {
                      required: true,
                      message: '请输入取货门牌号',
                    },
                  ],
                })(
                  <Input placeholder="请输入门牌号" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><span>取货时间</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="时间要求">
                {getFieldDecorator('takeLimit', {
                  rules: [
                    {
                      required: true,
                      message: '请选择取货时间要求',
                    },
                  ],
                  initialValue: '1',
                })(
                  <Select onChange={changeStartLimit}>
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
                {getFieldDecorator('startTime', {
                  rules: [
                    {
                      required: true,
                      message: '请输入货物时间',
                    },
                  ],
                  initialValue: defaultStartTime ? moment(defaultStartTime) : '',
                })(
                  <DatePicker
                    format="YYYY-MM-DD HH:mm"
                    placeholder="请输入起送时间"
                    style={{ width: '100%' }}
                    disabled={defaultStartTime ? 'true' : false}
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
                  rules: [
                    {
                      required: true,
                      message: '请输入收货联系人',
                    },
                  ],
                })(
                  <Input placeholder="请输入联系人" />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="联系电话">
                {getFieldDecorator('linkTelephone', {
                  rules: [
                    {
                      required: true,
                      message: '请输入收货人联系电话',
                    },
                    { pattern: regex.phone, message: '请输入正确的手机号码!' },
                  ],
                })(
                  <Input placeholder="请输入联系电话" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              {/* <FormItem label="联系地址">
                {getFieldDecorator('destInfo', {
                  rules: [
                    {
                      required: true,
                      message: '请输入收货地址',
                    },
                  ],
                })(
                  <Input placeholder="请输入联系地址" />
                )}
              </FormItem> */}
              <Address placeholder="请输入配送地址" {...shouHuoProps} />
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="门牌号">
                {getFieldDecorator('destAddress', {
                  rules: [
                    {
                      required: true,
                      message: '请输入收货门牌号',
                    },
                  ],
                })(
                  <Input placeholder="请输入门牌号" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><span>配送时间</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="时间要求">
                {getFieldDecorator('timeLimit', {
                  rules: [
                    {
                      required: true,
                      message: '请输入送货时间要求',
                    },
                  ],
                })(
                  <Select onChange={changeTakeLimit} placeholder="请选择时间要求">
                    {
                      timeLimitObj
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="配送时间">
                {getFieldDecorator('orderTakeTime', {
                  rules: [
                    {
                      required: true,
                      message: '请输入送货时间',
                    },
                  ],
                  initialValue: defaultTakeTime ? moment(defaultTakeTime) : '',
                })(
                  <DatePicker
                    format="YYYY-MM-DD HH:mm"
                    placeholder="请输入起送时间"
                    style={{ width: '100%' }}
                    disabled={defaultTakeTime ? 'true' : false}
                  />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="配送预估费用">
          <Row>
            <Col span={11}>
              <FormItem label="配送预估费用">
                {getFieldDecorator('amount', {
                  rules: [
                    {
                      required: true,
                      message: '请输入预估金额',
                    },
                    { pattern: regex.number, message: '请输入正确的金额' },
                  ],
                })(
                  <Input placeholder="请输入配送预估费用" addonAfter={<span>元</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Form>
      <Card>
        <Row className={styles['creat-button']}>
          <Col>
            <Button type="primary" onClick={creatOrder} loading={loading}>提 交 订 单</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

HelpToSend.propTypes = {
  form: PropTypes.isRequired,
  helpToSend: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const HelpToSendForm = Form.create()(HelpToSend)
export default connect(({ helpToSend, app }) => ({ helpToSend, app }))(HelpToSendForm)
