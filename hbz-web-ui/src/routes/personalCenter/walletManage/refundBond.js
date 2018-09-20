import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, message } from 'antd'
import { Iconfont } from 'components'
import { browserHistory } from 'react-router'
import styles from './index.less'

const RefundBond = ({
  dispatch,
  refundBond,
}) => {
  return (
    <div>
      <Card className={styles.card}>
        <div className={styles.title}>保证金退款说明</div>
        <div className={styles.content}>
          <p>觉得少了看风景卢卡斯搭街坊卢卡斯京东方阿斯顿见佛萨地方级的萨芬啊松井大辅啊收到了就开放撒旦克己复礼凯撒的解放立刻啊士大夫立刻啊撒旦解放拉萨扩大解放卢卡斯吉多发反对苏联空军弗兰克的撒娇分厘卡圣诞节啊弗兰克撒旦解放了可是大。</p>
          <p>
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
            保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金保证金
          </p>
        </div>
        <div className={styles.declare}>
          <div>* 退款后将不再享受以下特权</div>
          <p>
            1.保证金标签
          </p>
          <p>
            2.订单优先处理
          </p>
        </div>
        <div className={styles.price}>
          <p>发货保证金：</p>
          <span className={styles['show-price']}>￥0.00</span>
        </div>
        <div className={styles.foot}>
          <Button>退款</Button>
        </div>
      </Card>
    </div>
  )
}

RefundBond.propTypes = {
  refundBond: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ refundBond }) => ({ refundBond }))(RefundBond)
