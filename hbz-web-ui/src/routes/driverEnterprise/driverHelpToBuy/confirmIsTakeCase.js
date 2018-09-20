import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Select, message, Radio } from 'antd'
import { Iconfont, AgreeModal } from 'components'
import styles from './index.less'

const FormItem = Form.Item
const { TextArea } = Input

const ConfirmIsTakeCase = ({
  dispatch,
  confirmIsTakeCase,
  form,
}) => {
  const { getFieldDecorator } = form
  const { id, buyOrderDetail, timeLimit, visible } = confirmIsTakeCase
  const timeLimitObj = []
  for (let key of Object.keys(timeLimit)) {
    timeLimitObj.push(<Option value={key}>{timeLimit[key]}</Option>)
  }
  const takeCase = () => {
    const value = form.getFieldValue('isAgree')
    if (value && value === 1) {
      dispatch({
        type: 'confirmIsTakeCase/takeCase',
        payload: {
          orderId: id,
        },
      })
    } else {
      message.error('请点击同意货帮主平台代购协议！')
    }
  }
  const hideModal = () => {
    dispatch({
      type: 'confirmIsTakeCase/changeStates',
      payload: {
        visible: false,
      },
    })
  }
  const showModal = () => {
    dispatch({
      type: 'confirmIsTakeCase/changeStates',
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
              <Row>
                <Col>
                  <FormItem label="商品名称">
                    {getFieldDecorator('commodityName', {
                      initialValue: buyOrderDetail && buyOrderDetail.commodityName,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col>
                  <FormItem label="购买数量">
                    {getFieldDecorator('commodityCount', {
                      initialValue: buyOrderDetail && buyOrderDetail.commodityCount,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col>
                  <FormItem label="预估价格">
                    {getFieldDecorator('commodityAmount', {
                      initialValue: buyOrderDetail && buyOrderDetail.commodityAmount,
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
                  initialValue: buyOrderDetail && buyOrderDetail.buyNeedInfo,
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
                  initialValue: buyOrderDetail && buyOrderDetail.timeLimit,
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
                  initialValue: buyOrderDetail && buyOrderDetail.startTime,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="配送地址">
                {getFieldDecorator('destInfo', {
                  initialValue: buyOrderDetail && buyOrderDetail.destInfo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="门牌号">
                {getFieldDecorator('destAddress', {
                  initialValue: buyOrderDetail && buyOrderDetail.destAddress,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="feiyong" className={styles.icon} /><span>费用构成</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="商品购买费用">
                {getFieldDecorator('commodityAmount', {
                  initialValue: buyOrderDetail && buyOrderDetail.commodityAmount,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="配送费用">
                {getFieldDecorator('amount', {
                  initialValue: buyOrderDetail && buyOrderDetail.amount,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row className={styles.price}>
            <Col>
              <p>费用合计：</p>
              <span className={styles['show-price']}>{`￥${buyOrderDetail.amount + buyOrderDetail.commodityAmount || '0.00'}`}</span>
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
              <Button type="primary" onClick={takeCase}>接单</Button>
            </Col>
          </Row>
        </Card>
      </Form>
      <AgreeModal {...modalProps} />
    </div>
  )
}

ConfirmIsTakeCase.propTypes = {
  form: PropTypes.isRequired,
  confirmIsTakeCase: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  visible: PropTypes.bool,
}
const ConfirmIsTakeCaseForm = Form.create()(ConfirmIsTakeCase)
export default connect(({ confirmIsTakeCase }) => ({ confirmIsTakeCase }))(ConfirmIsTakeCaseForm)
