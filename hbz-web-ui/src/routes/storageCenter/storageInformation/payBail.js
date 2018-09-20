import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, message, Radio } from 'antd'
import { Iconfont, AgreeModal } from 'components'
import styles from './index.less'

const FormItem = Form.Item

const PayBail = ({
  dispatch,
  form,
  payBail,
}) => {
  const { getFieldDecorator } = form
  const { id, price, visible } = payBail
  const goPay = () => {
    const value = form.getFieldValue('isAgree')
    if (value && value === 1) {
      dispatch({
        type: 'payBail/creatPay',
        payload: {
          warehouseId: id,
          warehousePrice: price,
        },
      })
    } else {
      message.error('请点击同意货帮主租凭服务协议！')
    }
  }
  const hideModal = () => {
    dispatch({
      type: 'payBail/changeStates',
      payload: {
        visible: false,
      },
    })
  }
  const showModal = () => {
    dispatch({
      type: 'payBail/changeStates',
      payload: {
        visible: true,
      },
    })
  }
  const modalProps = {
    title: '货帮主协议',
    visible,
    hideModal,
    content: '货帮主平台代购协议......',
  }
  return (
    <div>
      <Card className={styles.bail}>
        <div className={styles.title}>诚意金说明</div>
        <p>
          诚意金撒大家分厘卡机就发健康的撒疾风连击啊的法律框架的萨芬离开觉得少了克己复礼凯撒附近的撒几点睡啊离开
          收到了看见分厘卡撒酒疯几点睡啊发了看到撒酒疯垃圾分厘卡的撒娇的萨洛克jfk的撒酒疯的撒辣椒粉离开房间打扫发
          都上了飞机离开洒家附近的撒雷克沙法律框架大梁看飞机立刻搭街坊积分 解放路口的撒酒疯就放假了了肯定是减肥就
          了的撒解放军的斯洛伐克的数据分类是弗兰克的沙发就分厘卡地方拉萨到了附近的萨洛克房间都是付款就打两份急事
        </p>
        <p>
          诚意金撒大家分厘卡机就发健康的撒疾风连击啊的法律框架的萨芬离开觉得少了克己复礼凯撒附近的撒几点睡啊离开
          收到了看见分厘卡撒酒疯几点睡啊发了看到撒酒疯垃圾分厘卡的撒娇的萨洛克jfk的撒酒疯的撒辣椒粉离开房间打扫发
          都上了飞机离开洒家附近的撒雷克沙法律框架大梁看飞机立刻搭街坊积分 解放路口的撒酒疯就放假了了肯定是减肥就
          了的撒解放军的斯洛伐克的数据分类是弗兰克的沙发就分厘卡地方拉萨到了附近的萨洛克房间都是付款就打两份急事
        </p>
        <div className={styles.price}>
          <p>仓储租凭保证金：</p>
          <div>
            <span className={styles['show-price']}>{`￥${price || '0.00'}`}</span>
          </div>
        </div>
        <Row style={{ marginTop: '20px' }}>
          <Col>
            <FormItem>
              {getFieldDecorator('isAgree', {
              })(
                <Radio.Group onChange={showModal}>
                  <Radio value={1}>点击同意货帮主租凭服务协议</Radio>
                </Radio.Group>
              )}
            </FormItem>
          </Col>
        </Row>
        <Row className={styles.pay}>
          <Col>
            <Button type="primary" onClick={goPay}>去 支 付</Button>
          </Col>
        </Row>
      </Card>
      <AgreeModal {...modalProps} />
    </div>
  )
}

PayBail.propTypes = {
  dispatch: PropTypes.func,
  storageDetail: PropTypes.object,
  payBail: PropTypes.isRequired,
  form: PropTypes.isRequired,
}
const PayBailForm = Form.create()(PayBail)
export default connect(({ payBail }) => ({ payBail }))(PayBailForm)
