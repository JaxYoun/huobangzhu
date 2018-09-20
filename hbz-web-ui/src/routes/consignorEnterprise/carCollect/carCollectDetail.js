import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { browserHistory } from 'react-router'
import { Icon, Card, Form, Row, Col, Input, Button, Select, Rate, DatePicker } from 'antd'
import { Iconfont } from 'components'
import moment from 'moment'
import styles from './index.less'

const FormItem = Form.Item
const Option = Select.Option
const { TextArea } = Input

const CarCollectDetail = ({
  dispatch,
  carCollectDetail,
  form,
}) => {
  const { getFieldDecorator } = form
  const { WeightUnits, VolumeUnits, transTypes, collectDetail } = carCollectDetail
  const transTypeObj = []
  const WeightUnitArr = []
  const VolumeUnitArr = []
  for (let key of Object.keys(transTypes)) {
    transTypeObj.push(<Option value={key}>{transTypes[key]}</Option>)
  }
  for (let key of Object.keys(WeightUnits)) {
    WeightUnitArr.push(<Option value={key}>{WeightUnits[key]}</Option>)
  }
  for (let key of Object.keys(VolumeUnits)) {
    VolumeUnitArr.push(<Option value={key}>{VolumeUnits[key]}</Option>)
  }
  return (
    <div>
      <Form className={styles.form}>
        <Card title={<div><Iconfont type="gouwu" className={styles.icon} /><span>货物详情</span></div>}>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="货物名称">
                {getFieldDecorator('commodityName', {
                  initialValue: collectDetail.commodityName,
                })(
                  <Input disabled placeholder="请输入货物名称" />
                )}
              </FormItem>
              <Row gutter={0}>
                <Col span="16">
                  <FormItem label="预估重量">
                    {getFieldDecorator('commodityWeight', {
                      initialValue: collectDetail.commodityWeight,
                    })(
                      <Input disabled placeholder="请输入预估重量" />
                    )}
                  </FormItem>
                </Col>
                <Col span="8">
                  <FormItem label=" " colon={false}>
                    {getFieldDecorator('WeightUnit', {
                      initialValue: collectDetail.WeightUnit,
                    })(
                      <Select disabled>
                        {WeightUnitArr}
                      </Select>
                    )}
                  </FormItem>
                </Col>
              </Row>
              <Row>
                <Col span="16">
                  <FormItem label="预估体积">
                    {getFieldDecorator('commodityVolume', {
                      initialValue: collectDetail.commodityVolume,
                    })(
                      <Input disabled placeholder="请输入预估体积" />
                    )}
                  </FormItem>
                </Col>
                <Col span="8">
                  <FormItem label=" " colon={false}>
                    {getFieldDecorator('volumeUnit', {
                      initialValue: collectDetail.volumeUnit,
                    })(
                      <Select disabled>
                        {VolumeUnitArr}
                      </Select>
                    )}
                  </FormItem>
                </Col>
              </Row>
            </Col>
            <Col span="12">
              <FormItem label="接运时间">
                {getFieldDecorator('orderTakeStart', {
                  initialValue: moment(collectDetail.orderTakeStart || ''),
                })(
                  <DatePicker disabled format="YYYY-MM-DD HH:mm" placeholder="请输入接运时间" />
                )}
              </FormItem>
              <FormItem label="货物描述">
                {getFieldDecorator('commodityDescribe', {
                  initialValue: collectDetail.commodityDescribe,
                })(
                  <TextArea disabled autosize={{ minRows: 7, maxRows: 18 }} placeholder="货物描述" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div><Iconfont type="huoche21" className={styles.icon} /><span>车辆要求</span></div>}>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="车辆类型">
                {getFieldDecorator('transType', {
                  initialValue: collectDetail.transType || 'UNLIMITED',
                })(
                  <Select
                    disabled
                    placeholder="请选择车辆类型"
                  >
                    {transTypeObj}
                  </Select>
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="最低载重">
                {getFieldDecorator('maxLoad', {
                  initialValue: collectDetail.maxLoad,
                })(
                  <Input disabled placeholder="请输入最低载重" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div><Iconfont type="feiyong" className={styles.icon} /><span>费用</span></div>}>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="单价">
                {getFieldDecorator('unitPrice', {
                  initialValue: collectDetail.unitPrice,
                })(
                  <Input disabled disabled placeholder="请输入单价" />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="预估总价">
                {getFieldDecorator('amount', {
                  initialValue: collectDetail.amount,
                })(
                  <Input disabled disabled placeholder="请输入预估总价" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div><Iconfont type="huoche1" className={styles.icon} /><span>取货信息</span></div>}>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="取货地址">
                {getFieldDecorator('originAreaCode', {
                  initialValue: collectDetail.originAreaCode,
                })(
                  <Input disabled disabled placeholder="请输入取货地址" />
                  // {<Cascader options={options} onChange={onChangeOriginArea} placeholder="请输入取货地址" />}
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="取货详细地址">
                {getFieldDecorator('originAddress', {
                  initialValue: collectDetail.originAddress,
                })(
                  <Input disabled disabled placeholder="请输入取货详细地址" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div><Iconfont type="huoche" className={styles.icon} /><span>送货信息</span></div>}>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="送货地址">
                {getFieldDecorator('destAreaCode', {
                  initialValue: collectDetail.destAreaCode,
                })(
                  <Input disabled disabled placeholder="请输入取货地址" />
                  // {<Cascader options={options} onChange={onChangeOriginArea} placeholder="请输入取货地址" />}
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="送货详细地址">
                {getFieldDecorator('destAddress', {
                  initialValue: collectDetail.destAddress,
                })(
                  <Input disabled disabled placeholder="请输入详细地址" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
        <Card title={<div><Iconfont type="lianxifangshi" className={styles.icon} /><span>联系方式</span></div>}>
          <Row gutter="48">
            <Col span="12">
              <FormItem label="联系人">
                {getFieldDecorator('linkman', {
                  initialValue: collectDetail.linkman,
                })(
                  <Input disabled disabled placeholder="请输入联系人" />
                )}
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="联系电话">
                {getFieldDecorator('linkTelephone', {
                  initialValue: collectDetail.linkTelephone,
                })(
                  <Input disabled placeholder="请输入联系电话" />
                )}
              </FormItem>
            </Col>
          </Row>
          <Row gutter="48">
            <Col >
              <FormItem label="补充说明">
                {getFieldDecorator('linkRemark', {
                  initialValue: collectDetail.linkRemark,
                })(
                  <TextArea disabled autosize={{ minRows: 2, maxRows: 6 }} placeholder="补充说明" />
                )}
              </FormItem>
            </Col>
          </Row>
        </Card>
      </Form>
    </div>
  )
}

CarCollectDetail.propTypes = {
  form: PropTypes.isRequired,
  carCollectDetail: PropTypes.isRequired,
  app: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}
const CarCollectDetailForm = Form.create()(CarCollectDetail)
export default connect(({ carCollectDetail }) => ({ carCollectDetail }))(CarCollectDetailForm)
