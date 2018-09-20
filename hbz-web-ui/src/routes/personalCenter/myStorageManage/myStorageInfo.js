import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'dva'
import { Card, Row, Col } from 'antd'
import { Ttable } from 'components'
import { browserHistory } from 'react-router'
import styles from './index.less'

const MyStorageInfo = ({
  dispatch,
  myStorageInfo,
}) => {
  const { fields, tableTime } = myStorageInfo
  const checkStorageDetail = (data) => {
    console.log(data)
    browserHistory.push(`/personalCenter/myStorageManage/myStorageEdit?orderId=${data}`)
  }
  const state = {
    0: '未审核',
    1: '审核通过',
    2: '审核未通过',
  }
  const clazz = {
    0: 'Unaudited',
    1: 'pass',
    2: 'failed',
  }
  const columnsData = [
    {
      render: (text, record) => {
        return (
          <Card className={styles.card} onClick={checkStorageDetail.bind(this, record.id)}>
            <Row>
              <Col span={4}>
                <div className={styles['logo-img']}>
                  <img alt="照片" src={record.titleImageList ? record.titleImageList[0] : ''} />
                </div>
              </Col>
              <Col span={19} offset={1}>
                <div className={styles.title}>
                  {record.name}
                </div>
                <div className={styles.price}>
                  {`￥${record.unitPrice}元`}
                </div>
                <div className={styles[clazz[record.lifecycle]]}>
                  {state[record.lifecycle]}
                </div>
                <div>
                  {record.warehouseDescribe}
                </div>
              </Col>
            </Row>
          </Card>
        )
      },
    },
  ]
  let tableProps = {
    columns: columnsData,
    sourceUrl: '/web/warehouse/getMyWarehouseListByPage',
    fields: fields || {},
    tableTime,
    showHeader: false,
    bordered: false,
  }
  return (
    <div>
      <Card>
        <Ttable {...tableProps} />
      </Card>
    </div>
  )
}

MyStorageInfo.propTypes = {
  dispatch: PropTypes.func,
  myStorageInfo: PropTypes.object,
  loading: PropTypes.object,
}
export default connect(({ myStorageInfo }) => ({ myStorageInfo }))(MyStorageInfo)
