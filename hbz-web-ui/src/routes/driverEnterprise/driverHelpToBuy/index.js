import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Rate } from 'antd'
import { Iconfont, Address, Ttable } from 'components'
import styles from './index.less'

const FormItem = Form.Item

const DriverHelpToBuy = ({
  dispatch,
  driverHelpToBuy,
  form,
}) => {
  const { getFieldDecorator } = form
  const addressProps = {
    address: {
      key: 'destInfo',
      value: '',
    },
    // label: '联系地址',
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
  const { fields, tableTime } = driverHelpToBuy
  const timeLimitObj = {
    Assign: '指派',
    Immediately: '立即',
  }
  const checkDetail = (data) => {
    browserHistory.push(`/driverEnterprise/driverHelpToBuy/confirmIsTakeCase?orderId=${data.id}`)
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
              <span className={styles.name}>联系人:</span>
              <span className={styles.value}>{record.linker}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>联系电话:</span>
              <span className={styles.value}>{record.linkTelephone}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送地址:</span>
              <span className={styles.value}>{record.destInfo}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>商品预估价格:</span>
              <span className={styles.value}>{`${record.commodityAmount}元`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送费用:</span>
              <span className={styles.value}>{`${record.amount}元`}</span>
            </div>
            <div className={styles.content}>
              <span className={styles.name}>配送要求:</span>
              <span className={styles.value}>{timeLimitObj[record.timeLimit]}</span>
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
    sourceUrl: '/order/buy/task/queryPage',
    fields: fields || {},
    tableTime,
    showHeader: false,
    bordered: false,
  }
  const search = () => {
    const value = form.getFieldsValue()
    if (value.destInfo === '') {
      form.resetFields(['destX', 'destY'])
      value.destX = ''
      value.destY = ''
    }
    let obj = {}
    for (let key in value) {
      if (value[key]) {
        obj[key] = value[key]
      }
    }
    dispatch({
      type: 'driverHelpToBuy/changeStates',
      payload: {
        fields: obj,
      },
    })
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
              <Address {...addressProps} placeholder="请输入配送地址" />
            </Col>
            <Col span={7} offset={2}>
              <FormItem label="商品名称">
                {getFieldDecorator('commodityName', {
                })(
                  <Input placeholder="请输入搜索的商品名称" />
                )}
              </FormItem>
            </Col>
            <Col span={5} offset={2}>
              <Button type="primary" onClick={search} style={{ marginTop: '34px' }}>查询</Button>
              <Button type="primary" onClick={rest} style={{ marginTop: '34px' }}>重置</Button>
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

DriverHelpToBuy.propTypes = {
  form: PropTypes.isRequired,
  driverHelpToBuy: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const DriverHelpToBuyForm = Form.create()(DriverHelpToBuy)
export default connect(({ driverHelpToBuy }) => ({ driverHelpToBuy }))(DriverHelpToBuyForm)
