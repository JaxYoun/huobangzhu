import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, Select, DatePicker } from 'antd'
import { Iconfont, Ttable } from 'components'
import { browserHistory } from 'react-router'
import { config } from 'utils'
import styles from './index.less'

const FormItem = Form.Item

const ReceiveDetail = ({
  dispatch,
  receiveDetail,
  form,
}) => {
  const { getFieldDecorator } = form
  const { orderTypes, orderState } = config
  const { receiveDetailData, id, logisticsData } = receiveDetail
  const goBack = () => {
    browserHistory.goBack()
  }
  const confirmSign = (text) => {
    console.log(text)
    dispatch({
      type: 'receiveDetail/confirmSign',
      payload: {
        orderId: id,
        apiType: receiveDetailData.orderType.toLowerCase(),
        do: text,
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
  const ShowBtn = () => {
    let btn = []
    // 月结订单逻辑
    if (receiveDetailData.settlementType === 'MONTHLY_SETTLEMENT') {
      if (receiveDetailData.offlineProcess && receiveDetailData.offlineProcess === '1') { // 用户拒绝签收
        btn.push(<span>客户拒绝签收，工作人员处理中</span>)
      } else {
        if (receiveDetailData.orderTrans === 'LOCKED_ORDER_DRIVER') {
          btn.push(<span>
            <Button type="primary" onClick={confirmSign.bind(this, 'drivingAgree')}>确认接单</Button>
            <Button type="primary" onClick={confirmSign.bind(this, 'refuseAgree')}>拒绝接单</Button>
          </span>)
        } else if (receiveDetailData.orderTrans === 'WAIT_TO_TAKE') {
          btn.push(
            <Button type="primary" onClick={confirmSign.bind(this, 'take')}>收货确认</Button>
          )
        } else if (receiveDetailData.orderTrans === 'TRANSPORT') {
          btn.push(
            <span>
              <Button type="primary" onClick={confirmSign.bind(this, 'ok')}>签收确认</Button>
              <Button type="primary" onClick={confirmSign.bind(this, 'refuse')}>拒绝签收</Button>
            </span>
          )
        }
      }
    } else {
      // 帮我买的逻辑需要特殊处理
      if (receiveDetailData.orderType === 'BUY') {
        if (receiveDetailData.offlineProcess && receiveDetailData.offlineProcess === '1') {
          btn.push(<span>客户拒绝签收，工作人员处理中</span>)
        } else {
          if (receiveDetailData.orderTrans === 'WAIT_TO_TAKE') {
            btn.push(
              <Button type="primary" onClick={confirmSign.bind(this, 'take')}>买货确认</Button>
            )
          } else if (receiveDetailData.orderTrans === 'TRANSPORT') {
            btn.push(
              <span>
                <Button type="primary" onClick={confirmSign.bind(this, 'ok')}>签收确认</Button>
                <Button type="primary" onClick={confirmSign.bind(this, 'refuse')}>拒绝签收</Button>
              </span>
            )
          }
        }
      } else {
        if (receiveDetailData.offlineProcess && receiveDetailData.offlineProcess === '1') {
          btn.push(<span>客户拒绝签收，工作人员处理中</span>)
        } else {
          if (receiveDetailData.orderTrans === 'LOCKED_ORDER_DRIVER' || receiveDetailData.orderTrans === 'WAIT_TO_TAKE') {
            btn.push(
              <Button type="primary" onClick={confirmSign.bind(this, 'take')}>收货确认</Button>
            )
          } else if (receiveDetailData.orderTrans === 'TRANSPORT') {
            btn.push(
              <span>
                <Button type="primary" onClick={confirmSign.bind(this, 'ok')}>签收确认</Button>
                <Button type="primary" onClick={confirmSign.bind(this, 'refuse')}>拒绝签收</Button>
              </span>
            )
          }
        }
      }
    }
    return (
      <span>{btn}</span>
    )
  }
  return (
    <div>
      <Form>
        <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>订单详情</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="运单号">
                {getFieldDecorator('orderNo', {
                  initialValue: receiveDetailData && receiveDetailData.orderNo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="订单类型">
                {getFieldDecorator('orderType', {
                  initialValue: receiveDetailData && orderTypes[receiveDetailData.orderType],
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
                  initialValue: (receiveDetailData && receiveDetailData.createdDate) || '',
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="运单状态">
                {getFieldDecorator('orderTrans', {
                  initialValue: receiveDetailData && orderState[receiveDetailData.orderTrans],
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            {
              receiveDetailData.orderType === 'EX' ? (
                <Col span={11}>
                  <FormItem label="货物描述">
                    {getFieldDecorator('commodityDesc', {
                      initialValue: receiveDetailData && receiveDetailData.commodityDesc,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              ) : (
                <Col span={11}>
                  <FormItem label="货物名称">
                    {getFieldDecorator('commodityName', {
                      initialValue: receiveDetailData && receiveDetailData.commodityName,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              )
            }
            <Col span={11} offset={1}>
              <FormItem label="总费用">
                {getFieldDecorator('amount', {
                  initialValue: receiveDetailData && receiveDetailData.amount,
                })(
                  <Input disabled addonAfter={<span>元</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            {
              receiveDetailData.orderType === 'BUY' ? null : (
                <Col span={11}>
                  <FormItem label="取货地址">
                    {getFieldDecorator('originInfo', {
                      initialValue: receiveDetailData && receiveDetailData.originInfo,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              )
            }
            <Col span={11} offset={receiveDetailData.orderType === 'BUY' ? 0 : 1}>
              <FormItem label="配送地址">
                {getFieldDecorator('destInfo', {
                  initialValue: receiveDetailData && receiveDetailData.destInfo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>货主详情</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="货主">
                {getFieldDecorator('nickName', {
                  initialValue: receiveDetailData && receiveDetailData.createUser.nickName,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="联系电话">
                {getFieldDecorator('telephone', {
                  initialValue: receiveDetailData && receiveDetailData.createUser.telephone,
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
                  initialValue: receiveDetailData && receiveDetailData.createdDate,
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
        <Card>
          <Row className={styles.foot}>
            <Col>
              <Button type="primary" onClick={goBack} className={styles.backBtn}>返回</Button>
              <ShowBtn />
            </Col>
          </Row>
        </Card>
      </Form>
    </div>
  )
}
ReceiveDetail.propTypes = {
  receiveDetail: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
  form: PropTypes.object,
}
const ReceiveDetailForm = Form.create()(ReceiveDetail)
export default connect(({ receiveDetail }) => ({ receiveDetail }))(ReceiveDetailForm)
