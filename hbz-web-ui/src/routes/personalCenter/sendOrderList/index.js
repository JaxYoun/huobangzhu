import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Form, Row, Col, Button, Select, DatePicker } from 'antd'
import { Iconfont, Ttable } from 'components'
import { browserHistory } from 'react-router'
import { config } from 'utils'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option

const SendOrderList = ({
  dispatch,
  sendOrderList,
  form,
}) => {
  const { getFieldDecorator } = form
  const { fields, tableTime, orderTypeData } = sendOrderList
  const orderTypeObj = []
  for (let key of Object.keys(orderTypeData)) {
    orderTypeObj.push(<Option value={key}>{orderTypeData[key]}</Option>)
  }
  const { orderTypes } = config
  const checkDetail = (data) => {
    if (data.settlementType === 'LEVY_ONLINE_PAYMENT') { // 判断是否是车辆征集
      if (data.orderTrans === 'NEW' || data.orderTrans === 'CONFIRMED') {
        browserHistory.push(`/consignorEnterprise/carCollect/carCollectTodo?orderId=${data.id}`)
      } else if (data.orderTrans === 'WAIT_TO_TAKE' || data.orderTrans === 'TRANSPORT'
          || data.orderTrans === 'WAIT_TO_CONFIRM' || data.orderTrans === 'WAIT_FOR_PAYMENT'
          || data.orderTrans === 'PAID' || data.orderTrans === 'LIQUIDATION_COMPLETED'
        ) {
        browserHistory.push(`/consignorEnterprise/carCollect/carCollectFinish?orderId=${data.id}`)
      } else if (data.orderTrans === 'ORDER_TO_BE_RECEIVED' || data.orderTrans === 'LOCKED_ORDER') {
        browserHistory.push(`/consignorEnterprise/carCollect/carCollectDoing?orderId=${data.id}`)
      } else {
        browserHistory.push(`/consignorEnterprise/carCollect/carCollectDetail?orderId=${data.id}`)
      }
    } else { // 如不是车辆征集的订单
      if (data.orderTrans === 'NEW') {
        switch (data.orderType) {
          case 'FSL':
            browserHistory.push(`/consignorEnterprise/specialLineTransport/confirmOrder?orderType=fslOrder&orderId=${data.id}`)
            break
          case 'LTL':
            browserHistory.push(`/consignorEnterprise/specialLineTransport/confirmOrder?orderType=LtlOrder&orderId=${data.id}`)
            break
          case 'BUY':
            browserHistory.push(`/consignorEnterprise/helpToBuy/helpToBuyComfirm?orderId=${data.id}`)
            break
          case 'SEND':
            browserHistory.push(`/consignorEnterprise/helpToSend/helpToSendComfirm?orderId=${data.id}`)
            break
          default:
            break
        }
      } else {
        browserHistory.push(`/personalCenter/sendOrderList/sendDetail?orderId=${data.id}`)
      }
    }
  }
  const columnsData = [
    {
      render: (text, record) => {
        return (
          <Card
            className={styles.card}
            title={
              <div className={styles.title}>
                <Iconfont type="fasong" className={styles.send} />
                <span>{record.commodityName}</span>
              </div>
            }
            onClick={checkDetail.bind(this, record)}
            extra={
              <span className={styles.time}>{record.startTime}</span>
            }
          >
            <div className={styles.content}>
              <span className={styles.name}>运单编号:</span>
              <span className={styles.value}>{record.orderNo}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>订单类型:</span>
              <span className={styles.value}>{orderTypes[record.orderType]}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送地址:</span>
              <span className={styles.value}>{record.destAddress}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>总费用:</span>
              <span className={styles.value}>{`${record.amount}元`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>发单时间:</span>
              <span className={styles.value}>{record.createdDate}</span>
            </div>
          </Card>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/order/queryPage',
    fields: fields || {},
    // scroll: { x: 3300 },
    tableTime,
    showHeader: false,
    bordered: false,
  }
  const querySendList = () => {
    let obj = {}
    const value = form.getFieldsValue()
    for (let key in value) {
      if (value[key]) {
        if (key === 'orderTakeStart') {
          obj[key] = value[key].format('YYYY-MM-DD HH:mm:ss')
        } else {
          obj[key] = value[key]
        }
      }
    }
    dispatch({
      type: 'sendOrderList/changeStates',
      payload: {
        fields: obj,
        tableTime: new Date().getTime(),
      },
    })
  }
  const rest = () => {
    form.resetFields()
  }
  return (
    <div>
      <Card>
        <Row>
          <Col span={7}>
            <FormItem label="订单类型">
              {getFieldDecorator('orderType', {
              })(
                <Select placeholder="请选择订单类型" style={{ width: '100%' }}>
                  {
                    orderTypeObj
                  }
                </Select>
              )}
            </FormItem>
          </Col>
          <Col span={7} offset={1}>
            <FormItem label="发运时间">
              {getFieldDecorator('orderTakeStart', {
              })(
                <DatePicker style={{ width: '100%' }} />
              )}
            </FormItem>
          </Col>
          <Col span={7} offset={2}>
            <Button type="primary" style={{ marginTop: '34px' }} onClick={querySendList}>查询</Button>
            <Button type="primary" style={{ marginTop: '34px' }} onClick={rest}>重置</Button>
          </Col>
        </Row>
      </Card>
      <Card>
        <Ttable {...tableProps} />
      </Card>
    </div>
  )
}

SendOrderList.propTypes = {
  sendOrderList: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
  form: PropTypes.object,
}

const SendOrderListForm = Form.create()(SendOrderList)
export default connect(({ sendOrderList }) => ({ sendOrderList }))(SendOrderListForm)
