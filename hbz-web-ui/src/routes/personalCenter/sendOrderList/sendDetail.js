import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Form, Row, Col, Input, Button } from 'antd'
import { Iconfont } from 'components'
import { browserHistory } from 'react-router'
import { config } from 'utils'
import styles from './index.less'

const FormItem = Form.Item

const SendDetail = ({
  dispatch,
  sendDetail,
  form,
}) => {
  const { sendDetailData, id, logisticsData } = sendDetail
  const { getFieldDecorator } = form
  const { orderTypes, orderState } = config
  const goBack = () => {
    browserHistory.goBack()
  }
  const confirmSign = () => {
    let type = sendDetailData.orderType.toLowerCase()
    dispatch({
      type: 'sendDetail/confirmSign',
      payload: {
        orderType: type,
        orderId: id,
      },
    })
  }
  const logistics = logisticsData.map(item => {
    return (
      <li>
        <span>{item.timeMillis}</span>
        <span>{orderState[item.orderTrans]}</span>
      </li>
    )
  })
  const doAgree = () => {
    let type = sendDetailData.orderType
    dispatch({
      type: 'sendDetail/doAgree',
      payload: {
        orderId: id,
        orderType: type,
      },
    })
  }
  return (
    <div>
      <Form>
        <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>订单详情</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="运单号">
                {getFieldDecorator('orderNo', {
                  initialValue: sendDetailData && sendDetailData.orderNo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="订单类型">
                {getFieldDecorator('orderType', {
                  initialValue: sendDetailData && orderTypes[sendDetailData.orderType],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="发单时间">
                {getFieldDecorator('createdDate', {
                  initialValue: (sendDetailData && sendDetailData.createdDate) || '',
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="运单状态">
                {getFieldDecorator('orderTrans', {
                  initialValue: sendDetailData && orderState[sendDetailData.orderTrans],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="货物名称">
                {getFieldDecorator('commodityName', {
                  initialValue: sendDetailData && sendDetailData.commodityName,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="总费用">
                {getFieldDecorator('amount', {
                  initialValue: sendDetailData && sendDetailData.amount,
                })(
                  <Input disabled addonAfter={<span>元</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            {
              sendDetailData.orderType === 'BUY' ? null : (
                <Col span={11}>
                  <FormItem label="取货地址">
                    {getFieldDecorator('originInfo', {
                      initialValue: sendDetailData && sendDetailData.originInfo,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              )
            }
            <Col span={11} offset={sendDetailData.orderType === 'BUY' ? 0 : 1}>
              <FormItem label="配送地址">
                {getFieldDecorator('destInfo', {
                  initialValue: sendDetailData && sendDetailData.destInfo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        {
          sendDetailData.orderTrans === 'NEW' ? null : (
            <div>
              <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>货主详情</span></div>}>
                <Row>
                  <Col span={11}>
                    <FormItem label="接运人">
                      {getFieldDecorator('nickName', {
                        initialValue: sendDetailData && sendDetailData.createUser.nickName,
                      })(
                        <Input disabled />
                      )}
                    </FormItem>
                  </Col>
                  <Col span={11} offset={1}>
                    <FormItem label="联系电话">
                      {getFieldDecorator('telephone', {
                        initialValue: sendDetailData && sendDetailData.createUser.telephone,
                      })(
                        <Input disabled />
                      )}
                    </FormItem>
                  </Col>
                </Row>
                <Row>
                  <Col span={11}>
                    <FormItem label="发单时间">
                      {getFieldDecorator('createdDate', {
                        initialValue: sendDetailData && sendDetailData.createdDate,
                      })(
                        <Input disabled />
                      )}
                    </FormItem>
                  </Col>
                </Row>
              </Card>
              <Card title="物流详情">
                <ul className={styles.log}>
                  {
                    logisticsData.length === 0 ? (
                      <li>
                        <span>暂无物流记录</span>
                      </li>
                    ) : (
                      logistics
                    )
                  }
                </ul>
              </Card>
            </div>
          )
        }
        <Card>
          <Row className={styles.foot}>
            <Col>
              <Button type="primary" onClick={goBack} className={styles.backBtn}>返回</Button>
              {
                sendDetailData.orderTrans === 'TRANSPORT' || sendDetailData.orderTrans === 'WAIT_TO_CONFIRM' ? (
                  <span>
                    <Button type="primary" onClick={confirmSign} className={styles.signBtn}>确认收件</Button>
                  </span>
                ) : null
              }
              {
                sendDetailData.orderTrans === 'LOCKED_ORDER' ? (
                  <Button type="primary" onClick={doAgree}>订单锁定待货主确认</Button>
                ) : null
              }
            </Col>
          </Row>
        </Card>
      </Form>
    </div>
  )
}
SendDetail.propTypes = {
  sendDetail: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
  form: PropTypes.object,
}
const SendDetailForm = Form.create()(SendDetail)
export default connect(({ sendDetail }) => ({ sendDetail }))(SendDetailForm)
