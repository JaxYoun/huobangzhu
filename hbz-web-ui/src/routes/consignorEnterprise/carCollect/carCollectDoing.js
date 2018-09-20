import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Select, Rate } from 'antd'
import { Iconfont } from 'components'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option

const CarCollectDoing = ({
  dispatch,
  app,
  carCollectDoing,
  form,
}) => {
  const { getFieldDecorator } = form
  const { tableData } = app
  const { driverType, capital, driverLevel, security } = tableData
  const { id, transTypes, orderDetail, orderTender, driverList, takeUserId } = carCollectDoing
  console.log('driverList', driverList)
  const transTypeObj = []
  for (let key of Object.keys(transTypes)) {
    transTypeObj.push(<Option value={key}>{transTypes[key]}</Option>)
  }
  const checkOrder = () => {
    browserHistory.push(`/consignorEnterprise/carCollect/carCollectDetail?orderId=${id}`)
  }
  const selectDriver = (driverId) => {
    dispatch({
      type: 'carCollectDoing/changeStates',
      payload: { takeUserId: driverId },
    })
  }
  const driversArr = driverList.map(item => {
    let driverRole = ''
    if (item.TransEnterprise) {
      driverRole = '企业'
    } else {
      driverRole = '个人'
    }
    return (
      <Col span={11} offset={1} className={styles['select-monthly-pay-list']}>
        <Card onClick={selectDriver.bind(this, item.id)} >
          <div className="driver-info">
            <div className="driver-tog">
              <div>{driverRole}</div>
              {takeUserId === item.id && <div className={styles['check-icon']}><Icon type="check" /></div>}
            </div>
            <div className="info-body" >
              <div className="driver-logo">
                <div className="driver-logo-img">
                  <img alt="照片" src={item.logo} />
                </div>
              </div>
              {
                driverRole === '企业' ? (
                  <div className="info-intro">
                    <div calssName="info-list">
                      <h2>{item.orgName}</h2>
                      <table>
                        <tr>
                          <td>姓名：</td><td>{item.nickName}</td>
                        </tr>
                        <tr>
                          <td>电话：</td><td>{item.telephone}</td>
                        </tr>
                      </table>
                    </div>
                    <div className="driver-remark">
                      需要货主提前打电话联系我，再找时间看货，最后才能确定是不是要拉你的货。信息息信才能确定是不信息息信才能确信...
                    </div>
                  </div>
                ) : (
                  <div className="info-intro" >
                    <div className="info-list" >
                      <h2>{item.nickName}</h2>
                      <table>
                        <tr>
                          <td>电话：</td><td>{item.telephone}</td>
                        </tr>
                        <tr>
                          <td>车牌号:</td><td>{item.PersonDriver.licensePlateNumber}</td>
                        </tr>
                        <tr>
                          <td>车辆：</td>
                          <td>
                            <ul className="car-info-list">
                              <li style={{ background: '#14cbcd' }}>{item.PersonDriver.commodityVolume}{item.PersonDriver.volumeUnit}</li>
                              <li style={{ background: '#3396cf' }}>{item.PersonDriver.transType}</li>
                              <li style={{ background: '#a670e4' }}>{item.PersonDriver.commodityWeight}{item.PersonDriver.weightUnit}</li>
                            </ul>
                          </td>
                        </tr>
                      </table>
                    </div>
                    <div className="driver-remark">
                    需要货主提前打电话联系我，再找时间看货，最后才能确定是不是要拉你的货。信息息信才能确定是不信息息信才能确信...
                    </div>
                  </div>
                )
              }
            </div>
          </div>
        </Card>
      </Col>
    )
  })
  return (
    <div>
      <Form>
        <Card title="征集订单">
          <Row>
            <Col span={11}>
              <FormItem label="订单编号">
                {getFieldDecorator('orderNo', {
                  initialValue: orderDetail && orderDetail.orderNo,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="货物名称">
                {getFieldDecorator('commodityName', {
                  initialValue: orderDetail && orderDetail.commodityName,
                })(
                  <Input />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="车辆类型">
                {getFieldDecorator('transType', {
                  initialValue: orderDetail && orderDetail.transType,
                })(
                  <Select>
                    {transTypeObj}
                  </Select>
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="征集条件">
          <Row>
            <Col span={11}>
              <FormItem label="司机类型">
                {getFieldDecorator('need', {
                  initialValue: orderTender && orderTender.need,
                })(
                  <Select>
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
                  initialValue: orderTender && orderTender.registryMoney,
                })(
                  <Select>
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
                  initialValue: orderTender && orderTender.bond,
                })(
                  <Select>
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
                  initialValue: orderTender && orderTender.starLevel,
                })(
                  <Select>
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
      <Card title="参与司机">
        <Row>
          {/* <Col span={11} className={styles['select-monthly-pay-list']}>
            <Card>
              <div className="driver-info">
                <div className="driver-tog">
                  <div>张三</div>
                  <div className={styles['check-icon']}><Icon type="check" /></div>
                </div>
                <div className="info-body" >
                  <div className="driver-logo">
                    <div className="driver-logo-img">
                      <img alt="照片" />
                    </div>
                  </div>
                  <div className="info-intro" >
                    <div className="info-list" >
                      <h2>张三</h2>
                      <table>
                        <tr>
                          <td>电话：</td><td>18608090792</td>
                        </tr>
                        <tr>
                          <td>车牌号:</td><td>川A 3243</td>
                        </tr>
                        <tr>
                          <td>车辆：</td>
                          <td>
                            <ul className="car-info-list">
                              <li style={{ background: '#14cbcd' }}>4.2米</li>
                              <li style={{ background: '#3396cf' }}>箱式</li>
                              <li style={{ background: '#a670e4' }}>4.4吨</li>
                            </ul>
                          </td>
                        </tr>
                      </table>
                    </div>
                    <div className="driver-remark">
                    需要货主提前打电话联系我，再找时间看货,最后才能确定是不是要拉你的货。信息息信才能确定是不信息息信才能确信...
                    </div>
                  </div>
                </div>
              </div>
            </Card>
          </Col> */}
          {driversArr}
        </Row>
      </Card>
      <Card>
        <Row className={styles['check-detail']}>
          <Col>
            <Button type="primary">保 存 司 机</Button>
            <Button type="primary" onClick={checkOrder}>查 看 订 单</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

CarCollectDoing.propTypes = {
  form: PropTypes.isRequired,
  carCollectDoing: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const CarCollectDoingForm = Form.create()(CarCollectDoing)
export default connect(({ carCollectDoing, app }) => ({ carCollectDoing, app }))(CarCollectDoingForm)
