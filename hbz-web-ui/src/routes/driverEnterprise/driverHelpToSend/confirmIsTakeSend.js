import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, Select, message, Radio } from 'antd'
import { Iconfont, AgreeModal } from 'components'
import styles from './index.less'

const FormItem = Form.Item
const { TextArea } = Input

const ConfirmIsTakeSend = ({
  dispatch,
  form,
  confirmIsTakeSend,
}) => {
  const { getFieldDecorator } = form
  const { id, sendOrderDetail, timeLimit, visible } = confirmIsTakeSend
  const timeLimitObj = []
  for (let key of Object.keys(timeLimit)) {
    timeLimitObj.push(<Option value={key}>{timeLimit[key]}</Option>)
  }
  const takeSend = () => {
    const value = form.getFieldValue('isAgree')
    if (value && value === 1) {
      dispatch({
        type: 'confirmIsTakeSend/takeSend',
        payload: { orderId: id },
      })
    } else {
      message.error('请点击同意货帮主平台代送协议！')
    }
  }
  const hideModal = () => {
    dispatch({
      type: 'confirmIsTakeSend/changeStates',
      payload: {
        visible: false,
      },
    })
  }
  const showModal = () => {
    dispatch({
      type: 'confirmIsTakeSend/changeStates',
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
        <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>商品要求</span></div>}>
          <Row>
            <Col span={11}>
              {/* <Row>
                <Col>
                  <FormItem label="商品名称">
                    {getFieldDecorator('commodityName', {
                      initialValue: sendOrderDetail && sendOrderDetail.commodityName,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row> */}
              <Row>
                <Col>
                  <FormItem label="货物重量">
                    {getFieldDecorator('commodityWeight', {
                      initialValue: sendOrderDetail && sendOrderDetail.commodityWeight,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col>
                  <FormItem label="货物体积">
                    {getFieldDecorator('commodityVolume', {
                      initialValue: sendOrderDetail && sendOrderDetail.commodityVolume,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col>
                  <FormItem label="预估价格">
                    {getFieldDecorator('amount', {
                      initialValue: sendOrderDetail && sendOrderDetail.amount,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="补充说明">
                {getFieldDecorator('buyNeedInfo', {
                  initialValue: sendOrderDetail && sendOrderDetail.commodityDesc,
                })(
                  <TextArea autosize={{ minRows: 6, maxRows: 18 }} disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="huoche21" className={styles.icon} /><span>配送要求</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="时间要求">
                {getFieldDecorator('timeLimit', {
                  initialValue: sendOrderDetail && sendOrderDetail.timeLimit,
                })(
                  <Select disabled>
                    {timeLimitObj}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="订单时间">
                {getFieldDecorator('startTime', {
                  initialValue: sendOrderDetail && sendOrderDetail.startTime,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="取货电话">
                {getFieldDecorator('originLinkTelephone', {
                  initialValue: sendOrderDetail && sendOrderDetail.originLinkTelephone,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="取货地址">
                {getFieldDecorator('originInfo', {
                  initialValue: sendOrderDetail && sendOrderDetail.originInfo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="收货电话">
                {getFieldDecorator('linkTelephone', {
                  initialValue: sendOrderDetail && sendOrderDetail.linkTelephone,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="收货地址">
                {getFieldDecorator('destInfo', {
                  initialValue: sendOrderDetail && sendOrderDetail.destInfo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="feiyong" className={styles.icon} /><span>费用构成</span></div>}>
          <Row className={styles.price}>
            <Col>
              <p>费用合计：</p>
              <span className={styles['show-price']}>{`￥${sendOrderDetail.amount || '0.00'}`}</span>
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
        <Card>
          <Row className={styles['creat-button']}>
            <Col>
              <Button type="primary" onClick={takeSend}>接 单</Button>
            </Col>
          </Row>
        </Card>
      </Form>
      <AgreeModal {...modalProps} />
    </div>
  )
}

ConfirmIsTakeSend.propTypes = {
  form: PropTypes.isRequired,
  confirmIsTakeSend: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const ConfirmIsTakeSendForm = Form.create()(ConfirmIsTakeSend)
export default connect(({ confirmIsTakeSend }) => ({ confirmIsTakeSend }))(ConfirmIsTakeSendForm)
