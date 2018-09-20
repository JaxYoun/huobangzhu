import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, message } from 'antd'
import { Iconfont } from 'components'
import { browserHistory } from 'react-router'

const WalletManage = ({
  dispatch,
  walletManage,
}) => {
  return (
    <div>
      111
    </div>
  )
}

WalletManage.propTypes = {
  walletManage: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ walletManage }) => ({ walletManage }))(WalletManage)
