import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Button, Input, Select } from 'antd'
import { Iconfont, SelectArea } from 'components'
import { browserHistory } from 'react-router'
import styles from './index.less'

const FormItem = Form.Item

const CheckVehicleDetail = ({
  form,
  dispatch,
  app,
  checkVehicleDetail,
}) => {
  const { getFieldDecorator } = form
  const { vehicleDetailData, transTypes, collectionData, driverList, id, showTakeInBtn } = checkVehicleDetail
  console.log('showTakeInBtn', showTakeInBtn)
  const originAddressDetail = vehicleDetailData &&
  `${vehicleDetailData.originArea.level1Name}${vehicleDetailData.originArea.level2Name}${vehicleDetailData.originArea.level3Name}${vehicleDetailData.originAddress}`
  const destAddressDetail = vehicleDetailData &&
  `${vehicleDetailData.destArea.level1Name}${vehicleDetailData.destArea.level2Name}${vehicleDetailData.destArea.level3Name}${vehicleDetailData.destAddress}`
  const transTypeObj = []
  for (let key of Object.keys(transTypes)) {
    transTypeObj.push(<Option value={key}>{transTypes[key]}</Option>)
  }
  const { tableData } = app
  const { driverType, capital, driverLevel, security } = tableData
  const goBack = () => {
    browserHistory.goBack()
  }
  const takeIn = () => {
    dispatch({
      type: 'checkVehicleDetail/takeInCollect',
      payload: {
        orderId: id,
      },
    })
  }
  return (
    <div>
      <Form>
        <Card title={<div className={styles.form}><Iconfont type="guanli" className={styles.icon} /><span>订单详情</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="收货地址">
                {getFieldDecorator('originAddress', {
                  initialValue: originAddressDetail,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="配送地址">
                {getFieldDecorator('destAddress', {
                  initialValue: destAddressDetail,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="订单编号">
                {getFieldDecorator('orderNo', {
                  initialValue: vehicleDetailData && vehicleDetailData.orderNo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="货物名称">
                {getFieldDecorator('commodityName', {
                  initialValue: vehicleDetailData && vehicleDetailData.commodityName,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="货物重量">
                {getFieldDecorator('linkTelephone', {
                  initialValue: vehicleDetailData && vehicleDetailData.commodityWeight,
                })(
                  <Input disabled addonAfter={<span>KG</span>} />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="货物体积">
                {getFieldDecorator('commodityVolume', {
                  initialValue: vehicleDetailData && vehicleDetailData.commodityVolume,
                })(
                  <Input disabled addonAfter={<span>m³</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="车辆类型">
                {getFieldDecorator('transType', {
                  initialValue: vehicleDetailData && vehicleDetailData.transType,
                })(
                  <Select disabled>
                    {
                      transTypeObj
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={1}>
              <FormItem label="货物价格">
                {getFieldDecorator('amount', {
                  initialValue: vehicleDetailData && vehicleDetailData.amount,
                })(
                  <Input disabled addonAfter={<span>元</span>} />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div className={styles.form}><Iconfont type="dui" className={styles.icon} /><span>征集条件</span></div>}>
          <Row>
            <Col span={11}>
              <FormItem label="司机类型">
                {getFieldDecorator('need', {
                  initialValue: collectionData && collectionData.need.toString(),
                })(
                  <Select disabled>
                    {
                      driverType.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="注册资金">
                {getFieldDecorator('registryMoney', {
                  initialValue: collectionData && collectionData.registryMoney.toString(),
                })(
                  <Select disabled>
                    {
                      capital.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="保证金额度">
                {getFieldDecorator('bond', {
                  initialValue: collectionData && collectionData.bond.toString(),
                })(
                  <Select disabled>
                    {
                      security.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="信誉等级">
                {getFieldDecorator('starLevel', {
                  initialValue: collectionData && collectionData.starLevel.toString(),
                })(
                  <Select disabled>
                    {
                      driverLevel.map(item => (
                        <Option value={item.val}>{item.name}</Option>
                      ))
                    }
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Form>
      <Card title={<div className={styles.form}><Iconfont type="jiaose" className={styles.icon} /><span>参与司机</span></div>}>
        <Row>
          <Col style={{ fontSize: '16px' }}>
            <span>现参与司机：</span>
            <span>{`${driverList.length}人`}</span>
          </Col>
        </Row>
      </Card>
      <Card className={styles.btn}>
        <Row>
          <Col>
            <Button type="primary" style={{ background: '#31ccd2' }} onClick={goBack}>返 回</Button>
            {
              showTakeInBtn ? (<Button style={{ background: '#f8a13a' }} onClick={takeIn}>参 与 征 集</Button>) : null
            }
            {/* <Button className={styles.collection} onClick={takeIn}>参 与 征 集</Button> */}
          </Col>
        </Row>
      </Card>
    </div>
  )
}

CheckVehicleDetail.propTypes = {
  form: PropTypes.isRequired,
  checkVehicleDetail: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
  app: PropTypes.isRequired,
}

const checkVehicleDetailForm = Form.create()(CheckVehicleDetail)
export default connect(({ checkVehicleDetail, app }) => ({ checkVehicleDetail, app }))(checkVehicleDetailForm)
