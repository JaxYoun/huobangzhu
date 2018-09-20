import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, Radio, Select } from 'antd'
import { Iconfont } from 'components'
import { browserHistory } from 'react-router'
import { address } from 'utils'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

const DriverOrderDetail = ({
  dispatch,
  driverOrderDetail,
  loading,
  form,
}) => {
  const { getFieldDecorator } = form
  const { orderDetail, transTypes, id, orderType } = driverOrderDetail
  const transTypeObj = []
  for (let key of Object.keys(transTypes)) {
    transTypeObj.push(<Option value={key}>{transTypes[key]}</Option>)
  }
  const getArea = (area) => {
    let areaName = ''
    if (orderDetail) {
      console.log('orderDetail', orderDetail)
      if (orderDetail[area].level === 2) {
        areaName = `${orderDetail[area].level1Name} / ${orderDetail[area].level2Name}`
      } else {
        areaName = `${orderDetail[area].level1Name} / ${orderDetail[area].level2Name} / ${orderDetail[area].level3Name}`
      }
    }
    return areaName
  }
  const takeOrder = () => {
    dispatch({
      type: 'driverOrderDetail/driverTakeOrder',
      payload: {
        id,
        orderType,
      },
    })
  }
  return (
    <div>
      <Form className={styles.form}>
        <Row>
          <Col>
            <Card className={styles['order-title-card']}>
              <h2 className={styles['confirm-order-title']}>
                订单编号：{orderDetail.orderNo}
              </h2>
            </Card>
          </Col>
          <Col>
            <Card title="货物详情">
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="货物名称">
                    {getFieldDecorator('commodityName', {
                      initialValue: orderDetail.commodityName,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                  <Row gutter={0}>
                    <Col span="24">
                      <FormItem label="预估重量">
                        {getFieldDecorator('commodityWeight', {
                          initialValue: orderDetail.commodityWeight,
                        })(
                          <Input disabled addonAfter={<span>吨</span>} />
                        )}
                      </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col span="24">
                      <FormItem label="预估体积">
                        {getFieldDecorator('commodityVolume', {
                          initialValue: orderDetail.commodityVolume,
                        })(
                          <Input disabled addonAfter={<span>m³</span>} />
                        )}
                      </FormItem>
                    </Col>
                  </Row>
                </Col>
                <Col span="12">
                  <FormItem label="货物描述">
                    {getFieldDecorator('commodityDescribe', {
                      initialValue: orderDetail.commodityDescribe,
                    })(
                      <TextArea disabled autosize={{ minRows: 11, maxRows: 18 }} />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col>
            <Card title="车辆要求">
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="车辆类型">
                    {getFieldDecorator('transType', {
                      initialValue: orderDetail.transType || 'UNLIMITED',
                    })(
                      <Select disabled>
                        {transTypeObj}
                      </Select>
                    )}
                  </FormItem>
                </Col>
                <Col span="12">
                  <Row gutter={0}>
                    <Col span="24">
                      <FormItem label="最低载重">
                        {getFieldDecorator('maxLoad', {
                          initialValue: orderDetail.maxLoad,
                        })(
                          <Input disabled addonAfter={<span>吨</span>} />
                        )}
                      </FormItem>
                    </Col>
                  </Row>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col>
            <Card title="费用">
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="单价">
                    {getFieldDecorator('unitPrice', {
                      initialValue: orderDetail.unitPrice,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="预估总价">
                    {getFieldDecorator('amount', {
                      initialValue: orderDetail.amount,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col>
            <Card
              title="取货信息"
            >
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="取货地址">
                    {getFieldDecorator('originAreaCode', {
                      initialValue: orderDetail ? getArea('originArea') : '',
                    })(<Input disabled />)}
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="取货详细地址">
                    {getFieldDecorator('originAddress', {
                      initialValue: orderDetail.originAddress,
                    })(<Input disabled />)}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col>
            <Card
              title="送货信息"
            >
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="收货地址">
                    {getFieldDecorator('destAreaCode', {
                      initialValue: orderDetail ? getArea('destArea') : '',
                    })(<Input disabled />)}
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="收货详细地址">
                    {getFieldDecorator('destAddress', {
                      initialValue: orderDetail.destAddress,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col>
            <Card
              title="联系人方式"
            >
              <Row gutter="48">
                <Col span="12">
                  <FormItem label="联系人">
                    {getFieldDecorator('linkMan', {
                      initialValue: orderDetail.linkMan,
                    })(
                      <Input disabled maxLength="30" />
                    )}
                  </FormItem>
                </Col>
                <Col span="12">
                  <FormItem label="取货电话">
                    {getFieldDecorator('linkTelephone', {
                      initialValue: orderDetail.linkTelephone,
                    })(
                      <Input disabled />
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row gutter="48">
                <Col span="48">
                  <FormItem label="补充说明">
                    {getFieldDecorator('linkRemark', {
                      initialValue: orderDetail.linkRemark,
                    })(
                      <TextArea disabled rows={4} />
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Card>
          </Col>
        </Row>
      </Form>
      <Card>
        <Row className={styles.take}>
          <Col>
            <Button type="primary" loading={loading.global} onClick={takeOrder}>确 认 接 单</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

DriverOrderDetail.propTypes = {
  driverOrderDetail: PropTypes.object,
  dispatch: PropTypes.func.isRequire,
  form: PropTypes.isRequired,
  loading: PropTypes.object,
}

const DriverOrderDetailForm = Form.create()(DriverOrderDetail)
export default connect(({ driverOrderDetail, loading }) => ({ driverOrderDetail, loading }))(DriverOrderDetailForm)
