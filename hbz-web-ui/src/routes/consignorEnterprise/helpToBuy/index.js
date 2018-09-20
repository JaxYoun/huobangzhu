import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { InputNumber, Card, Form, Row, Col, Input, Button, Select, DatePicker } from 'antd'
import { Iconfont, Address } from 'components'
import { regex } from 'utils'
import moment from 'moment'

import styles from './index.less'
import AddrsModal from './addrsModal'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

const HelpToBuy = ({
  dispatch,
  form,
  helpToBuy,
}) => {
  const {
    visible,
    confirmLoading,
    userAddrsList,
    timeLimit,
    loading,
    defaultStartTime,
  } = helpToBuy
  const { getFieldDecorator } = form
  const timeLimitObj = []
  for (let key of Object.keys(timeLimit)) {
    timeLimitObj.push(<Option value={key}>{timeLimit[key]}</Option>)
  }
  const creatOrder = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      formValue.startTime = formValue.startTime.format('YYYY-MM-DD HH:mm:ss')
      dispatch({
        type: 'helpToBuy/changeStates',
        payload: {
          loading: true,
        },
      })
      dispatch({
        type: 'helpToBuy/creatOrder',
        payload: formValue,
      })
    })
  }
  const addrLists = () => {
    dispatch({ type: 'helpToBuy/addrsModal' })
  }
  const numberProps = {
    max: 99999999,
    min: 0.00,
    precision: 2,
  }
  const numberIntProps = {
    max: 99999999,
    min: 0,
    precision: 0,
  }
  const addrProps = {
    visible,
    dispatch,
    confirmLoading,
    userAddrsList,
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
  const changeTakeLimit = (value) => {
    let time
    if (value === 'Immediately') {
      time = new Date().getTime()
    } else {
      time = ''
    }
    dispatch({
      type: 'helpToBuy/changeStates',
      payload: {
        defaultStartTime: time,
      },
    })
  }
  return (
    <div>
      <Form className={styles.form}>
        <Row>
          <Col>
            <Card title={<div><Iconfont type="gouwu" className={styles.icon} /><span>商品要求</span></div>}>
              <Row gutter={48}>
                <Col span={12}>
                  <FormItem label="商品名称">
                    {getFieldDecorator('commodityName', {
                      rules: [
                        {
                          required: true,
                          message: '请输入商品名称',
                        },
                      ],
                    })(
                      <Input placeholder="请输入商品名称" />
                    )}
                  </FormItem>
                  <FormItem label="商品数量">
                    {getFieldDecorator('commodityCount', {
                      rules: [
                        {
                          required: true,
                          message: '请输入商品数量',
                        },
                      ],
                    })(
                      <InputNumber {...numberIntProps} placeholder="请输入商品数量" />
                    )}
                  </FormItem>
                  <FormItem label="预估价格">
                    {getFieldDecorator('commodityAmount', {
                      rules: [
                        {
                          required: true,
                          message: '请输入预估价格',
                        },
                        { pattern: regex.number, message: '请输入正确的金额' },
                      ],
                    })(
                      <Input {...numberProps} placeholder="请输入预估总价" addonAfter={<span>元</span>} />
                    )}
                  </FormItem>
                </Col>
                <Col span={12} >
                  <FormItem label="补充说明">
                    {getFieldDecorator('buyNeedInfo', {
                      rules: [
                        {
                          required: true,
                          message: '请输入补充说明',
                        },
                      ],
                    })(
                      <TextArea autosize={{ minRows: 9, maxRows: 18 }} placeholder="补充说明" />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col>
            <Card
              title={<div><Iconfont type="huoche" className={styles.icon} /><span>配送要求</span></div>}
              // extra={<Button type="primary" shape="circle" size="small" icon="plus" onClick={addrLists} />}
            >
              <Row gutter={48}>
                <Col span={12}>
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
                <Col span={12}>
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
              <Row gutter={48}>
                <Col span={12}>
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
                  <Address {...shouHuoProps} placeholder="请输入收货地址" />
                </Col>
                <Col span={12} >
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
          </Col>
          <Col>
            <Card title="配送时间">
              <Row gutter={48}>
                <Col span={12}>
                  <FormItem label="时间要求">
                    {getFieldDecorator('timeLimit', {
                      rules: [
                        {
                          required: true,
                          message: '请输入送货时间要求',
                        },
                      ],
                      // initialValue: '1',
                    })(
                      <Select onChange={changeTakeLimit} placeholder="请选择时间要求">
                        {
                          timeLimitObj
                        }
                      </Select>
                    )}
                  </FormItem>
                </Col>
                <Col span={12}>
                  <FormItem label="起送时间">
                    {getFieldDecorator('startTime', {
                      rules: [
                        {
                          required: true,
                          message: '请输入送货时间',
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
          </Col>
          {/* <Col>
            <Card title="配送费用">
              <Row gutter={48}>
                <Col span={12}>
                  <FormItem label="配送费用">
                    {getFieldDecorator('amount', {
                      rules: [
                        {
                          required: true,
                          message: '请输入配送费用',
                        },
                      ],
                    })(
                      <InputNumber {...numberProps} placeholder="请输入配送费用" formatter={value => `￥${value}`} />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col> */}
        </Row>
      </Form>
      <Card>
        <Row className={styles['creat-button']}>
          <Col>
            <Button type="primary" onClick={creatOrder} loading={loading}>提 交 订 单</Button>
          </Col>
        </Row>
      </Card>
      {visible && <AddrsModal {...addrProps} />}
    </div>
  )
}

HelpToBuy.propTypes = {
  form: PropTypes.isRequired,
  helpToBuy: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const HelpToBuyForm = Form.create()(HelpToBuy)
export default connect(({ helpToBuy, app }) => ({ helpToBuy, app }))(HelpToBuyForm)
