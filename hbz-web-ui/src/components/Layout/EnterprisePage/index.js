import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Row, Col, Card } from 'antd'
import { Iconfont } from 'components'
import TreeMenu from './treeMenu'
import styles from './index.less'

const Consignor = ({ children, menus, loading }) => {
  return (<div>
  {/* <Loader spinning={loading.effects['app/query']} />*/}
    <Row gutter="48" className={styles['container-authened']}>
      <Col offset="1" span="5">
        <Card className={styles.navCard} title={
          <div className={styles.nav}><Iconfont type="daohang" className={styles.icon} />快速导航</div>
        }>
          <TreeMenu menu={menus} />
        </Card>
      </Col>
      <Col span="17">
        {children}
      </Col>
    </Row>
  </div>)
}

Consignor.propTypes = {
  children: PropTypes.element.isRequired,
  menus: PropTypes.array,
  loading: PropTypes.object,
}

export default Consignor
