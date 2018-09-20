import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Button } from 'antd'
import { Iconfont, AreaSelect, Ttable } from 'components'
import { browserHistory } from 'react-router'
import styles from './index.less'

const VehicleCollect = ({
  dispatch,
  form,
  vehicleCollect,
}) => {
  let { fields, tableTime } = vehicleCollect
  const getInfo = {
    province: {
      label: '收货地址',
      key: 'quProvince',
      outKey: 'originAreaCode',
      required: false,
    },
    form,
  }
  const shouInfo = {
    province: {
      label: '配送地址',
      key: 'shouProvince',
      outKey: 'destAreaCode',
      required: false,
    },
    form,
  }
  const collectStaut = {
    CONFIRMED: '未开始',
    WAIT_TO_TAKE: '征集完成',
    ORDER_TO_BE_RECEIVED: '征集中',
  }
  const collectStautClazz = {
    CONFIRMED: 'collect_confirmed',
    WAIT_TO_TAKE: 'collect_wait',
    ORDER_TO_BE_RECEIVED: 'collect_received',
  }
  const checkVehicleDetail = (id) => {
    browserHistory.push(`/driverEnterprise/vehicleCollect/checkVehicleDetail?orderId=${id}`)
  }
  const queryVehicle = () => {
    const formValue = form.getFieldsValue()
    let obj = {}
    if (formValue.quProvince || formValue.shouProvince) {
      for (let key in formValue) {
        if (key === 'originAreaCode' || key === 'destAreaCode') {
          if (formValue[key]) {
            obj[key] = formValue[key]
          }
        }
      }
    }
    obj.orderTrans = 'ORDER_TO_BE_RECEIVED'
    obj.settlementType = 'LEVY_ONLINE_PAYMENT'
    dispatch({
      type: 'vehicleCollect/changeStates',
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
                <span>{record.originArea.areaName}</span>
                <Icon type="swap-right" className={styles.dao} />
                <span>{record.destArea.areaName}</span>
              </div>
            }
            onClick={checkVehicleDetail.bind(this, record.id)}
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
              <span className={styles.name}>征集状态:</span>
              <span className={`${styles.value} ${styles[collectStautClazz[record.orderTrans]]}`}>{collectStaut[record.orderTrans]}</span>
            </div>
          </Card>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/order/task/near/queryPage',
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
              <AreaSelect {...getInfo} placeholder="请输入收货地址" />
            </Col>
            <Col span={7} offset={1}>
              <AreaSelect {...shouInfo} placeholder="请输入配送地址" />
            </Col>
            <Col span={7} offset={1} style={{ marginTop: '32px' }}>
              <Button type="primary" onClick={queryVehicle}>查询</Button>
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

VehicleCollect.propTypes = {
  form: PropTypes.isRequired,
  vehicleCollect: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}

const VehicleCollectForm = Form.create()(VehicleCollect)
export default connect(({ vehicleCollect }) => ({ vehicleCollect }))(VehicleCollectForm)
