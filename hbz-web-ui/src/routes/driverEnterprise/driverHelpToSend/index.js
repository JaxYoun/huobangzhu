import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Card, Form, Row, Col, Button, Rate } from 'antd'
import { Iconfont, Address, Ttable } from 'components'
import styles from './index.less'

const DriverHelpToSend = ({
  dispatch,
  form,
  driverHelpToSend,
}) => {
  const { fields, tableTime } = driverHelpToSend
  const originProps = {
    address: {
      key: 'originInfo',
      value: '',
    },
    label: '取货地址',
    requir: true,
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
  const destProps = {
    address: {
      key: 'destInfo',
      value: '',
    },
    label: '收货地址',
    requir: true,
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
  const checkDetail = (data) => {
    browserHistory.push(`/driverEnterprise/driverHelpToSend/confirmIsTakeSend?orderId=${data.id}`)
  }
  const querySendOrder = () => {
    const value = form.getFieldsValue()
    if (value.destInfo === '') {
      form.resetFields(['destX', 'destY'])
      value.destX = ''
      value.destY = ''
    }
    if (value.originInfo === '') {
      form.resetFields(['originX', 'originY'])
      value.originX = ''
      value.originY = ''
    }
    let obj = {}
    for (let key in value) {
      if (value[key]) {
        obj[key] = value[key]
      }
    }
    dispatch({ type: 'driverHelpToSend/changeStates', payload: { fields: obj } })
  }
  const rest = () => {
    form.resetFields()
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
              <span className={styles.name}>取货地址:</span>
              <span className={styles.value}>{record.originInfo}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送地址:</span>
              <span className={styles.value}>{record.destInfo}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>商品重量:</span>
              <span className={styles.value}>{`${record.commodityWeight}KG`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>商品体积:</span>
              <span className={styles.value}>{`${record.commodityVolume}m³`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送价格:</span>
              <span className={styles.value}>{`${record.amount}元`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>发布人信誉:</span>
              <span className={styles.value}><Rate value={record.createUser.starLevel} disabled /></span>
            </div>
          </Card>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/order/send/task/queryPage',
    fields: fields || {},
    tableTime,
    showHeader: false,
    bordered: false,
  }
  return (
    <div>
      <Form>
        <Card>
          <Row>
            <Col span={7}>
              <Address {...originProps} placeholder="请选择取货地址" />
            </Col>
            <Col span={7} offset={1}>
              <Address {...destProps} placeholder="请选择收货地址" />
            </Col>
            <Col span={7} offset={1} style={{ marginTop: '32px' }}>
              <Button type="primary" onClick={querySendOrder}>查询</Button>
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

DriverHelpToSend.propTypes = {
  form: PropTypes.isRequired,
  driverHelpToSend: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const DriverHelpToSendForm = Form.create()(DriverHelpToSend)
export default connect(({ driverHelpToSend }) => ({ driverHelpToSend }))(DriverHelpToSendForm)
