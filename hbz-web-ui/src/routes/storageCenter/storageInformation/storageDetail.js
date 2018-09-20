import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Icon, Card, Form, Row, Col, Input, Button, message } from 'antd'
import { Map, Marker, ScaleControl, NavigationControl } from 'react-bmap'
import { Iconfont } from 'components'
import { browserHistory } from 'react-router'
import styles from './index.less'

const StorageDetail = ({
  dispatch,
  storageDetail,
}) => {
  const { detailData, id } = storageDetail
  const lease = () => {
    browserHistory.push(`/storageCenter/storageInformation/payBail?orderId=${id}`)
  }
  return (
    <div>
      <Card className={styles.detail}>
        <div className={styles.title}>
          {detailData.name}
        </div>
        <div className={styles.creatTime}>{detailData.formatedCreateDate}</div>
        <ul>
          <li>
            <p>仓库容量</p>
            <p><span>{`${detailData.capacity}/㎡`}</span></p>
          </li>
          <li>
            <p>租赁价格</p>
            <p><span>{`${detailData.unitPrice}元/㎡`}</span></p>
          </li>
          <li>
            <p>租赁时间</p>
            <p><span>{`${detailData.minRentTime}个月以上`}</span></p>
          </li>
        </ul>
      </Card>
      <Card>
        <div className={styles.info}>
          <span>联系人：{detailData.ownerName}</span>
          <span>联系电话：{detailData.telephone}</span>
          <span>仓库地址：{detailData.address}</span>
        </div>
        <Map center={{ lng: detailData.coordX, lat: detailData.coordY }} zoom="13">
          <Marker
            position={{ lng: detailData.coordX, lat: detailData.coordY }}
            icon="loc_red"
          />
          <ScaleControl />
          <NavigationControl />
        </Map>
      </Card>
      <Card>
        <Row className={styles.foot}>
          <Col>
            <Button type="primary" onClick={lease}>租 赁</Button>
          </Col>
        </Row>
      </Card>
    </div>
  )
}

StorageDetail.propTypes = {
  storageDetail: PropTypes.object,
  dispatch: PropTypes.func.isRequired,
}

export default connect(({ storageDetail }) => ({ storageDetail }))(StorageDetail)
