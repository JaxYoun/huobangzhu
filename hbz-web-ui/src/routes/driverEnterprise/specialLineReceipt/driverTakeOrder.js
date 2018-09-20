import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Form, Row, Col, Input, Button, Rate } from 'antd'
import { AreaSelect, Ttable } from 'components'
import { regex } from 'utils'
import { browserHistory } from 'react-router'
import styles from './index.less'

const FormItem = Form.Item

const DriverTakeOrder = ({
  dispatch,
  specialLineReceipt,
  form,
}) => {
  const { orderType, fields, tableTime } = specialLineReceipt
  const { getFieldDecorator } = form
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
  const query = () => {
    let obj = {}
    const value = form.getFieldsValue()
    if (value.quProvince || value.shouProvince || value.commodityWeight || value.amount) {
      for (let key in value) {
        if (key === 'originAreaCode' || key === 'destAreaCode' || key === 'commodityWeight' || key === 'amount') {
          if (value[key]) {
            obj[key] = value[key]
          }
        }
      }
    }
    dispatch({
      type: 'specialLineReceipt/changeStates',
      payload: {
        fields: obj,
      },
    })
  }
  const rest = () => {
    form.resetFields()
  }
  const getArea = (data, area) => {
    let areaName = ''
    if (data[area].level === 2) {
      areaName = `${data[area].level1Name}${data[area].level2Name}`
    } else {
      areaName = `${data[area].level1Name}${data[area].level2Name}${data[area].level3Name}`
    }
    return areaName
  }
  const checkOrderDetail = (data) => {
    browserHistory.push(`/driverEnterprise/specialLineReceipt/driverOrderDetail?orderId=${data}&orderType=${orderType}`)
  }
  const columnsData = [
    {
      render: (text, record) => {
        return (
          <Card
            className={styles.card}
            onClick={checkOrderDetail.bind(this, record.id)}
          >
            <div className={styles.content}>
              <span className={styles.name}>收货地址:</span>
              <span className={styles.value}>{record.originAddress}</span>
              {/* <span className={styles.value}>{getArea(record, 'originArea')}{record.originAddress}</span> */}
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送地址:</span>
              <span className={styles.value}>{record.destAddress}</span>
              {/* <span className={styles.value}>{getArea(record, 'destArea')}{record.destAddress}</span> */}
            </div>
            <div className={styles.content}>
              <span className={styles.name}>重量:</span>
              <span className={styles.value}>{record.commodityWeight}吨</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>体积:</span>
              <span className={styles.value}>{record.commodityVolume}m³</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>运输价格:</span>
              <span className={styles.value}>{record.amount}元</span>
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
    sourceUrl: orderType === 'flsOrder' ? '/web/flsOrder/getAvailableFslOrderByBage' : '/web/ltlOrder/getAvailableLtlOrderByPage',
    fields: fields || {},
    tableTime,
    showHeader: false,
    bordered: false,
  }
  return (
    <div>
      <Card>
        <Form>
          <Row>
            <Col span={7}>
              <AreaSelect {...getInfo} placeholder="请选择收货地址" />
            </Col>
            <Col span={7} offset={1}>
              <AreaSelect {...shouInfo} placeholder="请选择配送地址" />
            </Col>
            <Col span={7} offset={1}>
              <FormItem label="最低载重">
                {getFieldDecorator('commodityWeight', {
                  rules: [
                    { pattern: regex.number, message: '请输入正确的重量' },
                  ],
                })(
                  <Input placeholder="请输入最低载重" addonAfter={<span>吨</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={7}>
              <FormItem label="最低价格">
                {getFieldDecorator('amount', {
                  rules: [
                    { pattern: regex.number, message: '请输入正确的价格' },
                  ],
                })(
                  <Input placeholder="请输入最低价格" addonAfter={<span>元</span>} />
                )}
              </FormItem>
            </Col>
            <Col span={7} offset={1}>
              <Button type="primary" style={{ marginTop: '32px' }} onClick={query}>查询</Button>
              <Button type="primary" style={{ marginTop: '32px' }} onClick={rest}>重置</Button>
            </Col>
          </Row>
        </Form>
      </Card>
      <Card>
        <Ttable {...tableProps} />
      </Card>
    </div>
  )
}

DriverTakeOrder.propTypes = {
  form: PropTypes.isRequired,
  specialLineReceipt: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const DriverTakeOrderForm = Form.create()(DriverTakeOrder)
export default connect(({ specialLineReceipt }) => ({ specialLineReceipt }))(DriverTakeOrderForm)
