import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Button } from 'antd'
import { Iconfont, AreaSelect, Ttable } from 'components'
import { config } from 'utils'
import styles from './index.less'

const CarCollect = ({
  dispatch,
  carCollect,
  form,
}) => {
  console.log(carCollect, 'carCollect')
  const { fields, tableTime } = carCollect
  const { orderState, collectStaut } = config
  // const { getFieldDecorator } = form
  const OrderStautClazz = {
    NEW: 'not-start',
    CONFIRMED: 'not-start',
    ORDER_TO_BE_RECEIVED: 'order-todo',
    LOCKED_ORDER: 'order-todo',
    WAIT_TO_TAKE: 'order-end',
    TRANSPORT: 'order-end',
    WAIT_TO_CONFIRM: 'order-end',
    WAIT_FOR_PAYMENT: 'order-end',
    PAID: 'order-end',
    LIQUIDATION_COMPLETED: 'order-end',
    OVER_TIME: 'order-cancel',
    APPLY_FOR_REFUND: 'order-cancel',
    WAITE_TO_REFUNDDE: 'order-cancel',
    REFUND_FINISHT: 'order-cancel',
    INVALID: 'order-cancel',
  }
  const onClick = (data) => {
    console.log(data)
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
  }
  const columnsData = [
    {
      render: (text, record) => {
        return (
          <Card className={styles.card}
            title={
              <div className={styles.title}>
                <Iconfont type="fasong" className={styles.send} />
                <span>{record.originArea.areaName}</span>
                <Icon type="swap-right" className={styles.dao} />
                <span>{record.destArea.areaName}</span>
              </div>
            }
            onClick={onClick.bind(this, record)}
            extra={
              <span className={styles.time}>{record.createdDate}</span>
            }
          >
            <div className={styles.content}>
              <span className={styles.name}>收货地址:</span>
              <span className={styles.value}>{record.originAddress}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送地址:</span>
              <span className={styles.value}>{record.destAddress}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>重量:</span>
              <span className={styles.value}>{`${record.commodityWeight}kg`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>体积:</span>
              <span className={styles.value}>{`${record.commodityVolume}m³`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>价格:</span>
              <span className={styles.value}>{`${record.amount}元`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>订单状态:</span>
              <span className={`${styles.value} ${styles[OrderStautClazz[record.orderTrans]]}`}>{orderState[record.orderTrans]}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>征集状态:</span>
              <span className={`${styles.value} ${styles[OrderStautClazz[record.orderTrans]]}`}>{collectStaut[record.orderTrans]}</span>
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
    tableTime,
    showHeader: false,
    bordered: false,
  }
  const getInfo = {
    province: {
      label: '取货地址',
      key: 'quProvince',
      outKey: 'originAreaCode',
      required: false,
    },
    form,
  }
  const shouInfo = {
    province: {
      label: '收货地址',
      key: 'shouProvince',
      outKey: 'destAreaCode',
      required: false,
    },
    form,
  }
  const queryCarCollect = () => {
    let obj = {}
    const value = form.getFieldsValue()
    console.log(value)
    if (value.quProvince || value.shouProvince) {
      for (let key in value) {
        if (key === 'originAreaCode' || key === 'destAreaCode') {
          if (value[key]) {
            obj[key] = value[key]
          }
        }
      }
    }
    obj.settlementType = 'LEVY_ONLINE_PAYMENT'
    obj.orderTypes = ['FSL', 'LTL']
    dispatch({ type: 'carCollect/changeStates', payload: { fields: obj } })
  }
  const rest = () => {
    form.resetFields()
  }
  return (
    <div>
      <Form>
        <Card>
          <Row>
            <Col span={7}>
              <AreaSelect {...getInfo} placeholder="请选择取货地址" />
            </Col>
            <Col span={7} offset={1}>
              <AreaSelect {...shouInfo} placeholder="请选收货地址" />
            </Col>
            <Col span={7} offset={1} style={{ marginTop: '32px' }}>
              <Button type="primary" onClick={queryCarCollect}>查询</Button>
              <Button type="primary" onClick={rest}>重置</Button>
            </Col>
          </Row>
        </Card>
      </Form>
      <Card>
        <Ttable {...tableProps} />
      </Card>
    </div>
  )
}

CarCollect.propTypes = {
  form: PropTypes.isRequired,
  carCollect: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const CarCollectForm = Form.create()(CarCollect)
export default connect(({ carCollect }) => ({ carCollect }))(CarCollectForm)
