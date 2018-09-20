import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, message } from 'antd'
import { Iconfont } from 'components'
import { browserHistory } from 'react-router'
import styles from './index.less'

const Info = ({
  dispatch,
  info,
}) => {
  return (
    <div>
      <Card className={styles.info}>
        <div className={styles.title}>
          货帮主App上线了，分享有礼活动同时展开
        </div>
        <div className={styles.time}>
          2017年10月15日
        </div>
        <p>
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
        </p>
        <p>
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
        </p>
        <p>
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
          下载分享给你的朋友将获取10元代金券一张，可用于发货及支付平台保险购买等业务；
        </p>
        <div className={styles.img}>
          <img alt="照片" />
        </div>
      </Card>
    </div>
  )
}

Info.propTypes = {
  Info: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ info }) => ({ info }))(Info)
