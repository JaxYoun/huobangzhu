import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button } from 'antd'
import { Iconfont } from 'components'
import { Link } from 'react-router'

const Consignor = () => {
  return (<Row gutter="48">
    <Col offset="1" span="5">
      <Card>
          货物详情
      </Card>
    </Col>
  </Row>)
}

Consignor.propTypes = {
  form: PropTypes.isRequired,
  consignor: PropTypes.isRequired,
  dispatch: PropTypes.func.isRequired,
}

export default Consignor
