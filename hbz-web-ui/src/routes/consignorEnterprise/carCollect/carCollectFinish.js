import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Select, Rate } from 'antd'
import { Iconfont } from 'components'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option

const CarCollectFinish = ({
  form,
  dispatch,
  app,
  carCollectFinish,
}) => {
  const { getFieldDecorator } = form
  const { tableData } = app
  const { driverType } = tableData
  const { id, orderDetail, orderTender, orderConsignor } = carCollectFinish
  const onClick = () => {
    console.log('ID', id)
    browserHistory.push(`/consignorEnterprise/carCollect/carCollectDetail?orderId=${id}`)
  }
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
                  <Input disabled />
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
                  <Input disabled />
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
                  <Select disabled>
                    {
                      driverType.map(item => (
                        <Option value={item.val}>{item.key}</Option>
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
                  <Input disabled />
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
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="信誉等级">
                {getFieldDecorator('starLevel', {
                  initialValue: orderTender && orderTender.starLevel,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title="承运方">
          <Row>
            <Col span={11}>
              <FormItem label="公司名称">
                {getFieldDecorator('orgName', {
                  initialValue: orderConsignor && orderConsignor.user.orgName,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="联系人">
                {getFieldDecorator('nickName', {
                  initialValue: orderConsignor && orderConsignor.user.nickName,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row>
            <Col span={11}>
              <FormItem label="联系电话">
                {getFieldDecorator('telephone', {
                  initialValue: orderConsignor && orderConsignor.user.telephone,
                })(
                  <Input disabled />
                )}
              </FormItem>
            </Col>
            <Col span={11} offset={2}>
              <FormItem label="平台信誉">
                {getFieldDecorator('starLevel', {
                  initialValue: orderConsignor && orderConsignor.user.starLevel,
                })(
                  <Rate disabled />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card>
          <Row className={styles['check-detail']}>
            <Col>
              <Button type="primary" onClick={onClick}>查 看 订 单</Button>
            </Col>
          </Row>
        </Card>
      </Form>
    </div>
  )
}

CarCollectFinish.propTypes = {
  form: PropTypes.isRequired,
  carCollectFinish: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const CarCollectFinishForm = Form.create()(CarCollectFinish)
export default connect(({ carCollectFinish, app }) => ({ carCollectFinish, app }))(CarCollectFinishForm)
