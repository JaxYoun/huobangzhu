import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, Select, DatePicker } from 'antd'
import { Iconfont, Ttable } from 'components'
import { browserHistory } from 'react-router'
import { config } from 'utils'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option

const ReceiveOrderList = ({
  dispatch,
  receiveOrderList,
  form,
}) => {
  const { getFieldDecorator } = form
  const { fields, tableTime, orderTypeData } = receiveOrderList
  const orderTypeObj = []
  for (let key of Object.keys(orderTypeData)) {
    orderTypeObj.push(<Option value={key}>{orderTypeData[key]}</Option>)
  }
  const { orderTypes, orderState } = config
  const checkDetail = (data) => {
    browserHistory.push(`/personalCenter/receiveOrderList/receiveDetail?orderId=${data.id}`)
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
            // extra={
            //   <span className={styles.time}>{record.startTime}</span>
            // }
          >
            <div className={styles.content}>
              <span className={styles.name}>运单编号:</span>
              <span className={styles.value}>{record.orderNo}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>运单状态:</span>
              <span className={styles.value}>{orderState[record.orderTrans]}</span>
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
              <span className={styles.name}>发单时间:</span>
              <span className={styles.value}>{record.createdDate}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>接运时间:</span>
              <span className={styles.value}>{record.orderTakeStart}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>总费用:</span>
              <span className={styles.value}>{`${record.amount}元`}</span>
            </div>
          </Card>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/order/task/queryPage',
    fields: fields || {},
    // scroll: { x: 3300 },
    tableTime,
    showHeader: false,
    bordered: false,
  }
  const queryReceiveList = () => {
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
      type: 'receiveOrderList/changeStates',
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
            <FormItem label="接运时间">
              {getFieldDecorator('orderTakeStart', {
              })(
                <DatePicker style={{ width: '100%' }} />
              )}
            </FormItem>
          </Col>
          <Col span={7} offset={2}>
            <Button type="primary" style={{ marginTop: '34px' }} onClick={queryReceiveList}>查询</Button>
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

ReceiveOrderList.propTypes = {
  receiveOrderList: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
  form: PropTypes.object,
}

const ReceiveOrderListForm = Form.create()(ReceiveOrderList)
export default connect(({ receiveOrderList }) => ({ receiveOrderList }))(ReceiveOrderListForm)
