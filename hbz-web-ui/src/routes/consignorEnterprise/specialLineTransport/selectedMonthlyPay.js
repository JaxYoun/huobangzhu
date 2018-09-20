import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, message } from 'antd'
import { Iconfont } from 'components'
import { Link } from 'react-router'
import { address } from 'utils'
import moment from 'moment'

const FormItem = Form.Item
import styles from './index.less'


const FslOrder = ({ dispatch, specialLineTransport, form }) => {
  const { orderDetail, confirmLoading, DriverList, takeUserId } = specialLineTransport
  const { getFieldDecorator, getFieldValue } = form
  console.log('DriverList', DriverList)
  const save = () => {
    if (!takeUserId) {
      message.warn('请选择司机！')
      return
    }
    dispatch({ type: 'specialLineTransport/monthPayConfirm', payload: { takeUserId } })
  }
  const queryDrivers = () => {
    const searchFiled = getFieldValue('searchFiled')
    dispatch({
      type: 'specialLineTransport/getDriverList',
      payload: { searchFiled },
    })
  }
  const selectDriver = (driverId) => {
    dispatch({
      type: 'specialLineTransport/changeStates',
      payload: { takeUserId: driverId },
    })
  }
  const driversArr = DriverList.map(item => {
    let driverRole = ''
    if (item.TransEnterprise) {
      driverRole = '企业'
    } else {
      driverRole = '个人'
    }
    return (
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
    )
  })
  return (<div className={styles.form}>
    <Row>
      <Col span={18}>
        <Card className={styles['sourch-drivers']}>
          <Form>
            <FormItem >
              {getFieldDecorator('searchFiled', {
              })(
                <Input
                  prefix={<Icon type="search" />}
                  suffix={<Button onClick={queryDrivers}>确定</Button>}
                  maxLength="15"
                  size="large"
                  placeholder="请输入司机名称或手机号码"
                />
              )}
            </FormItem>
          </Form>
        </Card>
      </Col>
    </Row>
    <Row>
      <Col>
        <Card className={styles['order-title-card']}>
          <h2 className={styles['confirm-order-title']}>
            订单编号：{orderDetail.orderNo}
          </h2>
        </Card>
      </Col>
      <Col className={styles['select-monthly-pay-list']}>
        {driversArr}
      </Col>
      <Col>
        <Card className={styles['bottem-card']}>
          <Col className={styles['bottem-btns']} >
            <Button type="primary" size="large" className={styles.save} loading={confirmLoading} onClick={save} >月结方式确认</Button>
          </Col>
        </Card>
      </Col>
    </Row>
  </div>)
}

FslOrder.propTypes = {
  form: PropTypes.isRequired,
  specialLineTransport: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const FslOrderForm = Form.create()(FslOrder)
export default connect(({ specialLineTransport }) => ({ specialLineTransport }))(FslOrderForm)
