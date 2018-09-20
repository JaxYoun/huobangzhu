import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Button, Select } from 'antd'
import { Iconfont, Ttable, Address } from 'components'
import { browserHistory } from 'react-router'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option

const MyJoinOrder = ({
  dispatch,
  form,
  myJoinOrder,
}) => {
  const { getFieldDecorator } = form
  let { fields, tableTime } = myJoinOrder
  const originProps = {
    address: {
      key: 'originInfo',
      value: '',
    },
    label: '发货地址',
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
  const collectStaut = {
    TAKEING: '征集中',
    TAKE: '征集完成',
    DISABLE: '排除征集',
  }
  const collectStautClazz = {
    TAKEING: 'collect_confirmed',
    TAKE: 'collect_wait',
    DISABLE: 'collect_received',
  }
  const checkVehicleDetail = (id) => {
    browserHistory.push(`/driverEnterprise/vehicleCollect/checkVehicleDetail?orderType=joinOrder&orderId=${id}`)
  }
  const search = () => {
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
    if (!value.takeType) {
      value.takeType = 'TAKEING'
    }
    let obj = {}
    for (let key in value) {
      if (value[key]) {
        obj[key] = value[key]
      }
    }
    dispatch({
      type: 'myJoinOrder/changeStates',
      payload: {
        fields: obj,
      },
    })
  }
  const rest = () => {
    form.resetFields()
  }
  const columnsData = [
    {
      render: (text, record) => {
        return (
          <Card className={styles.card}
            title={
              <div className={styles.title}>
                <Iconfont type="fasong" className={styles.send} />
                <span>{record.order.originArea.areaName}</span>
                <Icon type="swap-right" className={styles.dao} />
                <span>{record.order.destArea.areaName}</span>
              </div>
            }
            onClick={checkVehicleDetail.bind(this, record.order.id)}
            extra={
              <span className={styles.time}>{record.order.createdDate}</span>
            }
          >
            <div className={styles.content}>
              <span className={styles.name}>收货地址:</span>
              <span className={styles.value}>{record.order.originAddress}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送地址:</span>
              <span className={styles.value}>{record.order.destAddress}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>重量:</span>
              <span className={styles.value}>{`${record.order.commodityWeight}kg`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>体积:</span>
              <span className={styles.value}>{`${record.order.commodityVolume}m³`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>价格:</span>
              <span className={styles.value}>{`${record.order.amount}元`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>征集状态:</span>
              <span className={`${styles.value} ${styles[collectStautClazz[record.takeType]]}`}>{collectStaut[record.takeType]}</span>
            </div>
          </Card>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/order/taker/driver/queryPage',
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
              <Address {...originProps} placeholder="请输入发货地址" />
            </Col>
            <Col span={7} offset={1}>
              <Address {...destProps} placeholder="请输入收货地址" />
            </Col>
            <Col span={7} offset={1}>
              <FormItem label="征集状体">
                {getFieldDecorator('takeType', {
                })(
                  <Select placeholder="请选择征集状态">
                    <Option value="TAKEING">征集中</Option>
                    <Option value="TAKE">征集完成</Option>
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col>
              <Button type="primary" onClick={search}>查询</Button>
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

MyJoinOrder.propTypes = {
  form: PropTypes.isRequired,
  myJoinOrder: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const MyJoinOrderForm = Form.create()(MyJoinOrder)
export default connect(({ myJoinOrder }) => ({ myJoinOrder }))(MyJoinOrderForm)
