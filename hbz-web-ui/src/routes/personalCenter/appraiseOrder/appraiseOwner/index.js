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

const AppraiseOwner = ({
  dispatch,
  appraiseOwner,
  form,
}) => {
  const { getFieldDecorator } = form
  const { fields, tableTime, orderTypeData } = appraiseOwner
  const orderTypeObj = []
  for (let key of Object.keys(orderTypeData)) {
    orderTypeObj.push(<Option value={key}>{orderTypeData[key]}</Option>)
  }
  const { orderTypes } = config
  const goToAppraise = (data) => {
    browserHistory.push(`/personalCenter/appraiseOrder/appraise?orderId=${data.order.id}&type=owner`)
  }
  const queryOwner = () => {
    let obj = {}
    const value = form.getFieldsValue()
    for (let key in value) {
      if (value[key]) {
        if (key === 'createdDate') {
          obj[key] = value[key].format('YYYY-MM-DD HH:mm:ss')
        } else {
          obj[key] = value[key]
        }
      }
    }
    obj.type = 'CONSIGNOR'
    dispatch({
      type: 'appraiseOwner/changeStates',
      payload: {
        fields: obj,
        tableTime: new Date().getTime(),
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
          <Card
            className={styles.card}
            title={
              <div className={styles.title}>
                <Iconfont type="huowu" className={styles.send} />
                <span>{record.order.orderNo}</span>
              </div>
            }
            onClick={goToAppraise.bind(this, record)}
            extra={
              <span className={styles.time}>去评价</span>
            }
          >
            <div className={styles.content}>
              <span className={styles.name}>订单类型:</span>
              <span className={styles.value}>{orderTypes[record.order.orderType]}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送地址:</span>
              <span className={styles.value}>{record.order.destInfo + record.order.destAddress}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>发单时间:</span>
              <span className={styles.value}>{record.createdDate}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>总费用:</span>
              <span className={styles.value}>{`${record.order.amount}元`}</span>
            </div>
          </Card>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/rate/queryPage',
    fields: fields || {},
    tableTime,
    showHeader: false,
    bordered: false,
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
            <FormItem label="发单时间">
              {getFieldDecorator('createdDate', {
              })(
                <DatePicker style={{ width: '100%' }} />
              )}
            </FormItem>
          </Col>
          <Col span={7} offset={2}>
            <Button type="primary" style={{ marginTop: '34px' }} onClick={queryOwner}>查询</Button>
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

AppraiseOwner.propTypes = {
  appraiseOwner: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
  form: PropTypes.object,
}

const AppraiseOwnerForm = Form.create()(AppraiseOwner)
export default connect(({ appraiseOwner }) => ({ appraiseOwner }))(AppraiseOwnerForm)
