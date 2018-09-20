import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Row, Col, Input, Button, Select, Radio } from 'antd'
import { Iconfont } from 'components'
import styles from './index.less'

const RadioGroup = Radio.Group
const RadioButton = Radio.Button

const Pay = ({
  pay,
  dispatch,
  payBail,
}) => {
  let radioButton = []
  const { aliPay, weChat, union, payType, orderDetail } = pay
  const obj = {
    Alipay: aliPay,
    Wechat: weChat,
    Union: union,
  }
  const { payDetail } = payBail
  console.log('orderDetail', orderDetail)
  for (let key in payType) {
    if ({}.hasOwnProperty.call(payType, key)) {
      radioButton.push(
        <RadioButton value={key}>
          <img src={`/${key}.png`} alt="logo" />
          {payType[key]}
          {
            obj[key] ?
              <Iconfont type="chenggong" className={styles.paychoice} /> :
              <Iconfont type="yuanquan-copy" className={styles.noChoice} />
          }
        </RadioButton>
      )
    }
  }
  const onChange = (e) => {
    switch (e.target.value) {
      case 'Alipay':
        dispatch({
          type: 'pay/changeStates',
          payload: { aliPay: true, weChat: '', union: '' },
        })
        break
      case 'Wechat':
        dispatch({
          type: 'pay/changeStates',
          payload: { aliPay: '', weChat: true, union: '' },
        })
        break
      case 'Union':
        dispatch({
          type: 'pay/changeStates',
          payload: { aliPay: '', weChat: '', union: true },
        })
        break
      default:
        return
    }
  }
  return (
    <div>
      <Card>
        <Row className={styles['online-title']}>
          <Col span={24}>
            <h1>订单编号: {payDetail.orderNo}</h1>
            <span>订单类型: WAREHOUSE_EARNEST</span>
          </Col>
        </Row>
        <Row className={styles['online-price']}>
          <Col span={24}>
            <span >{`￥${payDetail.warehousePrice || 0}`}</span>
          </Col>
        </Row>
        <Row>
          <Col>
            <div className={styles['pay-grounp']}>
              <RadioGroup defaultValue="a" size="large" onChange={onChange}>
                {radioButton}
              </RadioGroup>
            </div>
          </Col>
        </Row>
      </Card>
      <Card>
        <Row className={styles['pay-button']}>
          <Col>
            <Button type="primary">支 付 确 认</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}
Pay.propTypes = {
  pay: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  payBail: PropTypes.isRequired,
}
export default connect(({ pay, payBail }) => ({ pay, payBail }))(Pay)
