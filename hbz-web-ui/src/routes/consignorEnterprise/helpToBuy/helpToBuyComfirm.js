import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Select, message, Radio } from 'antd'
import { Iconfont, AgreeModal } from 'components'
import moment from 'moment'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

const HelpToBuyComfirm = ({
  dispatch,
  form,
  helpToBuy,
  helpToBuyComfirm,
}) => {
  const { timeLimit } = helpToBuy
  const { orderDetail, id, price, visible } = helpToBuyComfirm
  const { getFieldDecorator } = form
  const timeLimitObj = []
  for (let key of Object.keys(timeLimit)) {
    timeLimitObj.push(<Option value={key}>{timeLimit[key]}</Option>)
  }
  const goPay = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      if (formValue.isAgree && formValue.isAgree === 1) {
        dispatch({
          type: 'helpToBuyComfirm/comfirmOrder',
          payload: {
            orderId: id,
            remuneration: formValue.remuneration,
          },
        })
      } else {
        message.error('请点击同意货帮主平台代购协议！')
      }
    })
  }
  const sendPrice = (e) => {
    console.log(e.target.value)
    dispatch({
      type: 'helpToBuyComfirm/changeStates',
      payload: {
        price: Number(e.target.value),
      },
    })
  }
  const hideModal = () => {
    dispatch({
      type: 'helpToBuyComfirm/changeStates',
      payload: {
        visible: false,
      },
    })
  }
  const showModal = () => {
    dispatch({
      type: 'helpToBuyComfirm/changeStates',
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
      <Form className={styles.form}>
        <Row>
          <Col>
            <Card title={<div><Iconfont type="gouwu" className={styles.icon} /><span>商品要求</span></div>}>
              <Row gutter={48}>
                <Col span={12}>
                  <FormItem label="商品名称">
                    {getFieldDecorator('commodityName', {
                      initialValue: orderDetail.commodityName,
                    })(
                      <Input disabled placeholder="请输入商品名称" />
                    )}
                  </FormItem>
                  <FormItem label="商品数量">
                    {getFieldDecorator('commodityCount', {
                      initialValue: orderDetail.commodityCount,
                    })(
                      <Input disabled placeholder="请输入商品数量" />
                    )}
                  </FormItem>
                  <FormItem label="预估价格">
                    {getFieldDecorator('commodityAmount', {
                      initialValue: orderDetail.commodityAmount,
                    })(
                      <Input disabled placeholder="请输入预估总价" />
                    )}
                  </FormItem>
                </Col>
                <Col span={12} >
                  <FormItem label="补充说明">
                    {getFieldDecorator('buyNeedInfo', {
                      initialValue: orderDetail.buyNeedInfo,
                    })(
                      <TextArea disabled autosize={{ minRows: 11, maxRows: 18 }} placeholder="补充说明" />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col>
            <Card
              title={<div><Iconfont type="huoche" className={styles.icon} /><span>配送要求</span></div>}
            >
              <Row gutter={48}>
                <Col span={12}>
                  <FormItem label="联系人">
                    {getFieldDecorator('linker', {
                      initialValue: orderDetail.linker,
                    })(
                      <Input disabled placeholder="请输入联系人" />
                    )}
                  </FormItem>
                </Col>
                <Col span={12}>
                  <FormItem label="联系电话">
                    {getFieldDecorator('linkTelephone', {
                      initialValue: orderDetail.linkTelephone,
                    })(
                      <Input disabled placeholder="请输入联系电话" />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row gutter={48}>
                <Col span={12}>
                  <FormItem label="联系地址">
                    {getFieldDecorator('destInfo', {
                      initialValue: orderDetail.destInfo,
                    })(
                      <Input disabled placeholder="请输入联系地址" />
                    )}
                  </FormItem>
                </Col>
                <Col span={12} >
                  <FormItem label="门牌号">
                    {getFieldDecorator('destAddress', {
                      initialValue: orderDetail.destAddress,
                    })(
                      <Input disabled placeholder="请输入门牌号" />
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
                      initialValue: orderDetail.timeLimit,
                    })(
                      <Select disabled >
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
                      initialValue: orderDetail.startTime,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col>
            <Card title="费用构成">
              <Row gutter={48}>
                <Col span={12}>
                  <FormItem label="商品费用">
                    {getFieldDecorator('commodityAmount', {
                      initialValue: orderDetail && orderDetail.commodityAmount,
                    })(
                      <Input disabled placeholder="请输入商品费用" />
                    )}
                  </FormItem>
                </Col>
                <Col span={12}>
                  <FormItem label="配送费用">
                    {getFieldDecorator('remuneration', {
                      rules: [
                        {
                          required: true,
                          message: '请输入配送费用',
                        },
                      ],
                    })(
                      <Input placeholder="请输入商品费用" onChange={sendPrice} />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row className={styles.price}>
                <Col>
                  <p>费用合计：</p>
                  <span className={styles['show-price']}>{`￥${(orderDetail.commodityAmount + price) || '0.00'}`}</span>
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
          </Col>
        </Row>
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

HelpToBuyComfirm.propTypes = {
  form: PropTypes.isRequired,
  helpToBuyComfirm: PropTypes.isRequired,
  helpToBuy: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  visible: PropTypes.bool,
}

const HelpToBuyComfirmForm = Form.create()(HelpToBuyComfirm)
export default connect(({ helpToBuyComfirm, helpToBuy }) => ({ helpToBuyComfirm, helpToBuy }))(HelpToBuyComfirmForm)
