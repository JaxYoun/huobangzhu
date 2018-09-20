import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button } from 'antd'
import { Iconfont } from 'components'
import { Link } from 'react-router'

const DriverEnterprise = () => {
  return (<Row gutter="48">
    <Col offset="1" span="5">
      <Card>
          我是车主
      </Card>
    </Col>
  </Row>)
}

DriverEnterprise.propTypes = {
  form: PropTypes.isRequired,
  consignor: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}

export default DriverEnterprise
