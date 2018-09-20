import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Form, Row, Col, Input, Button, Rate } from 'antd'
import { Iconfont } from 'components'
import { config } from 'utils'
import styles from './index.less'

const FormItem = Form.Item
const { TextArea } = Input

const Appraise = ({
  dispatch,
  appraise,
  form,
  loading,
}) => {
  const { getFieldDecorator } = form
  const { orderTypes, orderState } = config
  const { orderDetail, id, type } = appraise
  const confirmComment = () => {
    form.validateFields((err, formValue) => {
      if (err) {
        return
      }
      let { star, comment } = formValue
      let obj = {
        id,
        star,
        comment,
      }
      dispatch({
        type: 'appraise/comment',
        payload: obj,
      })
    })
  }
  return (
    <div>
      <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>订单详情</span></div>}>
        <Row>
          <Col span={11}>
            <FormItem label="运单号">
              {getFieldDecorator('orderNo', {
                initialValue: orderDetail && orderDetail.orderNo,
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
          <Col span={11} offset={1}>
            <FormItem label="订单类型">
              {getFieldDecorator('orderType', {
                initialValue: orderDetail && orderTypes[orderDetail.orderType],
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span={11}>
            <FormItem label="发单时间">
              {getFieldDecorator('createdDate', {
                initialValue: (orderDetail && orderDetail.createdDate) || '',
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
          <Col span={11} offset={1}>
            <FormItem label="运单状态">
              {getFieldDecorator('orderTrans', {
                initialValue: orderDetail && orderState[orderDetail.orderTrans],
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          {
            orderDetail.orderType === 'EX' ? (
              <Col span={11}>
                <FormItem label="货物描述">
                  {getFieldDecorator('commodityDesc', {
                    initialValue: orderDetail && orderDetail.commodityDesc,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
            ) : (
              <Col span={11}>
                <FormItem label="货物名称">
                  {getFieldDecorator('commodityName', {
                    initialValue: orderDetail && orderDetail.commodityName,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
            )
          }
          <Col span={11} offset={1}>
            <FormItem label="总费用">
              {getFieldDecorator('amount', {
                initialValue: orderDetail && orderDetail.amount,
              })(
                <Input disabled addonAfter={<span>元</span>} />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          {
            orderDetail.orderType === 'BUY' ? null : (
              <Col span={11}>
                <FormItem label="取货地址">
                  {getFieldDecorator('originInfo', {
                    initialValue: orderDetail && orderDetail.originInfo + orderDetail.originAddress,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
            )
          }
          <Col span={11} offset={orderDetail.orderType === 'BUY' ? 0 : 1}>
            <FormItem label="配送地址">
              {getFieldDecorator('destInfo', {
                initialValue: orderDetail && orderDetail.destInfo + orderDetail.destAddress,
              })(
                <Input disabled />
              )}
            </FormItem>
          </Col>
        </Row>
      </Card>
      {
        type === 'driver' ? (
          <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>接运详情</span></div>}>
            <Row>
              <Col span={11}>
                <FormItem label="接运人">
                  {getFieldDecorator('nickName', {
                    initialValue: orderDetail && orderDetail.takeUser.nickName,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
              <Col span={11} offset={1}>
                <FormItem label="联系电话">
                  {getFieldDecorator('telephone', {
                    initialValue: orderDetail && orderDetail.takeUser.telephone,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span={11}>
                <FormItem label="接运时间">
                  {getFieldDecorator('takeTime', {
                    initialValue: orderDetail && orderDetail.takeTime,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
            </Row>
          </Card>
        ) : (
          <Card title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>发运详情</span></div>}>
            <Row>
              <Col span={11}>
                <FormItem label="发运人">
                  {getFieldDecorator('nickName', {
                    initialValue: orderDetail && orderDetail.createUser.nickName,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
              <Col span={11} offset={1}>
                <FormItem label="联系电话">
                  {getFieldDecorator('telephone', {
                    initialValue: orderDetail && orderDetail.createUser.telephone,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
            </Row>
            <Row>
              <Col span={11}>
                <FormItem label="发运时间">
                  {getFieldDecorator('createdDate', {
                    initialValue: orderDetail && orderDetail.createdDate,
                  })(
                    <Input disabled />
                  )}
                </FormItem>
              </Col>
            </Row>
          </Card>
        )
      }
      <Card
        title={<div className={styles.form}><Iconfont type="gouwu" className={styles.icon} /><span>物流服务评价</span></div>}
      >
        <Row style={{ textAlign: 'center' }}>
          <Col span={24}>
            <FormItem>
              {getFieldDecorator('star', {
                rules: [
                  {
                    required: true,
                    message: '请选择分数',
                  },
                ],
              })(
                <Rate style={{ fontSize: 36 }} allowHalf />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col>
            <FormItem>
              {getFieldDecorator('comment', {
              })(
                <TextArea rows={6} placeholder="请填写评价" />
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.foot}>
          <Col>
            <Button onClick={confirmComment} loading={loading.global}>发 表 评 价</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

Appraise.propTypes = {
  appraise: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
  form: PropTypes.object,
  loading: PropTypes.object,
}
const AppraiseForm = Form.create()(Appraise)
export default connect(({ appraise, loading }) => ({ appraise, loading }))(AppraiseForm)
